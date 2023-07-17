package pl.kskowronski.views.regulations;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.log.Log;
import pl.kskowronski.data.entity.log.LogEvent;
import pl.kskowronski.data.service.UserService;
import pl.kskowronski.data.service.log.LogService;
import pl.kskowronski.views.MainLayout;

import javax.annotation.security.RolesAllowed;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Optional;

@Route(value = "regulations", layout = MainLayout.class)
@PageTitle("Regulaminy")
@RolesAllowed({"admin","supervisor","user","manager"})
public class RegulationsView extends VerticalLayout {

    private VerticalLayout v01 = new VerticalLayout();

    private LogService logService;
    private UserService userService;
    private Optional<User> worker;

    public RegulationsView(LogService logService, UserService userService) {
        this.logService = logService;
        this.userService = userService;
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        worker = userService.findByUsername(userDetails.getUsername());
        this.add(v01);

        try {
            generateRegulations();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateRegulations() throws IOException {
        InputStream employeeReportStream = getClass().getResourceAsStream("/pdf/oswiadczenie_ppk.pdf");
        byte[] pdfBytes = employeeReportStream.readAllBytes();

        StreamResource res = new StreamResource("oswiadczenie_ppk.pdf", () -> new ByteArrayInputStream(pdfBytes));
        String reportName = "oswiadczenie ppk";
        Anchor a = new Anchor(res, " Regulamin pracowinczy");

        a.setId(reportName);
        a.setTarget( "_blank" );
        a.getElement().addEventListener("click", event -> {
            new Thread(() -> { // asynchronous
                saveLog();
            }).start();
        });

        v01.add(a, new Html("<p style='font-size: 12px; margin-top: -0.7em;'> *Pobranie dokumentu oznacza zapoznanie się z treścią.</p>") );
    }

    private void saveLog(){
        Log log = new Log();
        log.setPrcId(worker.get().getPrcId());
        log.setEvent(LogEvent.DOWNLOAD_THE_REGULATION_2023.toString());
        log.setAuditDc(new Date());
        log.setDescription("Pobranie regualminu pracowniczego 2023");
        logService.save(log);
    }

}
