package pl.kskowronski.views.documents;

import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.vaadin.reports.PrintPreviewReport;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.egeria.ek.Zatrudnienie;
import pl.kskowronski.data.entity.egeria.global.EatFirma;
import pl.kskowronski.data.service.egeria.ek.EkPlanyPracyService;
import pl.kskowronski.data.service.egeria.ek.EkSystemyPracyService;
import pl.kskowronski.data.service.egeria.ek.ZatrudnienieService;
import pl.kskowronski.data.service.egeria.global.EatFirmaService;
import pl.kskowronski.views.MainLayout;

import javax.annotation.security.RolesAllowed;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Route(value = "documents", layout = MainLayout.class)
@PageTitle("Twoje dokumenty")
@RolesAllowed({"admin","supervisor","user","manager"})
public class DocumentsView extends VerticalLayout {

    private ZatrudnienieService zatrudnienieService;
    private EatFirmaService eatFirmaService;
    private EkSystemyPracyService ekSystemyPracyService;
    private EkPlanyPracyService ekPlanyPracyService;
    User worker;
    private Grid<Zatrudnienie> gridContracts;

    Style headerStyle = new StyleBuilder(true).setFont(Font.ARIAL_MEDIUM).build();
    Style groupStyle = new StyleBuilder(true).setFont(Font.ARIAL_MEDIUM_BOLD).build();

    public DocumentsView(ZatrudnienieService zatrudnienieService, EatFirmaService eatFirmaService, EkSystemyPracyService ekSystemyPracyService, EkPlanyPracyService ekPlanyPracyService) throws ParseException {
        this.zatrudnienieService = zatrudnienieService;
        this.eatFirmaService = eatFirmaService;
        this.ekSystemyPracyService = ekSystemyPracyService;
        this.ekPlanyPracyService = ekPlanyPracyService;
        generateGridContract();
        getCotractForPeriod();
    }

    private void generateGridContract() {
        this.gridContracts = new Grid<>(Zatrudnienie.class);
        gridContracts.setHeight("800px");
        gridContracts.setClassName("gridContracts");
        gridContracts.setColumns();
        gridContracts.addColumn(TemplateRenderer.<Zatrudnienie> of(
                "<div class=\"gridFirma\">[[item.firma]]</div>")
                .withProperty("firma", Zatrudnienie::getFrmNazwa))
                .setHeader("Firma");
        gridContracts.addColumn("zatDataPrzyj").setHeader("Data przyjęcia");
        gridContracts.addColumn("zatDataZmiany").setHeader("Data zmiany");
        gridContracts.addColumn("zatDataDo").setHeader("Data do");
        gridContracts.addComponentColumn(item -> createButtonDocument(item)).setHeader("");
        add(gridContracts);
    }

    private void getCotractForPeriod() throws ParseException {
        VaadinSession session = VaadinSession.getCurrent();
        worker = session.getAttribute(User.class);
        Optional<List<Zatrudnienie>> contracts = zatrudnienieService.getAllContractsForWorker(worker.getPrcId());
        if (!contracts.isPresent()){
            Notification.show("Brak umów w danym okresie", 3000, Notification.Position.MIDDLE);
        }
        gridContracts.setItems(contracts.get());
    }


    private Button createButtonDocument(Zatrudnienie zat) {
        Button button = new Button("Dokument", clickEvent -> {
            genPDF(zat);
        });
        return button;
    }

