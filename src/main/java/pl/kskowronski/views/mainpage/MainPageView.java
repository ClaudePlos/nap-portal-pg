package pl.kskowronski.views.mainpage;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
import pl.kskowronski.data.entity.egeria.ek.AbsenceLimitDTO;
import pl.kskowronski.data.entity.log.LogEvent;
import pl.kskowronski.data.entity.log.LogPit11;
import pl.kskowronski.data.service.UserService;
import pl.kskowronski.data.service.admin.NppAdvertisementService;
import pl.kskowronski.data.service.egeria.css.SKService;
import pl.kskowronski.data.service.egeria.ek.AbsenceLimitService;
import pl.kskowronski.data.service.egeria.ek.ZatrudnienieService;
import pl.kskowronski.data.service.egeria.global.EatFirmaService;
import pl.kskowronski.data.service.log.LogPit11Service;
import pl.kskowronski.data.service.log.LogService;
import pl.kskowronski.views.MainLayout;
import pl.kskowronski.views.regulations.RegulationsView;

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
    private AbsenceLimitService absenceLimitService;

    private Optional<User> worker;

    private Board board = new Board();
    private Row rootRow = new Row();
    private Row nestedRow = new Row();

    @Autowired
    public MainPageView(UserService userService, NppAdvertisementService nppAdvertisementService, LogPit11Service logPit11Service, AbsenceLimitService absenceLimitService
            , LogService logService, ZatrudnienieService zatrudnienieService, SKService skService, EatFirmaService eatFirmaService) {
        this.logPit11Service = logPit11Service;
        this.absenceLimitService = absenceLimitService;
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        worker = userService.findByUsername(userDetails.getUsername());

        List<NppAdvertisement> adverts = nppAdvertisementService.findAll();



        v01.add(new Html("<p style='font-size:24px;'><b>Witaj, " + worker.get().getPrcImie()  + " \uD83D\uDC4B</b></p>")); //+ " " + worker.get().getPrcNazwisko() + ". "
        v01.add(new Label("To jest strona przeznaczona dla Ciebie z dostępem do Twoich danych kadrowych."));
        v01.add(new Label(""), new Html("<b>Ogłoszenia:</b>"));

        RegulationsView reg = new RegulationsView(logService, userService, zatrudnienieService, skService, eatFirmaService);
        v01.add(reg);

        v01.add(new Label(""),new Label(""),new Label(""));

        adverts.stream().forEach( item -> {
            v01.add(new Label(item.getText()), new Html("<BR>"));
        });

        rootRow.add(v01, 2);

        Span spanMonthName = new Span( mappingMnthsToPL(LocalDate.now().getMonthValue()) );
        spanMonthName.getElement().getThemeList().add("badge primary");
        spanMonthName.getStyle().set("width","25px");
        spanMonthName.getStyle().set("height","40px");
        spanMonthName.getStyle().set("font-size","22px");
        Span spanMonthNum = new Span(LocalDate.now().getDayOfMonth() + "");
        spanMonthNum.getElement().getThemeList().add("badge contrast");
        spanMonthNum.getStyle().set("width","25px");
        spanMonthNum.getStyle().set("height","150px");
        spanMonthNum.getStyle().set("font-size","64px");



        nestedRow.add(spanMonthName);
        nestedRow.add(spanMonthNum);
        rootRow.addNestedRow(nestedRow);



        board.add(rootRow);

        add(board);
        this.setClassName("board-nested");

        try {
            PrintStatement();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            printInformationAboutLimitFreeDayAtYear();
        } catch (Exception e) {
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

        v01.add(a, new Html("<p style='font-size: 12px; margin-top: -0.7em;'> *Pobranie dokumentu oznacza zapoznanie się z treścią.</p>") );


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

    private void printInformationAboutLimitFreeDayAtYear() throws Exception {
        Optional<List<AbsenceLimitDTO>> listAbsencesLimits
                = absenceLimitService.findAllAbsenceLimitForPrcIdAndYear(worker.get().getPrcId()
                , LocalDate.now().getYear() + ""
                , "'A_UR1'");

        if (listAbsencesLimits.get().size() > 0 ) {
            Span limitFreeDays = new Span(new Html("<center><p>" + listAbsencesLimits.get().get(0).getPozostaloUrlopu() + " dni <br>" +
                    " <small> Urlopu wypoczynkowego do wykorzystania</small></p></center>"));
            limitFreeDays.getElement().getThemeList().add("badge contrast");
            limitFreeDays.getStyle().set("width","25px");
            limitFreeDays.getStyle().set("height","150px");
            limitFreeDays.getStyle().set("font-size","32px");
            limitFreeDays.getStyle().set("margin-top","15px");
            nestedRow.add(limitFreeDays);
        }
    }

    private String mappingMnthsToPL( int value ) {
        if ( value == 1 )
            return "Styczeń";
        if ( value == 2 )
            return "Luty";
        if ( value == 3 )
            return "Marzec";
        if ( value == 4 )
            return "Kwiecień";
        if ( value == 5 )
            return "Maj";
        if ( value == 6 )
            return "Czerwiec";
        if ( value == 7 )
            return "Lipiec";
        if ( value == 8 )
            return "Sierpień";
        if ( value == 9 )
            return "Wrzesień";
        if ( value == 10 )
            return "Październik";
        if ( value == 11 )
            return "Listopad";
        else
            return "Grudzień";
    }

}
