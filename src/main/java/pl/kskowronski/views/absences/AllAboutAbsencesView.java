package pl.kskowronski.views.absences;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kskowronski.data.MapperDate;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.egeria.eDek.EdktDeklaracjeDTO;
import pl.kskowronski.data.entity.egeria.ek.AbsenceDTO;
import pl.kskowronski.data.entity.egeria.ek.AbsenceLimitDTO;
import pl.kskowronski.data.service.egeria.ek.AbsenceLimitService;
import pl.kskowronski.data.service.egeria.ek.AbsenceService;
import pl.kskowronski.views.MainLayout;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;


@Route(value = "absences", layout = MainLayout.class)
@PageTitle("Twój urlop")
@RolesAllowed({"admin","supervisor","user","manager"})
public class AllAboutAbsencesView extends VerticalLayout {

    private Grid<AbsenceLimitDTO> gridAbLimit;
    private Grid<AbsenceDTO> grid;
    NumberField yearField = new NumberField();
    transient MapperDate mapperDate = new MapperDate();
    transient AbsenceService absenceService;
    transient AbsenceLimitService absenceLimitService;
    String width130 = "130px";
    private transient User worker;

    public AllAboutAbsencesView(@Autowired AbsenceService absenceService, @Autowired AbsenceLimitService absenceLimitService) throws Exception {
        this.absenceService = absenceService;
        this.absenceLimitService = absenceLimitService;
        setId("all-about-absences-view");
        VaadinSession session = VaadinSession.getCurrent();
        worker = session.getAttribute(User.class);

        Optional<List<AbsenceLimitDTO>> listAbsencesLimits
                = absenceLimitService.findAllAbsenceLimitForPrcIdAndYear(worker.getPrcId()
                , mapperDate.getCurrentlyYear()
                , "'A_UR1','UR91','UR31','UOG','UR3','L_PZO'"); // ,'A_UR11' na zadanie
        if (listAbsencesLimits.get().size() == 0){
            Notification.show("Brak limitów urlopowych w roku: " + mapperDate.getCurrentlyYear(), 3000, Notification.Position.MIDDLE);
        }

        Label labTitleGridLimit = new Label("Twojego urlopu w roku " + mapperDate.getCurrentlyYear() + " zostało:");
        add(labTitleGridLimit);
        this.gridAbLimit = new Grid<>(AbsenceLimitDTO.class);
        gridAbLimit.setColumns("nazwaWymiaru", "kodUrlopu", "ldOd", "ldDo", "pozostaloUrlopu", "frmNazwa"); //, "zaleglyUrlop"

        // For mobile
        gridAbLimit.addComponentColumn(item -> {
            VerticalLayout vl = new VerticalLayout();
            vl.add(new Html("<div><b>"+item.getFrmNazwa() +"</b></div>"));
            vl.add(new Html("<div><b>"+item.getNazwaWymiaru() +"</b></div>"));
            vl.add(new Html("<div><b>Od:</b> "+item.getLdOd()+ " <b>Od:</b> " +item.getLdDo()+ "</div>"));
            vl.add(new Html("<div><b>Pozostało:</b> "+ item.getPozostaloUrlopu() +"</div>"));
            return vl;
        }).setHeader("Urlop zostało").setVisible(false);

        // For Browser
        gridAbLimit.getColumnByKey("nazwaWymiaru").setWidth("200px").setHeader("Nazwa");
        gridAbLimit.getColumnByKey("kodUrlopu").setWidth("80px").setHeader("Kod");
        gridAbLimit.getColumnByKey("ldOd").setWidth(width130).setHeader("Od");
        gridAbLimit.getColumnByKey("ldDo").setWidth(width130).setHeader("Do");
        gridAbLimit.getColumnByKey("pozostaloUrlopu").setWidth("100px").setHeader("Zostało dni");
        gridAbLimit.getColumnByKey("frmNazwa").setWidth("250px").setHeader("Firma");
        if (listAbsencesLimits.isPresent())
            gridAbLimit.setItems(listAbsencesLimits.get());
        gridAbLimit.setHeight("150px");
        add(gridAbLimit);


        Label labTitleGrid = new Label("Twoje absencje w roku:");
        add(labTitleGrid);

        yearField.setLabel("Rok");
        yearField.getElement().setProperty("title", "Test");
        yearField.setHasControls(true);
        yearField.setValue(Double.parseDouble(mapperDate.getCurrentlyYear()));
        yearField.addValueChangeListener( e-> {
            try {
                onAbsenceChangeYear( String.valueOf(e.getValue().intValue()));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        add(yearField);
        setHorizontalComponentAlignment(Alignment.START, yearField);



        this.grid = new Grid<>(AbsenceDTO.class);
        grid.setColumns("abTypeOfAbsence", "abDataOd", "abDataDo", "abDniWykorzystane", "abGodzinyWykorzystane", "abFrmName");

        // For mobile
        grid.addComponentColumn(item -> {
            VerticalLayout vl = new VerticalLayout();
            vl.add(new Html("<div><b>"+item.getAbFrmName() +"</b></div>"));
            vl.add(new Html("<div>"+item.getAbTypeOfAbsence() +"</div>"));
            vl.add(new Html("<div><b>Od:</b> "+item.getAbDataOd()+ " <b>Od:</b> " +item.getAbDataDo() + "</div>"));
            vl.add(new Html("<div><b>Dni:</b> "+ item.getAbDniWykorzystane() + "<b> Godz:</b> "+ item.getAbGodzinyWykorzystane() +"</div>"));
            return vl;
        }).setHeader("Urlop wykorzystano").setVisible(false);

        // For Browser
        grid.getColumnByKey("abTypeOfAbsence").setWidth("200px").setHeader("Rodzaj");
        grid.getColumnByKey("abDataOd").setWidth(width130).setHeader("Data Od");
        grid.getColumnByKey("abDataDo").setWidth(width130).setHeader("Data Do");
        grid.getColumnByKey("abDniWykorzystane").setWidth("80px").setHeader("Dni");
        grid.getColumnByKey("abGodzinyWykorzystane").setWidth("80px").setHeader("Godz.");
        grid.getColumnByKey("abFrmName").setWidth("250px").setHeader("Firma");
        onAbsenceChangeYear(String.valueOf(yearField.getValue().intValue()));
        add(grid);

        //TODO add eligible days of holiday
    }

    private void onAbsenceChangeYear(String year) throws Exception {
        Optional<List<AbsenceDTO>> listAbsences = absenceService.findAllByAbPrcIdForYear(worker.getPrcId(), year);
        if (listAbsences.get().size() == 0) {
            Notification.show("Brak absencji w roku: " + year, 3000, Notification.Position.MIDDLE);
        }
        if (listAbsences.isPresent())
            grid.setItems(listAbsences.get());
        grid.getDataProvider().refreshAll();
    }


    // Functions for mobile version
    private Registration listener;
    private int breakpointPx = 1000;

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe width change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            adjustVisibleGridColumns(gridAbLimit, event.getWidth());
            adjustVisibleGridColumns2(grid, event.getWidth());
        }));
        // Adjust Grid according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserWidth = receiver.getBodyClientWidth();
            adjustVisibleGridColumns(gridAbLimit, browserWidth);
            adjustVisibleGridColumns2(grid, browserWidth);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }


    private void adjustVisibleGridColumns(Grid<AbsenceLimitDTO> grid, int width) {
        boolean[] visibleCols;
        // Change which columns are visible depending on browser width
        if (width > breakpointPx) {
            visibleCols = new boolean[]{true, true, true, true, true, true, false};
        } else {
            visibleCols = new boolean[]{false, false, false, false, false, false, true};
        }
        for (int c = 0; c < visibleCols.length; c++) {
            grid.getColumns().get(c).setVisible(visibleCols[c]);
        }
    }

    private void adjustVisibleGridColumns2(Grid<AbsenceDTO> grid, int width) {
        boolean[] visibleCols;
        // Change which columns are visible depending on browser width
        if (width > breakpointPx) {
            visibleCols = new boolean[]{true, true, true, true, true, true, false};
        } else {
            visibleCols = new boolean[]{false, false, false, false, false, false, true};
        }
        for (int c = 0; c < visibleCols.length; c++) {
            grid.getColumns().get(c).setVisible(visibleCols[c]);
        }
    }
    // End function form mobile version

}
