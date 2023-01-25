package pl.kskowronski.views.mainpage;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import pl.kskowronski.data.entity.admin.NppAdvertisement;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.log.LogEvent;
import pl.kskowronski.data.entity.log.LogPit11;
import pl.kskowronski.data.service.UserService;
import pl.kskowronski.data.service.admin.NppAdvertisementService;
import pl.kskowronski.data.service.log.LogPit11Service;
import pl.kskowronski.views.MainLayout;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@PageTitle("Strona główna")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed({"admin","supervisor","user","manager"})
public class MainPageView extends VerticalLayout {

    private VerticalLayout v01 = new VerticalLayout();

    private LogPit11Service logPit11Service;

    private Optional<User> worker;

    @Autowired
    public MainPageView(UserService userService, NppAdvertisementService nppAdvertisementService, LogPit11Service logPit11Service) {
        this.logPit11Service = logPit11Service;
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        worker = userService.findByUsername(userDetails.getUsername());

        List<NppAdvertisement> adverts = nppAdvertisementService.findAll();

        add(new Label("Witaj " + worker.get().getPrcImie() + " " + worker.get().getPrcNazwisko() + ". " ));
        add(new Label("To jest strona przeznaczona dla Ciebie z dostępem do Twoich danych kadrowych."));
        v01.add(new Label(""), new Html("<b>Ogłoszenia:</b>"));




        adverts.stream().forEach( item -> {
            v01.add(new Label(item.getText()), new Html("<BR>"));
        });

        add(v01);

        try {
            PrintStatement();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void PrintStatement() throws IOException {

        InputStream employeeReportStream = getClass().getResourceAsStream("/pdf/oswiadczenie_ppk.pdf");
        byte[] pdfBytes = employeeReportStream.readAllBytes();

        StreamResource res = new StreamResource("oswiadczenie_ppk.pdf", () -> new ByteArrayInputStream(pdfBytes));
        String reportName = "oswiadczenie ppk";
        Anchor a = new Anchor(res, " Oświadcznie PPK");

        a.setId(reportName);
        a.setTarget( "_blank" );
        a.getElement().addEventListener("click", event -> {
            new Thread(() -> { // asynchronous
                saveLog();
            }).start();
        });

        add(a, new Html("<p style='font-size: 12px; margin-top: -0.7em;'> *Pobranie dokumentu oznacza zapoznanie się z treścią.</p>") );


    }

    private void saveLog(){
        LogPit11 logPit11 = new LogPit11();
        logPit11.setPrcId(worker.get().getPrcId());
        logPit11.setEvent(LogEvent.DOWNLOAD_THE_PKK_STATEMENT.toString());
        logPit11.setYear(LocalDate.now().getYear());
        logPit11.setAuditDc(new Date());
        logPit11.setDescription("Pobranie oświadczenia PPK");
        logPit11Service.save(logPit11);
    }

}
