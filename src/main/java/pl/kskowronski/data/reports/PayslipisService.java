package pl.kskowronski.data.reports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.egeria.ckk.Client;
import pl.kskowronski.data.entity.egeria.ek.*;
import pl.kskowronski.data.entity.egeria.global.EatFirma;
import pl.kskowronski.data.service.egeria.ckk.ClientService;
import pl.kskowronski.data.service.egeria.ek.EkDefGroupRepo;
import pl.kskowronski.data.service.egeria.ek.EkGroupCodeRepo;
import pl.kskowronski.data.service.egeria.ek.SkladnikService;
import pl.kskowronski.data.service.egeria.ek.ZatrudnienieService;
import pl.kskowronski.data.service.egeria.global.EatFirmaService;


@Service
public class PayslipisService {

    @Autowired
    private ClientService clientService;

    @Autowired
    private EkDefGroupRepo ekDefGroupRepo;

    @Autowired
    private EkGroupCodeRepo ekGroupCodeRepo;

    @Autowired
    private ZatrudnienieService zatrudnienieService;

    @Autowired
    private EatFirmaService eatFirmaService;

    @Autowired
    SkladnikService skladnikService;

    public String PATH = "C:\\tmp\\"; //change it

    public String przygotujPaski(Long skId, Integer prcId, String okres, Integer frmId, Long typeContract) throws IOException {
        ClassLoader cl = this.getClass().getClassLoader();
        URL url =  cl.getResource("pit11/v26/pit11_26.jrxml");
        String absolutePath = url.getPath() + "\\";

        if (!absolutePath.toUpperCase().substring(1,3).equals("C:")){ //todo better check system operation
            PATH = "/home/szeryf/kskowronski_projects/nap_emp_portal/pit11_pdf/";
        }

        String path = "";
        path = generujPasek("paski_prac", skId, prcId, okres, frmId, typeContract);
        return path;
    }


