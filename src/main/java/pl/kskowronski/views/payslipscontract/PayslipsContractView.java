package pl.kskowronski.views.payslipscontract;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import pl.kskowronski.data.MapperDate;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.egeria.ek.Zatrudnienie;
import pl.kskowronski.data.reports.PayslipisService;
import pl.kskowronski.data.service.UserService;
import pl.kskowronski.data.service.admin.PdfService;
import pl.kskowronski.data.service.egeria.ek.ZatrudnienieService;
import pl.kskowronski.views.MainLayout;

import javax.annotation.security.RolesAllowed;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Route(value = "contract-payslips", layout = MainLayout.class)
@PageTitle("Paski UZ")
@RolesAllowed({"admin","supervisor","user"})
public class PayslipsContractView extends VerticalLayout {

    private transient ZatrudnienieService zatrudnienieService;
    private transient PayslipisService payslipisService;
    private transient MapperDate mapperDate = new MapperDate();
    private PdfService pdfService;
    private UserService userService;

    private Grid<Zatrudnienie> gridContracts;

    private Button butPlus = new Button("+");
    private Button butMinus = new Button("-");
    private TextField textPeriod = new TextField("Okres");

    private transient User worker;

    private Optional<List<Zatrudnienie>> contracts;

    @Autowired
    public PayslipsContractView(ZatrudnienieService zatrudnienieService, PayslipisService payslipisService, PdfService pdfService, UserService userService) throws ParseException {
        setId("payslips-view");
        setHeight("80%");
        this.zatrudnienieService = zatrudnienieService;
        this.payslipisService = payslipisService;
        this.pdfService = pdfService;
        this.userService = userService;
        VaadinSession session = VaadinSession.getCurrent();
        worker = session.getAttribute(User.class);
        textPeriod.setWidth("100px");

        this.gridContracts = new Grid<>(Zatrudnienie.class);
        gridContracts.setClassName("gridContracts");
        gridContracts.setColumns();

        // For mobile
        gridContracts.addComponentColumn(item -> {
            VerticalLayout vl = new VerticalLayout();
            vl.add(new Html("<div>"+item.getFrmNazwa() +"</div>"));
            vl.add(new Html("<div><b>Data przy.:</b> "+item.getZatDataPrzyj()+"</div>"));
            vl.add(new Html("<div><b>Data zmiany:</b> "+item.getZatDataZmiany()+"</div>"));
            String dataDo = "";
            if (item.getZatDataDo() != null) {
                dataDo =  item.getZatDataDo().toString();
            }
            vl.add(new Html("<div><b>Data do:</b> "+ dataDo +"</div>"));
            vl.add(new Html("<div><b>Umowa:</b> "+item.getZatNrUmowy()+"</div>"));
            return vl;
        }).setHeader("Pasek").setVisible(false);

        // For Browser
        gridContracts.addColumn(TemplateRenderer.<Zatrudnienie> of(
                "<div class=\"gridFirma\">[[item.firma]]</div>")
                .withProperty("firma", Zatrudnienie::getFrmNazwa))
                .setHeader("Firma");
        gridContracts.addColumn("zatDataPrzyj").setHeader("Data przyjęcia");
        gridContracts.addColumn("zatDataZmiany").setHeader("Data zmiany");
        gridContracts.addColumn("zatDataDo").setHeader("Data do");
        gridContracts.addColumn("zatNrUmowy").setHeader("Numer Umowy");
        gridContracts.setHeightFull();

        gridContracts.addComponentColumn(item -> createButtonPayslipLink(item)).setHeader("");

        Date now =  Date.from(LocalDate.now().minus(1, ChronoUnit.MONTHS).atStartOfDay(ZoneId.systemDefault()).toInstant());
        textPeriod.setValue(mapperDate.dtYYYYMM.format(now));
        textPeriod.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        butPlus.setWidth("5px");
        butPlus.addClickListener(event ->{
            Long mc = Long.parseLong(textPeriod.getValue().substring(textPeriod.getValue().length()-2));
            if ( mc < 12 ){
                mc++; String mcS = "0" + mc;
                textPeriod.setValue(textPeriod.getValue().substring(0,5) + mcS.substring(mcS.length()-2) );
            } else {
                Long year = Long.parseLong(textPeriod.getValue().substring(0,4));
                year++;
                textPeriod.setValue(year + "-01" );
            }
            try {
                getCotractForPeriod();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        butMinus.addClickListener(event ->{
            Long mc = Long.parseLong(textPeriod.getValue().substring(textPeriod.getValue().length()-2));
            if ( mc > 1 ){
                mc--; String mcS = "0" + mc;
                textPeriod.setValue(textPeriod.getValue().substring(0,5) + mcS.substring(mcS.length()-2) );
            } else {
                Long year = Long.parseLong(textPeriod.getValue().substring(0,4));
                year--;
                textPeriod.setValue(year + "-12" );
            }
            try {
                getCotractForPeriod();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        HorizontalLayout hPeriod = new HorizontalLayout();
        hPeriod.add(butMinus, textPeriod, butPlus);
        hPeriod.setClassName("hPeriod");
        add(hPeriod);


        add(gridContracts);
        getCotractForPeriod();

    }

    private Button createButtonPayslipLink(Zatrudnienie item) {
        Button button = new Button();
        if ( !item.isSecondContractOnHours() || contracts.get().size() == 1){
            button = new Button("Pasek", clickEvent -> {
                Date periodNow  = null;
                Date periodParam = null;
                try {
                    periodNow = Date.from(LocalDate.now().minus(1, ChronoUnit.MONTHS).atStartOfDay(ZoneId.systemDefault()).toInstant());
                    periodParam = mapperDate.dtYYYYMM.parse(textPeriod.getValue());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if ( periodParam.before(periodNow) ) {

                    if (mapperDate.dtYYYYMM.format(periodNow).equals(textPeriod.getValue()) && Long.parseLong(mapperDate.dtDD.format(periodNow)) < 10L){
                        Notification.show("Pasek jeszcze niedostępny. Pasek za ostatni miesiąc będzie dostępny po 10 danego miesiąca.", 5000, Notification.Position.MIDDLE);
                        return;
                    }

                    try {
                        GeneratePayslipiPDF(item.getZatPrcId(), item.getFrmId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Notification.show("Pasek jeszcze niedostępny. Pasek za ostatni miesiąc będzie dostępny po 10 danego miesiąca.", 5000, Notification.Position.MIDDLE);
                }
            });
        }

        return button;
    }

    private void getCotractForPeriod() throws ParseException {
        contracts = zatrudnienieService.getActualContractUzForWorker(worker.getPrcId(), textPeriod.getValue());
        if (!contracts.isPresent()){
            Notification.show("Brak umów w danym okresie", 3000, Notification.Position.MIDDLE);
        }
        if (contracts.isPresent())
            gridContracts.setItems(contracts.get());
    }

    private void GeneratePayslipiPDF(Integer prcId, Integer frmId) throws IOException {
        String path = this.payslipisService.przygotujPaski(null, prcId, textPeriod.getValue(), frmId, Long.parseLong("2"));//0 - full time job, 2 - contract
        displayPayslipsPDFonBrowser(path);
    }


    private void displayPayslipsPDFonBrowser(String path) throws IOException {

        File filePdf = ResourceUtils.getFile(path);

        byte[] pdfBytes = FileUtils.readFileToByteArray(filePdf);

        Dialog dialog = new Dialog();
        dialog.setWidth("300px");
        dialog.setHeight("150px");

        StreamResource res = new StreamResource("file.pdf", () -> new ByteArrayInputStream(pdfBytes));
        String reportName = "reportPayslipUz";
        Anchor a = new Anchor(res, "kliknij tu by pobrać pasek");
        a.setId(reportName);
        a.setTarget( "_blank" ) ;
        a.getElement().addEventListener("click", event -> {
            new Thread(() -> { // asynchronous
                try {
                    pdfService.removeFileFromDisk(path);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            dialog.close();
        });

        if (!userService.isMobileDevice()) {
            Page page = UI.getCurrent().getPage();
            page.executeJavaScript("document.getElementById('"+reportName+"').click();");
        }

        dialog.add(a, new Html("<div><br><div>"), new Button("Zamknij", e -> dialog.close()));
        add(dialog);
        dialog.open();
    }


    // Functions for mobile version
    private Registration listener;
    private int breakpointPx = 1000;

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe width change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            adjustVisibleGridColumns(gridContracts, event.getWidth());
        }));
        // Adjust Grid according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserWidth = receiver.getBodyClientWidth();
            adjustVisibleGridColumns(gridContracts, browserWidth);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }


    private void adjustVisibleGridColumns(Grid<Zatrudnienie> grid, int width) {
        boolean[] visibleCols;
        // Change which columns are visible depending on browser width
        if (width > breakpointPx) {
            visibleCols = new boolean[]{false, true, true, true, true, true};
        } else {
            visibleCols = new boolean[]{true, false, false, false, false, false};
        }
        for (int c = 0; c < visibleCols.length; c++) {
            grid.getColumns().get(c).setVisible(visibleCols[c]);
        }
    }
    // End function form mobile version

}
