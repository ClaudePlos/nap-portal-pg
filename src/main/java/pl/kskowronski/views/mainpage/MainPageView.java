package pl.kskowronski.views.mainpage;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.service.UserService;
import pl.kskowronski.views.MainLayout;

import java.util.Optional;

@PageTitle("Strona główna")
@Route(value = "hello", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
//@RolesAllowed("user")
public class MainPageView extends HorizontalLayout {

    private UserService userService;

    @Autowired
    public MainPageView(UserService userService) {
        this.userService = userService;

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> worker = userService.findByUsername(userDetails.getUsername());

        add(new Label("Witaj " + worker.get().getPrcImie() + " " + worker.get().getPrcNazwisko() + ". " ));
        add(new Label("To jest strona przeznaczona dla Ciebie z dostępem do Twoich danych kadrowych."));

    }

}