    private String generujPasek(String raportNazwa, Long skId, Integer prcId, String okres, Integer frmId, Long typeContract ) throws IOException {

        String fileName = prcId + "_" + typeContract + ".pdf";
        String path = PATH + fileName;
        File file = new File(PATH + fileName);
        FileOutputStream pdfFileout = new FileOutputStream(file);

        Float etat = 0.0f;

        Document document = new Document();

        Optional<EatFirma> systemCompany = eatFirmaService.findById(frmId);
        Client frm = clientService.getClientByKlKod(systemCompany.get().getFrmKlId()).get();

        try {
            //PdfWriter writer = PdfWriter.getInstance(document, pdfFileout );
            PdfWriter.getInstance(document, pdfFileout);
            document.setPageSize( PageSize.A4.rotate());
            int pages = 0;

            document.open();
            document.setMargins(10,10,36, 36);

            //FONTs
            BaseFont bf = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED ); // na polish chars
            Font helvFont10 = new Font(bf, 8);
            Font helvBoldFont10 = new Font(bf, 8, Font.BOLD);
            Font helvFont18 = new Font(bf, 18);
            Font helvBoldFont18 = new Font(bf, 18, Font.BOLD);

            float l = PageSize.A4.getLeft();
            float r = PageSize.A4.getRight();
            float b = PageSize.A4.getBottom();
            float t = PageSize.A4.getTop();

            Optional<List<EkDefGroup>> listEkDefGroup =  ekDefGroupRepo.findAllByDgDkKodOrderByDgNumer("PASEK");

            for ( EkDefGroup dg : listEkDefGroup.get() ){
                Optional<List<EkGroupCode>> listEkGrupyKodow =  ekGroupCodeRepo.findAllByGkDgKodOrderByGkNumer(dg.getDgKod());//hrPasekServiceBean.getEkGrupyKodow(dg.getDgKod());
                if (listEkGrupyKodow.isPresent()){
                    dg.setEkGrupyKodow(listEkGrupyKodow.get());
                }
            }

            List<User> listaAktPracNaSkMc = new ArrayList<>();
            if (prcId != 0){
                listaAktPracNaSkMc =  zatrudnienieService.getPracownikZatrudNaSkMc(prcId, okres, frmId, typeContract); //  hrPasekServiceBean.getPracownikZatrudNaSkMc();
            } else {
                //listaAktPracNaSkMc = hrPasekServiceBean.getPracownicyZatrudNaSkMc(skId, okres, frmId, typeContract);
                System.out.printf("brak dla sk w tej wersji");
            }

            for ( User p : listaAktPracNaSkMc ){

                /**
                 *  Tabela - nagu0142\u00F3wek paska
                 */
                PdfPTable tabNaglowek = new PdfPTable(3);
                tabNaglowek.setWidthPercentage(100);
                tabNaglowek.getDefaultCell().setUseAscender(true);
                tabNaglowek.getDefaultCell().setUseDescender(true);
                tabNaglowek.setWidths(new float[]{37,23,40});
                //tabelaNaglowek.setWidth

                PdfPCell cellNag_0_1 = new PdfPCell();
                PdfPCell cellNag_0_2 = new PdfPCell();

                PdfPCell cellNag_1_1 = new PdfPCell();
                PdfPCell cellNag_1_2 = new PdfPCell();
                PdfPCell cellNag_1_3 = new PdfPCell();

                PdfPCell cellNag_2_1 = new PdfPCell();
                PdfPCell cellNag_2_2 = new PdfPCell();
                PdfPCell cellNag_2_3 = new PdfPCell();

                PdfPCell cellNag_3_1 = new PdfPCell();
                PdfPCell cellNag_3_2 = new PdfPCell();
                PdfPCell cellNag_3_3 = new PdfPCell();


                cellNag_0_1.setColspan(2);
                cellNag_0_2.setHorizontalAlignment(Element.ALIGN_RIGHT);

                cellNag_0_1.setBorder(0);
                cellNag_0_2.setBorder(0);

                cellNag_1_1.setBorder(0);
                cellNag_1_2.setBorder(0);
                cellNag_1_3.setBorder(0);

                cellNag_2_1.setBorder(0);
                cellNag_2_2.setBorder(0);
                cellNag_2_3.setBorder(0);

                cellNag_3_1.setBorder(0);
                cellNag_3_2.setBorder(0);
                cellNag_3_3.setBorder(0);


                cellNag_0_2.addElement(new Paragraph("DRUK SPE\u0141NIA WYMOGI RAPORTU ZUS RMUA za miesi\u0105c: "  + okres , helvBoldFont10));
                cellNag_1_1.addElement(new Paragraph("Numer: " + p.getPrcNumer() + "   Nazwisko i imi\u0119: " + p.getNazwImie() , helvFont10));
                cellNag_1_2.addElement(new Paragraph("PESEL: " + p.getPrcPesel(), helvFont10));
                //cellNag_1_3.addElement(new Paragraph("NFZ: 12R - todo: oddziau0142 NFZ", helvFont10));

                cellNag_2_1.addElement(new Paragraph("Nazwa firmy: " + frm.getKldNazwa(), helvFont10));
                cellNag_2_2.addElement(new Paragraph("NIP: " + frm.getKldNip() + "  REGON: " + frm.getKldRegon(), helvFont10));

                if ( p.getZatrudnienia() != null ){
                    for ( Zatrudnienie zat : p.getZatrudnienia() ){
                        cellNag_2_3.addElement(new Paragraph("Kod tytu\u0142u ubezpieczenia.: " + zat.getDef0() + "       Wymiar: " + zat.getWymiarEtatu().getOpis(), helvFont10));
                        etat = zat.getWymiarEtatu().getEtat();
                    }
                }


                //cellNag_3_1.addElement(new Paragraph("   todo: miesi\u0105c i rok    2019          Tytuu0142 listy: Wszystkie listy", helvFont10));
                cellNag_3_2.addElement(new Paragraph(" ", helvFont10));
                //cellNag_3_3.addElement(new Paragraph("         Numer listy: Wszystkie listy ", helvFont10));

                tabNaglowek.addCell(cellNag_0_1);
                tabNaglowek.addCell(cellNag_0_2);
                tabNaglowek.addCell(cellNag_1_1);
                tabNaglowek.addCell(cellNag_1_2);
                tabNaglowek.addCell(cellNag_1_3);
                tabNaglowek.addCell(cellNag_2_1);
                tabNaglowek.addCell(cellNag_2_2);
                tabNaglowek.addCell(cellNag_2_3);
                tabNaglowek.addCell(cellNag_3_1);
                tabNaglowek.addCell(cellNag_3_2);
                tabNaglowek.addCell(cellNag_3_3);

                document.add(tabNaglowek);
                document.add(new Paragraph("", helvFont10));




                /**
                 *  Tabela - sku0142adniki wynagrodzenia
                 */
                PdfPTable tabSkladniki = new PdfPTable(11);

                tabSkladniki.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tabSkladniki.setWidthPercentage(100);
                tabSkladniki.getDefaultCell().setUseAscender(true);
                tabSkladniki.getDefaultCell().setUseDescender(true);
                tabSkladniki.setWidths(new int[] {30,8,8,15,8,10,10,10,10,10,40});

                tabSkladniki.addCell(new Paragraph("Naliczenia", helvFont10));
                //tabSkladniki.addCell(new Paragraph("Liczba dni przeprac.", helvFont10));
                tabSkladniki.addCell(new Paragraph("-", helvFont10));
                tabSkladniki.addCell(new Paragraph("Liczba godz. przeprac.", helvFont10));
                tabSkladniki.addCell(new Paragraph("Chorobowe ZUS", helvFont10));
                tabSkladniki.addCell(new Paragraph("Licz. dni wyn. chor.", helvFont10));
                tabSkladniki.addCell(new Paragraph(" ", helvFont10));
                tabSkladniki.addCell(new Paragraph("Sk\u0142adka na III filar", helvFont10));
                tabSkladniki.addCell(new Paragraph("Podst. sk\u0142. na ub. emer. i rent.", helvFont10));
                tabSkladniki.addCell(new Paragraph("Podst. sk\u0142. na ub. wypadkowe", helvFont10));
                tabSkladniki.addCell(new Paragraph("Podst. sk\u0142. na ub. chorobowe", helvFont10));

                // sku0142adki ZUS
                PdfPTable tabSkladZUS = new PdfPTable(6);
                tabSkladZUS.setWidthPercentage(100);
                PdfPCell cellSklPrcNag = new PdfPCell();
                PdfPCell cellSklFrmNag = new PdfPCell();

                cellSklPrcNag.addElement(new Paragraph("ZUS pracownik",helvFont10));
                cellSklPrcNag.setColspan(3);
                cellSklFrmNag.addElement(new Paragraph("ZUS pracodawca",helvFont10));
                cellSklFrmNag.setColspan(3);
                // pierwszy wiersz
                tabSkladZUS.addCell(cellSklPrcNag);
                tabSkladZUS.addCell(cellSklFrmNag);
                // drugi wiersz
                tabSkladZUS.addCell(new Paragraph("Em. 9,76%",helvFont10));
                tabSkladZUS.addCell(new Paragraph("Ren 1,5%",helvFont10));
                tabSkladZUS.addCell(new Paragraph("Chor. 2,45%",helvFont10));
                tabSkladZUS.addCell(new Paragraph("Em. 9,76%",helvFont10));
                tabSkladZUS.addCell(new Paragraph("Ren 6,5%",helvFont10));
                tabSkladZUS.addCell(new Paragraph("Wyp. 1,2%",helvFont10));

                PdfPCell cellSkladZUS = new PdfPCell();
                cellSkladZUS.setPadding(0);
                cellSkladZUS.addElement(tabSkladZUS);
                tabSkladniki.addCell(cellSkladZUS);

                // drugi wiersz tabeli Sku0142adnik\u00F3w
                // tabela naliczenia
                PdfPTable tabSkladNalicz = new PdfPTable(2);
                tabSkladNalicz.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tabSkladNalicz.setWidthPercentage(100);
                PdfPCell cellSkladNalicz = new PdfPCell();
                cellSkladNalicz.setPadding(0);


                PdfPTable tabSkladChor = new PdfPTable(2);
                tabSkladChor.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tabSkladChor.setWidthPercentage(100);
                PdfPCell cellSkladChor = new PdfPCell();
                cellSkladChor.setPadding(0);


                // definicje komorek dla pojedycznych wartosci

                PdfPCell cellSkladLiczbDniPrzeprac = new PdfPCell();
                cellSkladLiczbDniPrzeprac.setPadding(0);
                PdfPCell cellSkladLiczbaGodzPrzeprac = new PdfPCell();
                cellSkladLiczbaGodzPrzeprac.setPadding(0);
                PdfPCell cellSkladLiczbaDniChor = new PdfPCell();
                cellSkladLiczbaDniChor.setPadding(0);
                PdfPCell cellSklad3Filar = new PdfPCell();
                cellSklad3Filar.setPadding(0);
                PdfPCell cellSkladPodstEmRen = new PdfPCell();
                cellSkladPodstEmRen.setPadding(0);
                PdfPCell cellSkladPodstWyp = new PdfPCell();
                cellSkladPodstWyp.setPadding(0);
                PdfPCell cellSkladPodstChor = new PdfPCell();
                cellSkladPodstChor.setPadding(0);
                PdfPCell cellSkladPracownikEm = new PdfPCell();
                cellSkladPracownikEm.setPadding(0);
                PdfPCell cellSkladPracownikRen = new PdfPCell();
                cellSkladPracownikRen.setPadding(0);
                PdfPCell cellSkladPracownikChor = new PdfPCell();
                cellSkladPracownikChor.setPadding(0);
                PdfPCell cellSkladPracodawcaEm = new PdfPCell();
                cellSkladPracodawcaEm.setPadding(0);
                PdfPCell cellSkladPracodawcaRen = new PdfPCell();
                cellSkladPracodawcaRen.setPadding(0);
                PdfPCell cellSkladPracodawcaWyp = new PdfPCell();
                cellSkladPracodawcaWyp.setPadding(0);

                // komorki do tabeli potracen
                PdfPCell cellPotracKUP = new PdfPCell();
                cellPotracKUP.setPadding(0);
                PdfPCell cellPotracPodstPod = new PdfPCell();
                cellPotracPodstPod.setPadding(0);
                PdfPCell cellPotracBrutto = new PdfPCell();
                cellPotracBrutto.setPadding(0);
                PdfPCell cellPotracZalNalicz = new PdfPCell();
                cellPotracZalNalicz.setPadding(0);
                PdfPCell cellPotracUlga = new PdfPCell();
                cellPotracUlga.setPadding(0);
                PdfPCell cellPotracZwolPod = new PdfPCell();
                cellPotracZwolPod.setPadding(0);
                PdfPCell cellPotracZalPotr = new PdfPCell();
                cellPotracZalPotr.setPadding(0);
                PdfPCell cellPotracNetto = new PdfPCell();
                cellPotracNetto.setPadding(0);
                PdfPCell cellPotracDoWyplaty = new PdfPCell();
                cellPotracDoWyplaty.setPadding(0);
                PdfPCell cellPotracPrzelew = new PdfPCell();
                cellPotracPrzelew.setPadding(0);
                PdfPCell cellPotracStawkPod = new PdfPCell();
                cellPotracStawkPod.setPadding(0);

                PdfPCell cellZdrowPodst = new PdfPCell();
                cellZdrowPodst.setPadding(0);
                PdfPCell cellZdrowSkl1 = new PdfPCell();
                cellZdrowSkl1.setPadding(0);
                PdfPCell cellZdrowSkl2 = new PdfPCell();
                cellZdrowSkl2.setPadding(0);


                PdfPTable tabInnePotrac = new PdfPTable(2);
                tabInnePotrac.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tabInnePotrac.setWidthPercentage(100);
                PdfPTable tabInneDodat = new PdfPTable(2);
                tabInneDodat.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tabInneDodat.setWidthPercentage(100);
                PdfPCell cellInnePotrac = new PdfPCell();
                cellInnePotrac.setPadding(0);
                PdfPCell cellInneDodat = new PdfPCell();
                cellInneDodat.setPadding(0);

                PdfPTable tabSkladGrupa3 = new PdfPTable(2);
                tabSkladGrupa3.setWidthPercentage(100);
                PdfPCell cellSkladGrupa3 = new PdfPCell();
                cellSkladGrupa3.setPadding(0);



                String periodYYYYMM = okres;

                for ( EkDefGroup dg : listEkDefGroup.get() ){

//                        if (dg.getDgKod().equals("GRUPA1") ){
//
//                            for ( EkGrupyKodowVO dk : dg.getEkGrupyKodow() ){
//                                dk.setWartosc( hrPasekServiceBean.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
//                                if ( dk.getWartosc() != 0 ){
//                                    tabSkladNalicz.addCell(new Phrase(dk.getDskNazwa(), helvFont10));
//                                    tabSkladNalicz.addCell(new Phrase(dk.getWartosc().toString(),  helvFont10));
//                                }
//                            }
//                        }


                    if (dg.getDgKod().equals("GRUPA1") ){
                        String dskIdList = "";
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dskIdList += "'"+dk.getGkDskId()+"',";
                        }

                        List<EkSkladnikDTO> listSkladniku = skladnikService.getValueForListComponents(p.getPrcId(), periodYYYYMM, dskIdList, frmId, typeContract);

                        // lacze dwa skladniki 5 i 421 tak by scalić to tylko do dsk: 5
                        listSkladniku.stream().forEach( skl -> {
                            if (skl.getDskNazwa().equals("Wyrównanie do minimalnej")) {
                                listSkladniku.stream()
                                        .filter( s -> s.getDskNazwa().equals("Płaca zasad. z urlopami pomn. o chorob."))
                                        .forEach( s -> s.setWartosc( s.getWartosc() + skl.getWartosc()));
                            }
                        });
                        listSkladniku.removeIf( s -> s.getDskNazwa().equals("Wyrównanie do minimalnej") ); // usuniecie skladnika min.wyn

                        for ( EkSkladnikDTO s : listSkladniku ){
                            if ( s.getWartosc() != 0 ){
                                tabSkladNalicz.addCell(new Phrase(s.getDskNazwa(), helvFont10));
                                tabSkladNalicz.addCell(new Phrase(s.getWartosc().toString(),  helvFont10));
                            }
                        }
                    }


                    if (dg.getDgKod().equals("GRUPA2") ){
                        String dskIdList = "";
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dskIdList += "'"+dk.getGkDskId()+"',";
                        }

                        List<EkSkladnikDTO> listSkladniku = skladnikService.getValueForListComponents(p.getPrcId(), periodYYYYMM, dskIdList, frmId, typeContract);

                        for ( EkSkladnikDTO s : listSkladniku ){
                            if ( s.getWartosc() != 0 ){
                                tabSkladChor.addCell(new Phrase(s.getDskNazwa(), helvFont10));
                                tabSkladChor.addCell(new Phrase(s.getWartosc().toString(),  helvFont10));
                            }
                        }
                    }



                    if (dg.getDgKod().equals("GRUPA4") ){
                        String dskIdList = "";
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dskIdList += "'"+dk.getGkDskId()+"',";
                        }

                        List<EkSkladnikDTO> listSkladniku = skladnikService.getValueForListComponents(p.getPrcId(), periodYYYYMM, dskIdList, frmId, typeContract);

                        for ( EkSkladnikDTO s : listSkladniku ){
                            if ( s.getWartosc() != 0 ){
                                tabInnePotrac.addCell(new Phrase(s.getDskNazwa(), helvFont10));
                                tabInnePotrac.addCell(new Phrase(s.getWartosc().toString(),  helvFont10));
                            }
                        }
                    }



