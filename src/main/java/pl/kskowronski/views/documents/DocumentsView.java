package pl.kskowronski.views.documents;


import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.egeria.ek.Zatrudnienie;
import pl.kskowronski.data.service.egeria.ek.ZatrudnienieService;
import pl.kskowronski.views.MainLayout;

import javax.annotation.security.RolesAllowed;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Route(value = "documents", layout = MainLayout.class)
@PageTitle("Twoje dokumenty")
@RolesAllowed({"admin","supervisor","user","manager"})
public class DocumentsView extends VerticalLayout {

    private ZatrudnienieService zatrudnienieService;

    private Grid<Zatrudnienie> gridContracts;

    public DocumentsView(ZatrudnienieService zatrudnienieService) throws ParseException {
        this.zatrudnienieService = zatrudnienieService;
        generateGridContract();
        getCotractForPeriod();
    }

    private void generateGridContract() {
        this.gridContracts = new Grid<>(Zatrudnienie.class);
        gridContracts.setHeight("800px");
        gridContracts.setClassName("gridContracts");
        gridContracts.setColumns();
        gridContracts.addColumn(TemplateRenderer.<Zatrudnienie> of(
                "<div class=\"gridFirma\">[[item.firma]]</div>")
                .withProperty("firma", Zatrudnienie::getFrmNazwa))
                .setHeader("Firma");
        gridContracts.addColumn("zatDataPrzyj").setHeader("Data przyjęcia");
        gridContracts.addColumn("zatDataZmiany").setHeader("Data zmiany");
        gridContracts.addColumn("zatDataDo").setHeader("Data do");
        add(gridContracts);
    }

    private void getCotractForPeriod() throws ParseException {
        VaadinSession session = VaadinSession.getCurrent();
        User worker = session.getAttribute(User.class);
        Optional<List<Zatrudnienie>> contracts = zatrudnienieService.getAllContractsForWorker(worker.getPrcId());
        if (!contracts.isPresent()){
            Notification.show("Brak umów w danym okresie", 3000, Notification.Position.MIDDLE);
        }
        gridContracts.setItems(contracts.get());
    }
}
