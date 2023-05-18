package pl.kskowronski.views.api;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import pl.kskowronski.views.MainLayout;

@PageTitle("document")
@Route(value = "document", layout = MainLayout.class)
@AnonymousAllowed
public class ApiView extends VerticalLayout {

    public ApiView() {

        add(new Label("Api"));

    }
}
