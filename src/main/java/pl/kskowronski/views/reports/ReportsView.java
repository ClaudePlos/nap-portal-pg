package pl.kskowronski.views.reports;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.kskowronski.data.service.UserService;
import pl.kskowronski.data.service.egeria.css.SKService;
import pl.kskowronski.data.service.reports.ReportService;
import pl.kskowronski.views.MainLayout;
import pl.kskowronski.views.reports.list.WorkerWithPassView;

import javax.annotation.security.RolesAllowed;

@Route(value = "workers-and-pass", layout = MainLayout.class)
@PageTitle("SkForSupervisorView")
@RolesAllowed({"admin","manager"})
public class ReportsView extends Div {

    private String rep01 = "1. Wykaz pracowników z hasłami";
    private String rep02 = "2. .....";
    private String rep03 = "3. .....";


    public ReportsView(UserService userService, SKService skService, ReportService reportService) {
        HorizontalLayout h01 = new HorizontalLayout();
        ComboBox<String> comboRep = new ComboBox<>();
        comboRep.setItems(rep01,rep02,rep03);
        comboRep.setWidth("500px");
        comboRep.setLabel("Raporty");

        comboRep.addValueChangeListener( event -> {
            h01.removeAll();
            if (event.getValue().equals(rep01)) {
                WorkerWithPassView rep01View = new WorkerWithPassView(userService, skService, reportService);
                h01.add(rep01View);
            }


        });

        add( comboRep, h01);
    }
}
