package pl.kskowronski.views.login;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay {
    public LoginView() {
        setAction("login");

        Image titleComponent = new Image("icons/icon-32x32b.png", "alt text");
        titleComponent.setClassName("titleComponent");
        this.setTitle(titleComponent);
        this.addForgotPasswordListener(e->{
            Notification.show("Forgot password not yet handled", 30,
                    Notification.Position.TOP_CENTER);
        });

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setDescription("Rekeep Portal");
        i18n.getForm().setSubmit("Zaloguj");
        i18n.getForm().setUsername("Login");
        i18n.getForm().setPassword("Hasło");
        i18n.getForm().setForgotPassword("");
        i18n.getForm().setTitle("");
        i18n.getErrorMessage().setTitle("Niepoprawna nazwa użytkownika lub hasło");
        i18n.getErrorMessage().setMessage("Sprawdź, czy podałeś poprawną nazwę użytkownika i hasło, i spróbuj ponownie.");
        i18n.setAdditionalInformation("v.2022");
        setI18n(i18n);


        setForgotPasswordButtonVisible(false);
        setOpened(true);

    }

}
