package pl.kskowronski.views.mainpage;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import pl.kskowronski.data.entity.admin.NppAdvertisement;
import pl.kskowronski.data.entity.admin.NppSkForSupervisor;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.service.UserService;
import pl.kskowronski.data.service.admin.NppAdvertisementService;
import pl.kskowronski.views.MainLayout;

import java.util.List;
import java.util.Optional;

@PageTitle("Strona główna")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed({"admin","supervisor","user","manager"})
public class MainPageView extends VerticalLayout {

    private VerticalLayout v01 = new VerticalLayout();

    @Autowired
    public MainPageView(UserService userService, NppAdvertisementService nppAdvertisementService) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> worker = userService.findByUsername(userDetails.getUsername());

        List<NppAdvertisement> adverts = nppAdvertisementService.findAll();

        add(new Label("Witaj " + worker.get().getPrcImie() + " " + worker.get().getPrcNazwisko() + ". " ));
        add(new Label("To jest strona przeznaczona dla Ciebie z dostępem do Twoich danych kadrowych."));
        v01.add(new Label(""), new Html("<b>Ogłoszenia:</b>"));
        adverts.stream().forEach( item -> {
            v01.add(new Label(item.getText()), new Html("<BR>"));
        });

        add(v01);

    }

}
