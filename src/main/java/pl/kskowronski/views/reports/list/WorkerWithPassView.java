package pl.kskowronski.views.reports.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.egeria.css.SK;
import pl.kskowronski.data.entity.egeria.eDek.EdktDeklaracjeDTO;
import pl.kskowronski.data.service.UserService;
import pl.kskowronski.data.service.egeria.css.SKService;
import pl.kskowronski.data.service.reports.ReportService;

import java.util.List;
import java.util.Optional;

public class WorkerWithPassView extends HorizontalLayout {

    private Optional<User> loggedUser;
    private Select<SK> selectSK = new Select<>();
    private HorizontalLayout hTop = new HorizontalLayout();
    private List<SK> managerSkList;

    private Grid<User> grid;


    public WorkerWithPassView(UserService userService, SKService skService, ReportService reportService) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loggedUser = userService.findByUsername(userDetails.getUsername());
        managerSkList = skService.findSkForManager(loggedUser.get().getPrcId());
        if (managerSkList.size() > 0) {
            addSelectSK();
        }
        var butGetData = new Button("Pobierz");
        butGetData.addClickListener( clickEvent -> {
            var listWorkersOnSK = reportService.getWorkersListWithPassForSK(selectSK.getValue().getSkKod());
        });
        hTop.add(butGetData);

        this.grid = new Grid<>(User.class);
        grid.setColumns("prcNumer", "prcNazwisko", "prcImie", "password"); //, "dklXmlVisual"


        add(hTop, grid);
    }

    private void addSelectSK() {
        selectSK.setClassName("selectSK");
        selectSK.setItems(managerSkList);
        selectSK.setItemLabelGenerator(SK::getSkKod);
        if (managerSkList.size() > 0 )
            selectSK.setEmptySelectionCaption(managerSkList.get(0).getSkKod());
        selectSK.setLabel("Obiekt");
        if (managerSkList.size() > 0 )
            selectSK.setValue(managerSkList.get(0));
        hTop.add(selectSK);
    }
}
