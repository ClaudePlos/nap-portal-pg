package pl.kskowronski.views.pit11;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ResourceUtils;
import pl.kskowronski.data.MapperDate;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.egeria.eDek.EdktDeklaracjeDTO;
import pl.kskowronski.data.entity.log.LogEvent;
import pl.kskowronski.data.entity.log.LogPit11;
import pl.kskowronski.data.reports.Pit11Service;
import pl.kskowronski.data.service.UserService;
import pl.kskowronski.data.service.admin.PdfService;
import pl.kskowronski.data.service.egeria.edek.EdktDeklaracjeService;
import pl.kskowronski.data.service.log.LogPit11Service;
import pl.kskowronski.views.MainLayout;

import javax.annotation.security.RolesAllowed;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Route(value = "Pit11", layout = MainLayout.class)
@PageTitle("Pit11")
@RolesAllowed("user")
public class Pit11View extends VerticalLayout {

    private Grid<EdktDeklaracjeDTO> grid;

    private UserService userService;
    private EdktDeklaracjeService edktDeklaracjeService;
    private LogPit11Service logPit11Service;
    private PdfService pdfService;


    @Autowired
    Pit11Service pit11Service;

    SimpleDateFormat dtYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dtYYYY = new SimpleDateFormat("yyyy");

    MapperDate mapperDate = new MapperDate();

    Label labelXML = new Label();
    NumberField yearField = new NumberField();

    private Optional<User> worker;

    @Autowired
    public Pit11View( UserService userService, LogPit11Service logPit11Service
            , EdktDeklaracjeService edktDeklaracjeService, PdfService pdfService) {
        setHeight("85%");
        this.userService = userService;
        this.edktDeklaracjeService = edktDeklaracjeService;
        this.logPit11Service = logPit11Service;
        this.pdfService = pdfService;

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
        add(yearField);
        setHorizontalComponentAlignment(Alignment.START, yearField);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        worker = userService.findByUsername(userDetails.getUsername());


        setId("pit11-view");

        this.grid = new Grid<>(EdktDeklaracjeDTO.class);
        grid.setColumns("dklTdlKod", "dklFrmNazwa", "dklYear"); //, "dklXmlVisual"
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

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);

        add(splitLayout);

        refreshGrid();

        grid.addItemClickListener(
                event -> {
                    labelXML.setText(event.getItem().getDklXmlVisual());
                });

        onUserChangedYear(Long.parseLong(mapperDate.getCurrentlyYear())-1L + "");

    }

    private void generateDateInGrid(EdktDeklaracjeDTO item){
        grid.setItems(item);
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
        a.getElement().getStyle().set("display", "none");
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

        Page page = UI.getCurrent().getPage();
        page.executeJavaScript("document.getElementById('"+reportName+"').click();");

        dialog.add(a, new Html("<div><br><div>"), new Button("Zamknij", e -> dialog.close()));
        add(dialog);
        dialog.open();

        saveLog(description);
    }

    private void onUserChangedYear(String year){
        try {
            Optional<List<EdktDeklaracjeDTO>> listEDeklaracje =  edktDeklaracjeService.findAllByDklPrcId(worker.get().getPrcId(), year);
            if (listEDeklaracje.get().size() != 0){
                listEDeklaracje.get()
                        .stream()
                        .filter( i -> i.getDklStatus().equals(BigDecimal.valueOf(50L)) || i.getDklStatus().equals(BigDecimal.valueOf(40L))) // status have UPO
                        .sorted(Comparator.comparing(EdktDeklaracjeDTO::getDklDataOd).reversed())
                        .collect(Collectors.toList());
            } else {
                Notification.show("Brak deklaracji w roku: " + year, 3000, Notification.Position.MIDDLE);
            }
            grid.setItems(listEDeklaracje.get());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        wrapper.setHeightFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid, labelXML);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
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
}




