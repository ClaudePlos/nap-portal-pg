package pl.kskowronski.views.logout;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kskowronski.security.AuthenticatedUser;
import pl.kskowronski.views.MainLayout;

import javax.annotation.security.RolesAllowed;

@PageTitle("Logout")
@Route(value = "logout", layout = MainLayout.class)
@RolesAllowed({"admin","supervisor","user"})
public class Logout extends VerticalLayout {

    private AuthenticatedUser authenticatedUser;

    @Autowired
    public Logout(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        authenticatedUser.logout();
    }
}
