package pl.kskowronski.views.reports.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.vaadin.reports.PrintPreviewReport;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.egeria.css.SK;
import pl.kskowronski.data.service.UserService;
import pl.kskowronski.data.service.egeria.css.SKService;
import pl.kskowronski.data.service.reports.ReportService;

import java.util.List;
import java.util.Optional;

public class WorkerWithPassView extends Div {

    private Optional<User> loggedUser;
    private Select<SK> selectSK = new Select<>();
    private HorizontalLayout hTop = new HorizontalLayout();
    private VerticalLayout vTop = new VerticalLayout();
    private List<SK> managerSkList;
    private List<User> listWorkersOnSK;

    private Grid<User> grid;
    private GridListDataView<User> dataView;
    private TextField searchField;
    private PrintPreviewReport report = new PrintPreviewReport(User.class, "prcNumer", "prcNazwisko", "prcImie", "password");
    private Anchor aPdf = new Anchor( "","PDF");

    public WorkerWithPassView(UserService userService, SKService skService, ReportService reportService) {
        setHeightFull();
        hTop.setClassName("hTop");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loggedUser = userService.findByUsername(userDetails.getUsername());
        managerSkList = skService.findSkForManager(loggedUser.get().getPrcId());
        if (managerSkList.size() > 0) {
            addSelectSK();
        }


        var butGetData = new Button("Pobierz");
        butGetData.addClickListener( clickEvent -> {
            listWorkersOnSK = reportService.getWorkersListWithPassForSK(selectSK.getValue().getSkId());
            dataView = grid.setItems(listWorkersOnSK);
            var pdf = report.getStreamResource("pracownicy.pdf", () -> reportService.getWorkersListWithPassForSK(selectSK.getValue().getSkId()), PrintPreviewReport.Format.PDF);
            hTop.remove(aPdf);
            aPdf = new Anchor(pdf, "PDF");
            aPdf.setClassName("anchorPDF");
            aPdf.setTarget( "_blank" ) ;
            hTop.add(aPdf);

            if (listWorkersOnSK.size() == 0) {
                com.vaadin.flow.component.notification.Notification.show("Brak danych", 1000, Notification.Position.MIDDLE);
            } else {
                dataView.addFilter(user -> {
                    String searchTerm = searchField.getValue().trim();

                    if (searchTerm.isEmpty())
                        return true;

                    boolean matchesFullName = matchesTerm(user.getPrcNazwisko(),
                            searchTerm);
                    boolean matchesEmail = matchesTerm(user.getPrcImie(), searchTerm);
                    boolean matchesProfession = matchesTerm(user.getPrcNumer().toString(),
                            searchTerm);

                    return matchesFullName || matchesEmail || matchesProfession;
                });

            }
        });

        searchField = new TextField();
        searchField.setClassName("searchField");
        searchField.setWidth("300px");
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> dataView.refreshAll());

        hTop.add(butGetData, searchField);

        this.grid = new Grid<>(User.class);
        grid.setClassName("gridWorkers");
        grid.setColumns();
        grid.addColumn("prcNumer").setWidth("100px").setHeader("Numer");
        grid.addColumn("prcNazwisko").setWidth("100px").setHeader("Nazwisko");
        grid.addColumn("prcImie").setWidth("300px").setHeader("Imię");
        grid.addColumn("password").setWidth("300px").setHeader("Hasło");
        vTop.add(hTop, grid);

        add(vTop);
    }

    private void addSelectSK() {
        selectSK.setClassName("selectSK");
        selectSK.setItems(managerSkList);
        selectSK.setItemLabelGenerator(SK::getSkKod);
        if (managerSkList.size() > 0 )
            selectSK.setEmptySelectionCaption(managerSkList.get(0).getSkKod());
        //selectSK.setLabel("Obiekt");
        if (managerSkList.size() > 0 )
            selectSK.setValue(managerSkList.get(0));
        hTop.add(selectSK);
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return value.toLowerCase().contains(searchTerm.toLowerCase());
    }
}
