package pl.kskowronski.views.advancepayment;


import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import pl.kskowronski.data.MapperDate;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.egeria.AdvancePaymentDTO;
import pl.kskowronski.data.entity.egeria.eDek.EdktDeklaracjeDTO;
import pl.kskowronski.data.service.AdvancePaymentService;
import pl.kskowronski.data.service.UserService;
import pl.kskowronski.views.MainLayout;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;

@Route(value = "advance-payment", layout = MainLayout.class)
@PageTitle("Twoje zaliczki")
@RolesAllowed({"EK04"})
public class AdvancePaymentView extends VerticalLayout {

    private Optional<User> loggedUser;
    private AdvancePaymentService advancePaymentService;
    private MapperDate mapperDate = new MapperDate();

    private Grid<AdvancePaymentDTO> grid;
    private NumberField yearField = new NumberField();
    private String whetherClearing = "N";
    Checkbox checkClearing = new Checkbox();


    public AdvancePaymentView(UserService userService, AdvancePaymentService advancePaymentService) {
        this.advancePaymentService = advancePaymentService;
        setHeight("98%");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loggedUser = userService.findByUsername(userDetails.getUsername());
        checkClearing.setLabel("Rozliczone?");
        checkClearing.setClassName("checkClearingAdvancePayment");
        checkClearing.addValueChangeListener(event -> {
            if (event.getValue() == true) {
                whetherClearing = "T";
            } else {
                whetherClearing = "N";
            }
            onUserChangedYear();
        });

        yearField.setLabel("Rok");
        yearField.setHasControls(true);
        yearField.setValue(Double.parseDouble(Long.parseLong(mapperDate.getCurrentlyYear()) + ""));
        yearField.addValueChangeListener( e-> {
            try {
                onUserChangedYear();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        this.grid = new Grid<>(AdvancePaymentDTO.class);
        grid.setClassName("gridAdvancePayment");
        grid.setColumns();
        grid.addColumn("frmName").setWidth("200px").setHeader("Firma").setAutoWidth(true);
        grid.addColumn("creationDate").setWidth("100px").setHeader("Data utworzenia").setAutoWidth(true);
        grid.addColumn("ctAmount").setWidth("150px").setHeader("Kwota").setAutoWidth(true);
        grid.addColumn(TemplateRenderer.<AdvancePaymentDTO> of(
                "<div title='[[item.desc]]'>[[item.desc]]</div>")
                .withProperty("desc", AdvancePaymentDTO::getDescription))
                .setHeader("Opis").setWidth("400px");
        grid.addColumn("clearingDate").setWidth("100px").setHeader("Data rozliczenia").setAutoWidth(true);
        grid.addColumn("whetherClearing").setWidth("100px").setHeader("Czy rozliczone?").setAutoWidth(true);
        grid.setWidth("100%");
        grid.setHeightFull();

        HorizontalLayout h01 = new HorizontalLayout();
        h01.add(yearField, checkClearing);
        h01.setClassName("h1AdvancePayment");
        add(h01, grid);
        onUserChangedYear();
    }

    private void onUserChangedYear(){
        List<AdvancePaymentDTO> advancePayments = advancePaymentService.getAdvancePaymentForUser(
                loggedUser.get().getPrcNumer(), String.valueOf(yearField.getValue().intValue()), whetherClearing);
        grid.setItems(advancePayments);
        if (advancePayments.size() == 0) {
            com.vaadin.flow.component.notification.Notification.show("Brak nierozliczonych zaliczek", 5000, Notification.Position.MIDDLE);
        }
    }
}
