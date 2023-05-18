package pl.kskowronski.views.documents;


import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.kskowronski.views.MainLayout;

import javax.annotation.security.RolesAllowed;

@Route(value = "documents", layout = MainLayout.class)
@PageTitle("Twoje dokumenty")
@RolesAllowed({"admin","supervisor","user","manager"})
public class DocumentsView extends VerticalLayout {

    public DocumentsView() {


        add( new Label("sdfsdfsdf"));

    }
}
