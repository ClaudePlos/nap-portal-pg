package pl.kskowronski.views.payslips;


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
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import pl.kskowronski.data.MapperDate;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.egeria.ek.Zatrudnienie;
import pl.kskowronski.data.reports.PayslipisService;
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

@Route(value = "payslips", layout = MainLayout.class)
@PageTitle("Paski")
@RolesAllowed({"admin","supervisor","user"})
public class PayslipsView extends VerticalLayout {

    private ZatrudnienieService zatrudnienieService;
    private PayslipisService payslipisService;
    private MapperDate mapperDate = new MapperDate();
    private PdfService pdfService;

    private Grid<Zatrudnienie> gridContracts;

    private Button butPlus = new Button("+");
    private Button butMinus = new Button("-");
    private TextField textPeriod = new TextField("Okres");

    private User worker;


    @Autowired
    public PayslipsView( ZatrudnienieService zatrudnienieService, PayslipisService payslipisService, PdfService pdfService) throws ParseException {
        setId("payslips-view");
        setHeight("80%");
        this.zatrudnienieService = zatrudnienieService;
        this.payslipisService = payslipisService;
        this.pdfService = pdfService;
        VaadinSession session = VaadinSession.getCurrent();
        worker = session.getAttribute(User.class);
        textPeriod.setWidth("100px");

        this.gridContracts = new Grid<>(Zatrudnienie.class);
        gridContracts.setClassName("gridContracts");
        gridContracts.setColumns();
        //gridContracts.addColumn("frmNazwa");
        gridContracts.addColumn(TemplateRenderer.<Zatrudnienie> of(
                "<div class=\"gridFirma\">[[item.firma]]</div>")
                .withProperty("firma", Zatrudnienie::getFrmNazwa))
                .setHeader("Firma");
        gridContracts.addColumn("zatDataPrzyj").setHeader("Data przyjęcia");
        gridContracts.addColumn("zatDataZmiany").setHeader("Data zmiany");
        gridContracts.addColumn("zatDataDo").setHeader("Data do");
        gridContracts.setHeightFull();


        gridContracts.addColumn(new NativeButtonRenderer<Zatrudnienie>("Pasek",
                item -> {
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

                }));




        Date now =  Date.from(LocalDate.now().minus(1, ChronoUnit.MONTHS).atStartOfDay(ZoneId.systemDefault()).toInstant());
        textPeriod.setClassName("textPeriod");
        textPeriod.setValue(mapperDate.dtYYYYMM.format(now));
        textPeriod.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);;
        butPlus.setClassName("butPlus");
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
        butMinus.setClassName("butMinus");
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

    private void getCotractForPeriod() throws ParseException {
        Optional<List<Zatrudnienie>> contracts = zatrudnienieService.getActualContractForWorker(worker.getPrcId(), textPeriod.getValue());
        if (!contracts.isPresent()){
            Notification.show("Brak umów w danym okresie", 3000, Notification.Position.MIDDLE);
        }
        gridContracts.setItems(contracts.get());
    }

    private void GeneratePayslipiPDF(Integer prcId, Integer frmId) throws IOException {
        String path = this.payslipisService.przygotujPaski(null, prcId, textPeriod.getValue(), frmId, Long.parseLong("0"));//0 - full time job, 2 - contract
        displayPayslipsPDFonBrowser(path);
    }


    private void displayPayslipsPDFonBrowser(String path) throws IOException {

        File filePdf = ResourceUtils.getFile(path);

        byte[] pdfBytes = FileUtils.readFileToByteArray(filePdf);

        Dialog dialog = new Dialog();
        dialog.setWidth("300px");
        dialog.setHeight("150px");

        StreamResource res = new StreamResource("file.pdf", () -> new ByteArrayInputStream(pdfBytes));
        String reportName = "reportPayslip";
        Anchor a = new Anchor(res, "kliknij tu by pobrać pasek");
        a.setId(reportName);
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
        a.setTarget( "_blank" ) ;

        Page page = UI.getCurrent().getPage();
        page.executeJavaScript("document.getElementById('"+reportName+"').click();");

        dialog.add(a, new Html("<div><br><div>"), new Button("Zamknij", e -> dialog.close()));
        add(dialog);
        dialog.open();
    }




}
