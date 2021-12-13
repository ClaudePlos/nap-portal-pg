package pl.kskowronski.views.login;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay {
    public LoginView() {
        setAction("login");

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Rekeep");
        i18n.getHeader().setDescription("Portal v.2022");
        i18n.getForm().setSubmit("Zaloguj");
        i18n.getForm().setPassword("Hasło");
        i18n.getErrorMessage().setTitle("Niepoprawna nazwa użytkownika lub hasło");
        i18n.getErrorMessage().setMessage("Sprawdź, czy podałeś poprawną nazwę użytkownika i hasło, i spróbuj ponownie.");
        i18n.setAdditionalInformation(null);
        setI18n(i18n);

        setForgotPasswordButtonVisible(false);
        setOpened(true);

    }

}
