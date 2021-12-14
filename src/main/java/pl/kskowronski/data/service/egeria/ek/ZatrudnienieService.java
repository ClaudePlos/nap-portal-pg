package pl.kskowronski.data.service.egeria.ek;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.MapperDate;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.egeria.ek.*;
import pl.kskowronski.data.service.egeria.css.JORepo;
import pl.kskowronski.data.service.egeria.global.EatFirmaService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ZatrudnienieService extends CrudService<Zatrudnienie, Integer> {

    @PersistenceContext
    private EntityManager em;

    private ZatrudnienieRepo repo;

    public ZatrudnienieService(@Autowired ZatrudnienieRepo repo) {
        this.repo = repo;
    }

    @Override
    protected ZatrudnienieRepo getRepository() {
        return repo;
    }

    @Autowired
    WymiarEtatuRepo wymiarEtatuRepo;

    @Autowired
    JORepo joRepo;

    @Autowired
    EatFirmaService eatFirmaService;

    private SimpleDateFormat dfYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");

    private MapperDate mapperDate = new MapperDate();

    public Optional<List<Zatrudnienie>> getActualContractForWorker(Integer prcId, String period) throws ParseException {
        //consolidationService.setConsolidateCompany();
        Date dataOd = dfYYYYMMDD.parse(period + "-01");
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataOd);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH)); //last day month
        Date dataDo = cal.getTime();

        Optional<List<Zatrudnienie>> contracts = Optional.of(new ArrayList<>());
        try {
            contracts = Optional.ofNullable(em.createQuery("select z from Zatrudnienie z where z.zatPrcId = :prcId "
                    + "and z.zatDataZmiany <= :dataDo and COALESCE(z.zatDataDo, :dataOd) >= :dataOd "
                    + "and z.zatTypUmowy = 0")
                    .setParameter("prcId", prcId)
                    .setParameter("dataOd", dataOd, TemporalType.DATE)
                    .setParameter("dataDo", dataDo, TemporalType.DATE)
                    .getResultList());
        } catch ( Exception ex ){
            System.out.println(ex.getMessage());
        }

        if ( contracts.isPresent() ){
            contracts.get().stream().forEach( z -> {
                z.setFrmNazwa(eatFirmaService.findById(z.getFrmId()).get().getFrmNazwa());
                z.setWymiarEtatu(wymiarEtatuRepo.findById(z.getZatWymiar().toString()).get());
                z.setJoName(joRepo.findById(z.getZatObId()).get().getObNazwa());
            });
        }
        return contracts;
    }

    public Optional<List<Zatrudnienie>> getActualContractUzForWorker(Integer prcId, String period) throws ParseException {
        //consolidationService.setConsolidateCompany();
        Date dataOd = dfYYYYMMDD.parse(period + "-01");
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataOd);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH)); //last day month
        Date dataDo = cal.getTime();

        Optional<List<Zatrudnienie>> contracts = Optional.of(new ArrayList<>());
        try {
            contracts = Optional.ofNullable(em.createQuery("select z from Zatrudnienie z where z.zatPrcId = :prcId "
                    + "and z.zatDataZmiany <= :dataDo and COALESCE(z.zatDataDo, :dataOd) >= :dataOd "
                    + "and z.zatTypUmowy in (2)")
                    .setParameter("prcId", prcId)
                    .setParameter("dataOd", dataOd, TemporalType.DATE)
                    .setParameter("dataDo", dataDo, TemporalType.DATE)
                    .getResultList());
        } catch ( Exception ex ){
            System.out.println(ex.getMessage());
        }

        if ( contracts.isPresent() ){
            contracts.get().stream().forEach( z -> {
                z.setFrmNazwa(eatFirmaService.findById(z.getFrmId()).get().getFrmNazwa());
                z.setJoName(joRepo.findById(z.getZatObId()).get().getObNazwa());
            });
        }
        return contracts;
    }

    public List<User> getPracownikZatrudNaSkMc(Integer prcIdForm, String okres, Integer frmId, Long typeContract ){
        //consolidationService.setConsolidateCompanyOnCompany(frmId);
        List<User> listaAktPracNaSk = new ArrayList<User>();
        // todo KS usunąć stanowiska kosztow administracji
        String sql = "select distinct prc_id, prc_numer, prc_nazwisko, prc_imie, prc_pesel, zat_wymiar, zat_status\n" +
                "from ek_zatrudnienie, ek_pracownicy\n";
        sql += "where (COALESCE(zat_data_do, to_date('2099', 'YYYY')) >= to_date('" + okres + "', 'YYYY-MM')\n" +
                "and zat_data_zmiany <= last_day(to_date('" + okres + "', 'YYYY-MM')))\n" +
                "and zat_typ_umowy = " + typeContract + "\n" +
                "and zat_prc_id = prc_id\n";
        sql += "and zat_prc_id = " + prcIdForm + " order by prc_nazwisko, prc_imie";

        List result = em.createNativeQuery(sql).getResultList();
        for (Iterator iter = result.iterator(); iter.hasNext();)
        {
            Object[] ob = (Object[]) iter.next();
            User prac = new User();
            Integer prcId = ((BigDecimal) ob[0]).intValue();
            Integer prcNumer = ((BigDecimal) ob[1]).intValue();
            prac.setPrcId(Integer.valueOf(prcId.toString()));
            prac.setPrcNumer(Integer.valueOf(prcNumer.toString()));
            prac.setPrcNazwisko((String) ob[2]);
            prac.setPrcImie((String) ob[3]);
            prac.setPrcPesel((String) ob[4]);

            if ( typeContract == 0 ){
                Zatrudnienie zat = new Zatrudnienie();
                List<Zatrudnienie> listZat = new ArrayList<>();
                Optional<WymiarEtatu> wymEtatu =  wymiarEtatuRepo.findById( ((BigDecimal) ob[5]).toString() );
                zat.setWymiarEtatu(wymEtatu.get());
                zat.setDef0( ((BigDecimal) ob[6]).toString() );
                listZat.add(zat);
                prac.setZatrudnienia(listZat);
            }

            listaAktPracNaSk.add(prac);
        }
        return listaAktPracNaSk;
    }

    public ListaPlacDTO getlPlacNaglowek(Integer prcId, Date okres, Integer frmId){
        ListaPlacDTO lP = new ListaPlacDTO();

        String sql = "select distinct lst_numer, to_char(lst_data_obliczen,'MM-YYYY') za_mc, to_char(lst_data_wyplaty,'DD-MM-YYYY') " +
                "from EK_GRUPY_KODOW, ek_skladniki, ek_listy, ek_def_skladnikow " +
                "where sk_dsk_id = dsk_id " +
                "and sk_lst_id = lst_id " +
                "and gk_dsk_id = dsk_id " +
                "and lst_drl_kod = 'LPLAC' " +
                "and sk_prc_id = " + prcId +
                "and sk_frm_id = " + frmId +
                "and sk_Wartosc != 0 " +
                "and lst_lst_id_korekta is null " +
                "and to_char(lst_data_obliczen,'YYYY-MM') ='"+ mapperDate.dtYYYYMM.format(okres) +"'";
        try{
            Object[] ob = (Object[]) em.createNativeQuery(sql).getSingleResult();

            lP.setLSTnumber( (String) ob[0] );
            lP.setZaMsc((String) ob[1]);
            lP.setDataWypl( (String) ob[2]);

        }catch (Exception e){
            throw new RuntimeException("Nie moge pobrac listy płac dla pracownika err-79we351 param: prcid:" + prcId);
        }
        return lP;
    }

    public List<ListaPlacWartKolumnyDTO> getLPlacWartKol(Integer prcId, Date okres, String kol, Integer frmId){
        List<Object[]> lPW = null;
        List<ListaPlacWartKolumnyDTO> lWartKolumny = new ArrayList<ListaPlacWartKolumnyDTO>();

        String sql = "select dsk_skrot, sk_wartosc from EK_GRUPY_KODOW, ek_skladniki, ek_listy, ek_def_skladnikow \n" +
                "where sk_dsk_id = dsk_id\n" +
                "and sk_lst_id = lst_id \n" +
                "and gk_dsk_id = dsk_id\n" +
                "and lst_drl_kod = 'LPLAC'\n" +
                "and gk_dg_kod = '"+ kol +"'\n" +
                "and sk_prc_id = "+ prcId +"\n" +
                "and sk_frm_id = "+ frmId +"\n" +
                "and sk_Wartosc != 0\n" +
                "and lst_lst_id_korekta is null\n" +
                "and to_char(lst_data_obliczen,'YYYY-MM') = '"+ mapperDate.dtYYYYMM.format(okres) +"'\n" +
                "order by gk_numer";

        try{

            Query query = em.createNativeQuery(sql);
            lPW = query.getResultList();

            for(Object[] l : lPW) {
                ListaPlacWartKolumnyDTO lP = new ListaPlacWartKolumnyDTO();
                lP.setDskSkrot((String) l[0]);
                lP.setSkWartosc((Integer) l[1]);
                lP.setKolumna(kol);
                lWartKolumny.add(lP);
            }

        }catch (Exception e){
            throw new RuntimeException("Nie moge pobrac listy płac dla pracownika err-79we351 param: prcid:" + prcId);
        }

        return lWartKolumny;
    }

    public List<ListaPlacWartKolumnyDTO> getListPlacWartKol8(Integer prcId, Date okres, Integer frmId)
    {
        Object[] lPW = null;
        List<ListaPlacWartKolumnyDTO> lWartKolumny = new ArrayList<ListaPlacWartKolumnyDTO>();

        String sql = "select to_char(zat_status/10)||ek_pck_niepelnosprawnosc.najlepsza_na_dzien(zat_prc_id,last_day(lst_data_wyplaty))\n" +
                " , to_char((select SUBSTR(wsl_alias,1,3)/SUBSTR(wsl_alias,4,6) from css_wartosci_slownikow where wsl_sl_nazwa like 'TYP_ETATU' and wsl_wartosc = zat_wymiar)) etat\n" +
                "   from ek_zatrudnienie,ek_skladniki,ek_listy\n" +
                "   where sk_zat_id = zat_id\n" +
                "   and sk_lst_id = lst_id\n" +
                "   and to_char(lst_data_obliczen,'YYYY-MM') = '"+ mapperDate.dtYYYYMM.format(okres) +"'\n" +
                "   and LST_DRL_KOD = 'LPLAC'\n" +
                "   and lst_lst_id_korekta is null\n" +
                "   and zat_prc_id = " + prcId +"\n" +
                "   and zat_frm_id = " + frmId +"\n" +
                "   and rownum = 1 ";

        try{

            Query query = em.createNativeQuery(sql);
            lPW = (Object[]) query.getSingleResult();


            ListaPlacWartKolumnyDTO lP = new ListaPlacWartKolumnyDTO();
            lP.setDskSkrot("Kod ubezp:");
            lP.setKolumna("KOL8");
            lP.setOpis((String) lPW[0]);
            lWartKolumny.add(lP);

            ListaPlacWartKolumnyDTO lP1 = new ListaPlacWartKolumnyDTO();
            lP1.setDskSkrot("Etat:");
            lP1.setKolumna("KOL8");
            lP1.setOpis((String) lPW[1]);
            lWartKolumny.add(lP1);


        }catch ( Exception e ) {
            throw new RuntimeException("Nie moge pobrac getListPlacWartKol8 param: prcid:" + prcId + " sql:" + sql);
        }

        return lWartKolumny;
    }

    public List<ListaPlacWartKolumnyDTO> getListPlacWartKol8PrzelKasa(Integer prcId, Date okres, Integer frmId)
    {
        List<Object[]> lPW = null;
        List<ListaPlacWartKolumnyDTO> lWartKolumny = new ArrayList<ListaPlacWartKolumnyDTO>();

        String sql = "select distinct dsk_skrot, to_char(sk_wartosc) from EK_GRUPY_KODOW, ek_skladniki, ek_listy, ek_def_skladnikow \n" +
                "where sk_dsk_id = dsk_id\n" +
                "and sk_lst_id = lst_id \n" +
                "and gk_dsk_id = dsk_id\n" +
                "and lst_drl_kod = 'LPLAC'\n" +
                "and sk_dsk_id in (200,999)\n" +
                "and sk_prc_id = "+ prcId +"\n" +
                "and sk_frm_id = "+ frmId +"\n" +
                "and sk_Wartosc != 0\n" +
                "and lst_lst_id_korekta is null\n" +
                "and to_char(lst_data_obliczen,'YYYY-MM') = '"+ mapperDate.dtYYYYMM.format(okres) +"'";
        // "order by gk_numer";

        try{

            Query query = em.createNativeQuery(sql);
            lPW = query.getResultList();

            for(Object[] l : lPW){
                ListaPlacWartKolumnyDTO lP = new ListaPlacWartKolumnyDTO();
                lP.setDskSkrot((String) l[0]);
                lP.setOpis((String) l[1]);
                lP.setKolumna("KOL8");
                lWartKolumny.add(lP);
            }

        }catch ( Exception e ) {
            throw new RuntimeException("Nie moge pobrac listy płac dla pracownika err-79we351 param: prcid:" + prcId);
        }

        return lWartKolumny;
    }

}
