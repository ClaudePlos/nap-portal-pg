package pl.kskowronski.views.map;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.RolesAllowed;


import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;
import pl.kskowronski.components.leafletmap.LeafletMap;
import pl.kskowronski.views.MainLayout;

@PageTitle("Map")
@Route(value = "map", layout = MainLayout.class)
@RolesAllowed({"admin"})
public class MapView extends VerticalLayout {

    private LeafletMap map = new LeafletMap();
    private Button butCheck = new Button();

    public MapView() {
        setSizeFull();
        setPadding(false);
        map.setSizeFull();
        map.setView(55.0, 10.0, 4);
        add(map, butCheck);
        butCheck.addClickListener( i -> {
            if (isMobileDevice()){
                Notification notification = Notification.show("Mobile!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } else {
                Notification notification = Notification.show("PC!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }

        });
    }

    public  boolean isMobileDevice() {
        WebBrowser webBrowser = VaadinSession.getCurrent().getBrowser();
        return webBrowser.isAndroid() || webBrowser.isIPhone() || webBrowser.isWindowsPhone();
    }
}
