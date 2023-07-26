package pl.kskowronski.views.regulations;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
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
import pl.kskowronski.data.service.egeria.css.SKService;
import pl.kskowronski.data.service.egeria.ek.ZatrudnienieService;
import pl.kskowronski.data.service.egeria.global.EatFirmaService;
import pl.kskowronski.data.service.log.LogService;
import pl.kskowronski.views.MainLayout;

import javax.annotation.security.RolesAllowed;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@Route(value = "regulations", layout = MainLayout.class)
@PageTitle("Regulaminy")
@RolesAllowed({"admin","supervisor","user","manager"})
public class RegulationsView extends VerticalLayout {

    private VerticalLayout v01 = new VerticalLayout();

    private LogService logService;
    private Optional<User> worker;

    DateTimeFormatter formYYYYMM = DateTimeFormatter.ofPattern("yyyy-MM");

    public RegulationsView(LogService logService, UserService userService, ZatrudnienieService zatrudnienieService, SKService skService, EatFirmaService eatFirmaService) {
        this.logService = logService;
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        worker = userService.findByUsername(userDetails.getUsername());
        if (worker.isPresent()) {
            try {
                worker.get().setZatrudnienia(zatrudnienieService.getActualContractForWorker(worker.get().getPrcId(), LocalDate.now().format(formYYYYMM)).get());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        worker.get().getZatrudnienia().stream().forEach( z -> {
            try {
                    this.add(new Html("<b style=\"color:red;font-size:20px;\"> Uwaga!!! Nowy regulamin pracy, zapoznaj się:</b>"), v01);
                    String fileName = getPathForRegulation(z.getFrmId());
                    String path = "/pdf/regulations2023/" + fileName;
                    generateRegulations(path, fileName, skService.findSkKodForSkId(z.getZatSkId()), eatFirmaService.findById(z.getFrmId()).get().getFrmNazwa() );
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private String getPathForRegulation( Integer frmId ) {
        String fileName = "";
        if ( frmId == 300201 ) {
            fileName = "regulamin N. Service.pdf";
        } else if ( frmId == 300000 ) {
            fileName = "regulamin IZAN+.pdf";
        } else if ( frmId == 300313 ) {
            fileName = "regulamin JOL-MARK.pdf";
        } else if ( frmId == 300322 ) {
            fileName = "regulamin N. Catering.pdf";
        } else if ( frmId == 300203 ) {
            fileName = "regulamin N. Hospital.pdf";
        } else if ( frmId == 300304 ) {
            fileName = "regulamin N. Inwestycje.pdf";
        } else if ( frmId == 300305 ) {
            fileName = "regulamin N. Marketing.pdf";
        } else if ( frmId == 300315 ) {
            fileName = "regulamin Rekeep FM.pdf";
        } else if ( frmId == 300317 ) {
            fileName = "regulamin Triomed.pdf";
        } else if ( frmId == 300319 ) {
            fileName = "CATERMED_REGULAMIN_PRACY.pdf";
        } else if ( frmId == 300170 ) {
            fileName = "Regulamin Rekeep Polska.pdf";
        }
        else {
            fileName = "Brak dostepu.pdf";
        }
        return fileName;
    }

    private void generateRegulations(String path, String fileName, String sk, String company) throws IOException {

        InputStream employeeReportStream = getClass().getResourceAsStream(path);
        byte[] pdfBytes = employeeReportStream.readAllBytes();

        StreamResource res = new StreamResource(fileName, () -> new ByteArrayInputStream(pdfBytes));
        String reportName = fileName;
        Anchor a = new Anchor(res, fileName);

        a.setId(reportName);
        a.setTarget( "_blank" );
        a.getElement().addEventListener("click", event -> {
            new Thread(() -> { // asynchronous
                saveLog(fileName, sk, company);
            }).start();
        });

        v01.add(a, new Html("<p style='font-size: 12px; margin-top: -0.7em;'> *Pobranie dokumentu oznacza zapoznanie się z treścią.</p>") );
    }

    private void saveLog( String desc, String sk, String company){
        Log log = new Log();
        log.setPrcId(worker.get().getPrcId());
        log.setEvent(LogEvent.DOWNLOAD_THE_REGULATION_2023.toString());
        log.setAuditDc(new Date());
        log.setDescription("Pobranie " + desc);
        log.setSk(sk);
        log.setCompany(company);
        logService.save(log);
    }

}