                    if (dg.getDgKod().equals("GRUPA5") ){
                        String dskIdList = "";
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dskIdList += "'"+dk.getGkDskId()+"',";
                        }

                        List<EkSkladnikDTO> listSkladniku = skladnikService.getValueForListComponents(p.getPrcId(), periodYYYYMM, dskIdList, frmId, typeContract);

                        for ( EkSkladnikDTO s : listSkladniku ){
                            if ( s.getWartosc() != 0 ){
                                tabInneDodat.addCell(new Phrase(s.getDskNazwa(), helvFont10));
                                tabInneDodat.addCell(new Phrase(s.getWartosc().toString(),  helvFont10));
                            }
                        }
                    }


                    if (dg.getDgKod().equals("SKL_3_FIL") ){
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dk.setWartosc( skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
                            cellSklad3Filar.addElement(new Phrase(" " + dk.getWartosc().toString(),  helvFont10));
                        }
                    }
                    if (dg.getDgKod().equals("POD_EM_REN") ){
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dk.setWartosc( skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
                            cellSkladPodstEmRen.addElement(new Phrase(" " + dk.getWartosc().toString(),  helvFont10));
                        }
                    }
                    if (dg.getDgKod().equals("POD_WYP") ){
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dk.setWartosc( skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
                            cellSkladPodstWyp.addElement(new Phrase(" " + dk.getWartosc().toString(),  helvFont10));
                        }
                    }
                    if (dg.getDgKod().equals("POD_CHOR") ){
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dk.setWartosc( skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
                            cellSkladPodstChor.addElement(new Phrase(" " + dk.getWartosc().toString(),  helvFont10));
                        }
                    }
                    if (dg.getDgKod().equals("ZUS_PC_EM") ){
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dk.setWartosc( skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
                            cellSkladPracownikEm.addElement(new Phrase(" " + dk.getWartosc().toString(),  helvFont10));
                        }
                    }
                    if (dg.getDgKod().equals("ZUS_PC_REN") ){
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dk.setWartosc( skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
                            cellSkladPracownikRen.addElement(new Phrase(" " + dk.getWartosc().toString(),  helvFont10));
                        }
                    }
                    if (dg.getDgKod().equals("ZUS_PC_CHO") ){
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dk.setWartosc( skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
                            cellSkladPracownikChor.addElement(new Phrase(" " + dk.getWartosc().toString(),  helvFont10));
                        }
                    }
                    if (dg.getDgKod().equals("ZUS_PD_EM") ){
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dk.setWartosc( skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
                            cellSkladPracodawcaEm.addElement(new Phrase(" " + dk.getWartosc().toString(),  helvFont10));
                        }
                    }
                    if (dg.getDgKod().equals("ZUS_PD_REN") ){
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dk.setWartosc( skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
                            cellSkladPracodawcaRen.addElement(new Phrase(" " + dk.getWartosc().toString(),  helvFont10));
                        }
                    }
                    if (dg.getDgKod().equals("ZUS_PD_WYP") ){
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dk.setWartosc( skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
                            cellSkladPracodawcaWyp.addElement(new Phrase(" " + dk.getWartosc().toString() + '\n' + ' ',  helvFont10));
                        }
                    }

                    // tabela 2
                    if (dg.getDgKod().equals("KOSZ_PRZ") ){
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dk.setWartosc( skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
                            if ( dk.getWartosc() != 0 ){
                                cellPotracKUP.addElement(new Phrase(" " + dk.getWartosc().toString(),  helvFont10));
                            }
                        }
                    }
                    if (dg.getDgKod().equals("POD_POD") ){
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dk.setWartosc( skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
                            cellPotracPodstPod.addElement(new Phrase(" " + dk.getWartosc().toString() + '\n' + ' ',  helvFont10));
                        }
                    }
                    if (dg.getDgKod().equals("BRUTTO") ){
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dk.setWartosc( skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
                            cellPotracBrutto.addElement(new Phrase(" " + dk.getWartosc().toString(),  helvFont10));
                        }
                    }
                    if (dg.getDgKod().equals("ZAL_NAL") ){
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dk.setWartosc( skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
                            cellPotracZalNalicz.addElement(new Phrase(" " + dk.getWartosc().toString(),  helvFont10));
                        }
                    }
