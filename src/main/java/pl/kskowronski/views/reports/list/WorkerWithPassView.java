package pl.kskowronski.views.reports.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.egeria.css.SK;
import pl.kskowronski.data.service.UserService;
import pl.kskowronski.data.service.egeria.css.SKService;

import java.util.List;
import java.util.Optional;

public class WorkerWithPassView extends HorizontalLayout {

    private Optional<User> loggedUser;
    private Select<SK> selectSK = new Select<>();
    private HorizontalLayout hTop = new HorizontalLayout();
    private List<SK> managerSkList;


    public WorkerWithPassView(UserService userService, SKService skService) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loggedUser = userService.findByUsername(userDetails.getUsername());
        managerSkList = skService.findSkForManager(loggedUser.get().getPrcId());
        if (managerSkList.size() > 0) {
            addSelectSK();
        }
        var butGetData = new Button("Pobierz");
        butGetData.addClickListener( clickEvent -> {

        });
        hTop.add(butGetData);
        add(hTop);
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
