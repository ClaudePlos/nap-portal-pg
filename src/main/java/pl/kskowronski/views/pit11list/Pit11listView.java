package pl.kskowronski.views.pit11list;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ResourceUtils;
import pl.kskowronski.data.MapperDate;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.egeria.css.SK;
import pl.kskowronski.data.entity.egeria.eDek.EdktDeklaracjeDTO;
import pl.kskowronski.data.entity.egeria.ek.Zatrudnienie;
import pl.kskowronski.data.entity.log.LogEvent;
import pl.kskowronski.data.entity.log.LogPit11;
import pl.kskowronski.data.reports.Pit11Service;
import pl.kskowronski.data.service.UserService;
import pl.kskowronski.data.service.admin.PdfService;
import pl.kskowronski.data.service.egeria.css.SKService;
import pl.kskowronski.data.service.egeria.edek.EdktDeklaracjeService;
import pl.kskowronski.data.service.log.LogPit11Service;
import pl.kskowronski.views.MainLayout;

import javax.annotation.security.RolesAllowed;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Route(value = "listPit11", layout = MainLayout.class)
@PageTitle("Pit11 lista")
@RolesAllowed({"admin","supervisor"})
public class Pit11listView extends VerticalLayout {

    MapperDate mapperDate = new MapperDate();
    private UserService userService;
    private EdktDeklaracjeService edktDeklaracjeService;
    private SKService skService;
    SimpleDateFormat dtYYYY = new SimpleDateFormat("yyyy");
    private Pit11Service pit11Service;
    private PdfService pdfService;

    private Grid<EdktDeklaracjeDTO> grid;
    private Optional<User> worker;
    private NumberField yearField = new NumberField();
    private Select<SK> selectSK = new Select<>();

    private HorizontalLayout hTop = new HorizontalLayout();
    private LogPit11Service logPit11Service;


