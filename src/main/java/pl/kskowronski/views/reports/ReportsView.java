package pl.kskowronski.views.reports;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.kskowronski.data.service.UserService;
import pl.kskowronski.data.service.egeria.css.SKService;
import pl.kskowronski.data.service.log.LogService;
import pl.kskowronski.data.service.reports.ReportService;
import pl.kskowronski.views.MainLayout;
import pl.kskowronski.views.reports.list.WorkerWithPassView;
import pl.kskowronski.views.reports.list.WorkersRegulationWhoGotView;

import javax.annotation.security.RolesAllowed;

@Route(value = "workers-and-pass", layout = MainLayout.class)
@PageTitle("SkForSupervisorView")
@RolesAllowed({"admin","manager"})
public class ReportsView extends VerticalLayout {

    private String rep01 = "01. Wykaz pracowników z hasłami";
    private String rep02 = "02. Regulamin pracowniczy, kto pobrał";
    private String rep03 = "03. .....";


    public ReportsView(UserService userService, SKService skService, ReportService reportService, LogService logService) {
        setHeight("85%");
        HorizontalLayout h01 = new HorizontalLayout();
        ComboBox<String> comboRep = new ComboBox<>();
        comboRep.setClassName("comboRep");
        comboRep.setItems(rep01,rep02,rep03);
        comboRep.setWidth("500px");
        comboRep.setLabel("Raporty");

        comboRep.addValueChangeListener( event -> {
            h01.removeAll();
            if (event.getValue().equals(rep01)) {
                WorkerWithPassView rep01View = new WorkerWithPassView(userService, skService, reportService);
                h01.add(rep01View);
            } else if (event.getValue().equals(rep02)) {
                WorkersRegulationWhoGotView rep02View = new WorkersRegulationWhoGotView(logService, userService);
                h01.add(rep02View);
            }


        });

        add( comboRep, h01);
    }
}