    private void genPDF(Zatrudnienie zat){

        Optional<EatFirma> company = eatFirmaService.findById(zat.getFrmId());

        Double normaDobowa = ekSystemyPracyService.getNormaDobowaForSpId( ekPlanyPracyService.getSpId(zat.getPpId()) );
        Double tygodniowaNorma = ekSystemyPracyService.getNormaTygodniowaForSpId( ekPlanyPracyService.getSpId(zat.getPpId()) );
        String npKod = zat.getZatStatus().toString().substring(zat.getZatStatus().toString().length()-1);

        String przerwaPracaNP1 = "";
        String przerwaPracaNP2 = "";
        String dodUrlopNP1 = "";
        String dodUrlopNP2 = "";
        if ( Integer.parseInt(npKod) > 0 ) {
            przerwaPracaNP1 = "  - dodatkowa przerwa z tytułu posiadania orzeczenia o stopniu niepełnosprawności: 15 min (dla osób ";
            przerwaPracaNP2 = "niepełnosprawnych po kodzie ubezpieczenia z umowy)";
            dodUrlopNP1 = "Wymiar przysługującego Ci urlopu dodatkowego: ……………………(tylko dla NP. po kodzie ubezpieczenia)";
            dodUrlopNP2 = "Ilość dni do wykorzystania w 20… roku: …………………";
        }

        List<User> list = new ArrayList<>();
        list.add(worker);

        Dialog dialog = new Dialog();
        dialog.setWidth("700px");
        dialog.setHeight("950px");

        var butClose = new Button("X", e -> dialog.close());
        butClose.setClassName("butClosePrintView");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String zatDataDo =  zat.getZatDataDo() == null ? " czas nieokreślony" : zat.getZatDataDo().toString();

        var report = new PrintPreviewReport<>();
        report.getReportBuilder().setMargins(20, 3, 40, 40)
                .setTitle("")
                .addAutoText( worker.getNazwImie() + " " + npKod, AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 200, headerStyle)
                .addAutoText("Data: " + LocalDateTime.now().format(formatter), AutoText.POSITION_HEADER, AutoText.ALIGNMENT_RIGHT, 200, headerStyle)
                //.addAutoText(AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_HEADER, AutoText.ALIGNMENT_RIGHT, 200, 10, headerStyle)

                .addAutoText("", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)
                .addAutoText("", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)
                .addAutoText("Jesteś zatrudniony w ramach zawartej umowy o pracę w firmie : " + company.get().getFrmNazwa(), AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)
                .addAutoText("na okres od " + zat.getZatDataPrzyj() + " do " + zatDataDo, AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)
                .addAutoText("", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)
                .addAutoText("Wymiar etatu: " + zat.getWymiarEtatu().getOpis(), AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)

                .addAutoText("", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)
                .addAutoText("", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)
                .addAutoText("Obowiązuje Panią/Pana : " + normaDobowa +" godzinna dobowa norma czasu pracy i przeciętnie " + tygodniowaNorma + " godzinna", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)

                .addAutoText(" tygodniowa norma czasu pracy w przeciętnie 5- dniowym tygodniu pracy w okresie rozliczeniowym. ", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)
                .addAutoText("Jeżeli jest to uzasadnione rodzajem pracy lub jej organizacją mogą być stosowane rozkłady czasu pracy, ", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)
                .addAutoText("których dopuszczalne jest przedłużenie wymiaru czasu pracy do 12 godzin na dobę/ 72 godzin tygodniowo", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)

                .addAutoText("", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)
                .addAutoText("Przysługują Panu/i następujące przerwy w pracy:", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)
                .addAutoText("  - na posiłek w wymiarze 15 minut", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)
                .addAutoText("  - dodatkowa przerwa z tytułu wydłużenia dobowego wymiaru czasu pracy powyżej 9 godzin: 15 min", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)


                .addAutoText(przerwaPracaNP1, AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)
                .addAutoText(przerwaPracaNP2, AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)



                .addAutoText("", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)
                .addAutoText("", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)
                .addAutoText("Wymiar przysługującego Ci urlopu wypoczynkowego: …………………………… ( z zakładki okresy)", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)
                .addAutoText("Ilość dni do wykorzystania w 20… roku: …………", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)


                .addAutoText("", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)
                .addAutoText(dodUrlopNP1, AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)
                .addAutoText(dodUrlopNP2, AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 500, headerStyle)

                .setPrintBackgroundOnOddRows(true);



        report.setItems(null);

        //report.getReportBuilder().addAutoText("Jesteś zatrudniony w ramach", AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 200, headerStyle);

        dialog.add( butClose, report );

        dialog.open();

    }
}