//                        if (dg.getDgKod().equals("ULGA") ){
//                            for ( EkGrupyKodowVO dk : dg.getEkGrupyKodow() ){
//                                dk.setWartosc( hrPasekServiceBean.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
//                                if ( dk.getWartosc() != 0 ){
//                                    cellPotracUlga.addElement(new Phrase(" " + dk.getWartosc().toString(),  helvFont10));
//                                }
//                            }
//                        }

                    if (dg.getDgKod().equals("ULGA") ){
                        String dskIdList = "";
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dskIdList += "'"+dk.getGkDskId()+"',";
                        }

                        List<EkSkladnikDTO> listSkladniku = skladnikService.getValueForListComponents(p.getPrcId(), periodYYYYMM, dskIdList, frmId, typeContract);

                        for ( EkSkladnikDTO s : listSkladniku ){
                            if ( s.getWartosc() != 0 ){
                                cellPotracUlga.addElement(new Phrase(" " + s.getWartosc().toString(),  helvFont10));
                            }
                        }
                    }


                    if (dg.getDgKod().equals("ZWOL_POD") ){
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dk.setWartosc( skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
                            cellPotracZwolPod.addElement(new Phrase(" " + dk.getWartosc().toString(),  helvFont10));
                        }
                    }
//                        if (dg.getDgKod().equals("ZAL_POTR") ){
//                            for ( EkGrupyKodowVO dk : dg.getEkGrupyKodow() ){
//                                dk.setWartosc( hrPasekServiceBean.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
//                                if ( dk.getWartosc() != 0 ){
//                                    cellPotracZalPotr.addElement(new Phrase(" " + dk.getWartosc().toString(),  helvFont10));
//                                }
//                            }
//                        }

                    if (dg.getDgKod().equals("ZAL_POTR") ){
                        String dskIdList = "";
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dskIdList += "'"+dk.getGkDskId()+"',";
                        }

                        List<EkSkladnikDTO> listSkladniku = skladnikService.getValueForListComponents(p.getPrcId(), periodYYYYMM, dskIdList, frmId, typeContract);

                        for ( EkSkladnikDTO s : listSkladniku ){
                            if ( s.getWartosc() != 0 ){
                                cellPotracZalPotr.addElement(new Phrase(" " + s.getWartosc().toString(),  helvFont10));
                            }
                        }
                    }


                    if (dg.getDgKod().equals("NA_PRZELEW") ){
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dk.setWartosc( skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
                            if ( dk.getWartosc() != 0 ){
                                cellPotracPrzelew.addElement(new Phrase(" " + dk.getWartosc().toString(),  helvFont10));
                            }
                        }
                    }

                    if (dg.getDgKod().equals("UB_ZD_POD") ){
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dk.setWartosc( skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
                            cellZdrowPodst.addElement(new Phrase(" " + dk.getWartosc().toString() + '\n' + ' ',  helvFont10));
                        }
                    }
                    if (dg.getDgKod().equals("UB_ZD_SKL1") ){
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dk.setWartosc( skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
                            cellZdrowSkl1.addElement(new Phrase(" " + dk.getWartosc().toString(),  helvFont10));
                        }
                    }
                    if (dg.getDgKod().equals("UB_ZD_SKL2") ){
                        for ( EkGroupCode dk : dg.getEkGrupyKodow() ){
                            dk.setWartosc( skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, dk.getGkDskId(), frmId, typeContract) );
                            cellZdrowSkl2.addElement(new Phrase(" " + dk.getWartosc().toString(),  helvFont10));
                        }
                    }

                }

                // get il. day pracownika
                //double etatLong =  etat;
                //double ilDniPrzepracowanych = skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, Integer.valueOf(12465L) , frmId, typeContract).longValue()
                //        / (8L*etat);
                //skladnikService.getDniPrzeprac(p.getPrcId(), periodYYYYMM, frmId);
                cellSkladLiczbDniPrzeprac.addElement(new Phrase(" " //+ilDniPrzepracowanych
                        ,helvFont10));


                // get stawka podatkowa
                Long stawkaPodatkowa = skladnikService.getStawkaPodatkowa(p.getPrcId(), periodYYYYMM, frmId);
                cellPotracStawkPod.addElement(new Phrase(" " + stawkaPodatkowa.toString() + '\n' + '.', helvFont10));

                // get Brutto
                Double wynagrBrutto = skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, Integer.valueOf("610"), frmId, typeContract);
                cellPotracBrutto.addElement(new Phrase(" " + wynagrBrutto.toString(),  helvFont10));

                // get Netto
                Double wynagrNetto = skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, Integer.valueOf("10204"), frmId, typeContract);
                cellPotracNetto.addElement(new Phrase(" " + wynagrNetto.toString(),  helvFont10));

                // get DoWyplaty
                Double wynagrDoWyplaty = skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, Integer.valueOf("999"), frmId, typeContract);
                cellPotracDoWyplaty.addElement(new Phrase(" " + wynagrDoWyplaty.toString() + System.lineSeparator() ,  helvFont10));

                // get GodzinyPrzepracowane
                Double ilGodzPrzeprac = skladnikService.getValueFromPayroll( p.getPrcId(), periodYYYYMM, Integer.valueOf("12464"), frmId, typeContract);
                cellSkladLiczbaGodzPrzeprac.addElement(new Phrase(" " + ilGodzPrzeprac.toString(),  helvFont10));



                // get liczba dni chorobowego
                Long liczbaDniChorobowego = skladnikService.getLiczbaDniChorob(p.getPrcId(), periodYYYYMM, frmId );
                cellSkladLiczbaDniChor.addElement(new Phrase(" " + liczbaDniChorobowego.toString(), helvFont10));

                // usupelnic tabele danymi z bazy danych
                cellSkladNalicz.addElement(tabSkladNalicz);

                tabSkladniki.addCell(cellSkladNalicz);
                tabSkladniki.addCell(cellSkladLiczbDniPrzeprac); // liczba dni przeprac.
                tabSkladniki.addCell(cellSkladLiczbaGodzPrzeprac); // liczba godz. przeprac.

                cellSkladChor.addElement(tabSkladChor);
                tabSkladniki.addCell(cellSkladChor);

                tabSkladniki.addCell(cellSkladLiczbaDniChor); // liczba dni wyn. chorobowego

                cellSkladGrupa3.addElement(tabSkladGrupa3);
                tabSkladniki.addCell(cellSkladGrupa3); // skladniki grupy 3??
                tabSkladniki.addCell(cellSklad3Filar); // sku0142. na III filar
                tabSkladniki.addCell(cellSkladPodstEmRen); // podstawa emer. i rent
                tabSkladniki.addCell(cellSkladPodstWyp); // podstawa wypadkowe
                tabSkladniki.addCell(cellSkladPodstChor); // podstawa chorobowe

                // sku0142adki ZUS
                PdfPTable tabSkladZUSDane = new PdfPTable(6);
                tabSkladZUSDane.setWidthPercentage(100);

                tabSkladZUSDane.addCell(cellSkladPracownikEm);
                tabSkladZUSDane.addCell(cellSkladPracownikRen);
                tabSkladZUSDane.addCell(cellSkladPracownikChor);
                tabSkladZUSDane.addCell(cellSkladPracodawcaEm);
                tabSkladZUSDane.addCell(cellSkladPracodawcaRen);
                tabSkladZUSDane.addCell(cellSkladPracodawcaWyp);

                PdfPCell cellSkladZUSDane = new PdfPCell();
                cellSkladZUSDane.setPadding(0);
                cellSkladZUSDane.addElement(tabSkladZUSDane);
                tabSkladniki.addCell(cellSkladZUSDane);


                document.add(tabSkladniki);
                document.add(new Paragraph(" ", helvFont10));


                /**
                 *   Tabela potr\u0105ceń - potr\u0105cenia
                 */

                PdfPTable tabPotrac = new PdfPTable(12);

                tabPotrac.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tabPotrac.setWidthPercentage(100);
                tabPotrac.getDefaultCell().setUseAscender(true);
                tabPotrac.getDefaultCell().setUseDescender(true);
                tabPotrac.setWidths(new int[] {30,10,10,15,10,10,10,10,10,10,10,10});

                // elementy naglowka tabeli potr\u0105ceń - wiersz 1
                PdfPTable tabZdrowNagl = new PdfPTable(3);
                tabZdrowNagl.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tabZdrowNagl.setWidthPercentage(100);
                tabZdrowNagl.getDefaultCell().setUseAscender(true);
                tabZdrowNagl.getDefaultCell().setUseDescender(true);

                PdfPTable tabZdrowDane = new PdfPTable(3);
                tabZdrowDane.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tabZdrowDane.setWidthPercentage(100);
                tabZdrowDane.getDefaultCell().setUseAscender(true);
                tabZdrowDane.getDefaultCell().setUseDescender(true);

                PdfPCell cellZdNag = new PdfPCell();
                cellZdNag.addElement(new Paragraph("Ubezpieczenie zdrowotne",helvFont10));
                cellZdNag.setColspan(3);
                cellZdNag.setHorizontalAlignment(1);
                cellZdNag.setVerticalAlignment(1);
                tabZdrowNagl.addCell(cellZdNag);
                tabZdrowNagl.addCell(new Phrase("Podstawa",helvFont10));
                tabZdrowNagl.addCell(new Phrase("Sk\u0142adka 7,75%",helvFont10));
                tabZdrowNagl.addCell(new Phrase("Sk\u0142adka 1,25%",helvFont10));


                // dodanie elementow do tabeli tabPotrac - wiersz 1
                PdfPCell cellPotracUbZdNag = new PdfPCell();

                cellPotracUbZdNag.addElement(tabZdrowNagl);
                cellPotracUbZdNag.setPadding(0);
                tabPotrac.addCell(cellPotracUbZdNag);
                tabPotrac.addCell(new Phrase("Koszty uzyskania przychodu",helvFont10));
                tabPotrac.addCell(new Phrase("Podstawa podatkowa",helvFont10));
                tabPotrac.addCell(new Phrase("Wynagrodzenie brutto",helvFont10));
                tabPotrac.addCell(new Phrase("Zaliczka naliczona",helvFont10));
                tabPotrac.addCell(new Phrase("Ulga",helvFont10));
                tabPotrac.addCell(new Phrase("Zwol Podatk.",helvFont10));
                tabPotrac.addCell(new Phrase("Zaliczka potr\u0105cona",helvFont10));
                tabPotrac.addCell(new Phrase("Wynagrodz. netto",helvFont10));
                tabPotrac.addCell(new Phrase("Do wyp\u0142aty",helvFont10));
                tabPotrac.addCell(new Phrase("Na przelew",helvFont10));
                tabPotrac.addCell(new Phrase("Stawka podatkowa",helvFont10));


                // potr\u0105cenia - wiersz z danymi
                cellZdrowPodst.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabZdrowDane.addCell(cellZdrowPodst); //
                tabZdrowDane.addCell(cellZdrowSkl1);
                tabZdrowDane.addCell(cellZdrowSkl2);

                PdfPCell cellPotracUbZdDane = new PdfPCell();

                cellPotracUbZdDane.addElement(tabZdrowDane);
                cellPotracUbZdDane.setPadding(0);
                tabPotrac.addCell(cellPotracUbZdDane);
                tabPotrac.addCell(cellPotracKUP);
                tabPotrac.addCell(cellPotracPodstPod);
                tabPotrac.addCell(cellPotracBrutto);
                tabPotrac.addCell(cellPotracZalNalicz);
                tabPotrac.addCell(cellPotracUlga);
                tabPotrac.addCell(cellPotracZwolPod);
                tabPotrac.addCell(cellPotracZalPotr);
                tabPotrac.addCell(cellPotracNetto);
                tabPotrac.addCell(cellPotracDoWyplaty);
                tabPotrac.addCell(cellPotracPrzelew);
                tabPotrac.addCell(cellPotracStawkPod);

                document.add(tabPotrac);
                document.add(new Paragraph(" ",helvFont10));


                /**
                 *  Tabela - inne sku0142adniki i potr\u0105cenia
                 */

                PdfPTable tabInne = new PdfPTable(8);
                tabInne.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tabInne.setWidthPercentage(100);
                tabInne.getDefaultCell().setUseAscender(true);
                tabInne.getDefaultCell().setUseDescender(true);
                tabInne.setWidths(new float[]{13,13,13,13,13,13,13,5});

                tabInne.addCell(new Paragraph("Potr\u0105cenia", helvFont10));
                tabInne.addCell(new Paragraph("Inne dodatki", helvFont10));
                tabInne.addCell(new Paragraph("", helvFont10));
                tabInne.addCell(new Paragraph("", helvFont10));
                tabInne.addCell(new Paragraph("", helvFont10));
                tabInne.addCell(new Paragraph("", helvFont10));
                tabInne.addCell(new Paragraph("", helvFont10));
                tabInne.addCell(new Paragraph("Numer paska", helvFont10));

                PdfPCell cellInnePusty = new PdfPCell();
                cellInnePusty.setColspan(5);

                cellInnePotrac.addElement(tabInnePotrac);
                cellInneDodat.addElement(tabInneDodat);
                tabInne.addCell(cellInnePotrac);
                tabInne.addCell(cellInneDodat);
                tabInne.addCell(cellInnePusty);
                tabInne.addCell(new Paragraph("1", helvFont10));

                document.add(tabInne);



                /*
                  Stopka
                */
                document.add(new Paragraph("",helvFont10));
                document.add(new Paragraph("Informacje o przekroczeniu rocznej podstawy wymiaru sku\u0142adki na ubezpieczenie emerytalne i rentowe: ", helvFont10));
                document.add(new Paragraph("O\u015Bwiadczam, \u017Ce dane zawarte w formularzu s\u0105 zgodne ze stanem prawnym i faktycznym. Jestem \u015Bwiadomy{a) odpowiedzialno\u015Bci karnej "
                        + "za zeznanie nieprawdy lub zatajenie prawdy. ", helvFont10));

                Date dataWypelnienia = new Date();
                SimpleDateFormat dfDataWypelnienia = new SimpleDateFormat("dd.MM.y");
                document.add(new Paragraph("Data wype\u0142nienia: " + dfDataWypelnienia.format(dataWypelnienia), helvFont10));
                //document.add(new Phrase("Wykaz absencji: ",helvBoldFont10));
                Paragraph podstPrawna1 = new Paragraph();
                podstPrawna1.setAlignment(Element.ALIGN_RIGHT);
                podstPrawna1.setFont(helvFont10);
                podstPrawna1.add("Podstawa prawna dla RMUA: Ustawa z dnia 13.10.1998 r. o systemie ubezp. spo\u0142ecznych");
                Paragraph podstPrawna2 = new Paragraph();
                podstPrawna2.setAlignment(Element.ALIGN_RIGHT);
                podstPrawna2.setFont(helvFont10);
                podstPrawna2.add("(Dz. U. z 2007 r. Nr 11, poz. 74 z p\u00F3\u017An. zm.)");
                Paragraph podstPrawna3 = new Paragraph();
                podstPrawna3.setAlignment(Element.ALIGN_RIGHT);
                podstPrawna3.setFont(helvFont10);
                podstPrawna3.add("Dokument jest wydrukiem komputerowym i nie wymaga dodatkowych podpis\u00F3w oraz stempla firmowego.");
                document.add(podstPrawna1);
                document.add(podstPrawna2);
                document.add(podstPrawna3);

                document.newPage();
            }  // koniec for( Pracownik ...

            document.close();
//           writer.close();

        }
        catch ( Exception e ){
            throw new IOException("PASEK-e.154 - " + frm.getKldNazwa() + ": brak pask\u00F3w. Sprawd\u017A dok\u0142adnie wpiane parametry. Skid: " + skId);
        }

        return path;
    }


}