    @Autowired
    public Pit11listView(UserService userService, EdktDeklaracjeService edktDeklaracjeService, SKService skService
            , Pit11Service pit11Service, PdfService pdfService, LogPit11Service logPit11Service) throws ParseException {
        this.userService = userService;
        this.edktDeklaracjeService = edktDeklaracjeService;
        this.skService = skService;
        this.pit11Service = pit11Service;
        this.pdfService = pdfService;
        this.logPit11Service = logPit11Service;

        setHeight("85%");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        worker = userService.findByUsername(userDetails.getUsername());
        hTop.setClassName("hTop");
        addYearField();
        addSelectSK();

        add(hTop);
        Button buttonRun = new Button("Pobierz");
        buttonRun.setClassName("buttonRun");
        buttonRun.addClickListener(clickEvent -> {
            onUserChangedYear(String.valueOf(yearField.getValue().intValue()));
        });
        hTop.add(buttonRun);

        this.grid = new Grid<>(EdktDeklaracjeDTO.class);
        grid.setColumns("dklTdlKod", "dklFrmNazwa", "dklPrcNazwisko", "dklPrcImie", "dklYear"); //, "dklXmlVisual"

        // For mobile
        grid.addComponentColumn(item -> {
            VerticalLayout vl = new VerticalLayout();
            vl.add(new Html("<div><b>"+item.getDklFrmNazwa() +"</b></div>"));
            vl.add(new Html("<div><b></b> "+item.getDklPrcNazwisko()+ " " +item.getDklPrcImie()+ "</div>"));
            vl.add(new Html("<div><b>Rok:</b> "+ item.getDklYear() +"</div>"));
            return vl;
        }).setHeader("Pit").setVisible(false);

        // For Browser
        grid.getColumnByKey("dklTdlKod").setWidth("100px").setHeader("Dek");
        grid.getColumnByKey("dklYear").setWidth("100px").setHeader("Rok");
        //grid.getColumnByKey("dklXmlVisual").setWidth("100px").setHeader("Xml");
        grid.getColumnByKey("dklFrmNazwa").setWidth("300px").setHeader("Firma");
        grid.setHeightFull();
        // run generate pit pdf
        grid.addColumn(new NativeButtonRenderer<EdktDeklaracjeDTO>("Pit11",
                item -> {
                    try {
                        String path = pit11Service.exportPit11Report("pdf", worker.get().getPassword(), dtYYYY.format(item.getDklDataOd()),  item.getDklXmlVisual());
                        displayPitPDFonBrowser(path, "Company:" + item.getDklFrmNazwa());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (JRException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ));

        add(grid);

    }


    private void onUserChangedYear(String year)  {
        try {
            Optional<List<EdktDeklaracjeDTO>> listEDeklaracje =  edktDeklaracjeService.getListPit11ForSupervisor(year, selectSK.getValue().getSkId());
            grid.setItems(listEDeklaracje.get());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void addYearField() {
        yearField.setClassName("yearField");
        yearField.setLabel("Rok");
        yearField.getElement().setProperty("title", "Test");
        yearField.setHasControls(true);
        yearField.setValue(Double.parseDouble(Long.parseLong(mapperDate.getCurrentlyYear())-1L + ""));
        yearField.addValueChangeListener( e-> {
            try {
                onUserChangedYear( String.valueOf(e.getValue().intValue()));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        hTop.add(yearField);
    }

    private void addSelectSK() {
        List<SK> listSK = skService.findSkForSupervisor( worker.get().getPrcId() );
        selectSK.setClassName("selectSK");
        selectSK.setItems(listSK);
        selectSK.setItemLabelGenerator(SK::getSkKod);
        if (listSK.size() > 0 )
            selectSK.setEmptySelectionCaption(listSK.get(0).getSkKod());
        selectSK.setLabel("Obiekt");
        if (listSK.size() > 0 )
            selectSK.setValue(listSK.get(0));
        hTop.add(selectSK);
    }



    private void displayPitPDFonBrowser(String path, String description) throws FileNotFoundException, IOException {

        File filePdf = ResourceUtils.getFile(path);

        byte[] pdfBytes = FileUtils.readFileToByteArray(filePdf);

        Dialog dialog = new Dialog();
        dialog.setWidth("300px");
        dialog.setHeight("150px");

        StreamResource res = new StreamResource("file.pdf", () -> new ByteArrayInputStream(pdfBytes));
        String reportName = "reportPit11";
        Anchor a = new Anchor(res, "kliknij tu by pobrać pit11");
        a.setId(reportName);
        a.setTarget( "_blank" );
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

        saveLog(description);
    }

    private void saveLog(String description){
        LogPit11 logPit11 = new LogPit11();
        logPit11.setPrcId(worker.get().getPrcId());
        logPit11.setEvent(LogEvent.DOWNLOAD_THE_DECLARATION_PIT11.toString());
        logPit11.setYear( Integer.valueOf(yearField.getValue().toString().substring(0,4)) );
        logPit11.setAuditDc(new Date());
        logPit11.setDescription(description);
        logPit11Service.save(logPit11);
    }




    // Functions for mobile version
    private Registration listener;
    private int breakpointPx = 1000;

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe width change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            adjustVisibleGridColumns(grid, event.getWidth());
        }));
        // Adjust Grid according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserWidth = receiver.getBodyClientWidth();
            adjustVisibleGridColumns(grid, browserWidth);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }


    private void adjustVisibleGridColumns(Grid<EdktDeklaracjeDTO> grid, int width) {
        boolean[] visibleCols;
        // Change which columns are visible depending on browser width
        if (width > breakpointPx) {
            visibleCols = new boolean[]{true, true, true, true, true, false};
        } else {
            visibleCols = new boolean[]{false, false, false, false, false, true};
        }
        for (int c = 0; c < visibleCols.length; c++) {
            grid.getColumns().get(c).setVisible(visibleCols[c]);
        }
    }
    // End function form mobile version
}
