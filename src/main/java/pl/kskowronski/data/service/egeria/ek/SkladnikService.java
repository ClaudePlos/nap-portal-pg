package pl.kskowronski.data.service.egeria.ek;

import org.springframework.stereotype.Service;
import pl.kskowronski.data.entity.egeria.ek.EkSkladnikDTO;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Service
public class SkladnikService  {

    @PersistenceContext
    private EntityManager em;


    public List<EkSkladnikDTO> getValueForListComponents (Integer prcId, String periodYYYYMM, String dskIdList, Integer frmId, Long typeContract){
        List<EkSkladnikDTO> listComponents = new ArrayList<>();
        List<Object[]> listComObj = new ArrayList<>();
        String contract = "LPLAC";
        //consolidationService.setConsolidateCompanyOnCompany( frmId );

        if ( typeContract == 2 ){
            contract = "ZLEC";
        }

        try {
            String sql = "select dsk_nazwa, COALESCE(sum(sk_wartosc), 0) wart \n" +
                    " from ek_listy, ek_skladniki, ek_def_skladnikow \n" +
                    " where sk_lst_id = lst_id\n" +
                    "   and sk_dsk_id = dsk_id\n" +
                    "   and sk_prc_id = " + prcId + "\n" +
                    "   and lst_drl_kod = '" + contract + "'\n" +
                    "   and sk_status = 1 \n" +
                    "   and to_char(lst_data_obliczen,'YYYY-MM') = '" + periodYYYYMM + "' and sk_frm_id = " + frmId + "  \n" +
                    "   and sk_dsk_id in (" + dskIdList.substring(0, dskIdList.length()-1) + ") group by dsk_nazwa order by dsk_nazwa";

            listComObj = em.createNativeQuery(sql).getResultList();

            for ( Object[] l : listComObj )
            {
                EkSkladnikDTO sk = new EkSkladnikDTO();
                sk.setDskNazwa( (String )l[0]);
                sk.setWartosc( (Double ) l[1] );
                listComponents.add(sk);
                //System.out.println(sk.getDskNazwa() + " " + sk.getWartosc());
            }

        } catch (Exception e) {
            return listComponents;
        }
        return listComponents;
    }

    public Double getValueFromPayroll( Integer prcId, String periodYYYYMM, Integer dskId, Integer frmId, Long typeContract ){
        String contract = "LPLAC";
        //consolidationService.setConsolidateCompanyOnCompany( frmId );

        if ( typeContract == 2 ){
            contract = "ZLEC";
        }

        try {
            String sql = "select COALESCE(sum(sk_wartosc), 0) wart \n" +
                    " from ek_listy, ek_skladniki \n" +
                    " where sk_lst_id = lst_id\n" +
                    "   and sk_prc_id = " + prcId + "\n" +
                    "   and lst_drl_kod = '" + contract + "'\n" +
                    "   and sk_status = 1 \n" +
                    "   and to_char(lst_data_obliczen,'YYYY-MM') = '" + periodYYYYMM + "'  \n" +
                    "   and sk_dsk_id = '" + dskId + "'" +
                    "   and sk_frm_id = " + frmId;
            List result = em.createNativeQuery(sql).getResultList();
            for (Iterator iter = result.iterator(); iter.hasNext();)
            {
                Double value = (Double) iter.next();
                return value;
            }

        } catch (Exception e) {
            return Double.parseDouble("0");
        }
        return Double.parseDouble("0");
    }

    public Long getDniPrzeprac( Integer prcId, String periodYYYYMM, Integer frmId ){
        //consolidationService.setConsolidateCompanyOnCompany( frmId );

        try {
            String sql = "select ek_pck_staze2.dni_abs(" + prcId + ", \n" +
                    "'PRZEPRAC', to_date('" + periodYYYYMM + "', 'YYYY-MM'), \n" +
                    "LAST_DAY( to_date('" + periodYYYYMM + "', 'YYYY-MM') ), \n" +
                    "'HI_ID') from dual ";

            List result = em.createNativeQuery(sql).getResultList();
            for (Iterator iter = result.iterator() ; iter.hasNext();)
            {
                Integer value = (Integer) iter.next();
                return Long.parseLong(value.toString());
            }
        } catch (Exception e) {
            return Long.parseLong("0");
        }
        return Long.parseLong("0");
    }

    public Long getStawkaPodatkowa( Integer prcId, String periodYYYYMM, Integer frmId ){
        //consolidationService.setConsolidateCompanyOnCompany( frmId );

        try {
//            String sql = "select ek_pck_rap_pasek.stawka_podatkowa(" + prcId + ", \n" +
//                    "to_number(substr('" + periodYYYYMM + "',0,4)), \n" +
//                    "to_number(substr('" + periodYYYYMM + "',6,2)) ) \n" +
//                    "from dual ";

            String sql = "SELECT dnm_stawka_podatku\n" +
                    " FROM EK_DANE_MIESIECZNE\n" +
                    " WHERE dnm_prc_id = " + prcId + "\n" +
                    " AND dnm_rok = cast(substr('" + periodYYYYMM + "',1,4) as integer)\n" +
                    " AND dnm_miesiac =  cast(substr('" + periodYYYYMM + "',6,2) as integer) + 1 and dnm_frm_id = " + frmId;

            List result = em.createNativeQuery(sql).getResultList();
            for (Iterator iter = result.iterator() ; iter.hasNext();)
            {
                BigDecimal value = (BigDecimal) iter.next();
                return Long.valueOf(value.intValue());
            }
        } catch (Exception e) {
            return Long.parseLong("0");
        }
        return Long.parseLong("0");
    }

    public Long getLiczbaDniChorob( Integer prcId, String periodYYYYMM, Integer frmId ){
        //consolidationService.setConsolidateCompanyOnCompany( frmId );

        try {
//        String sql = "select nap_rap_pasek.liczba_dni_chorobowego(" + prcId + ", '" + periodYYYYMM + "') wart from dual";
            String sql = "SELECT COALESCE( sum(AB_DNI_WYKORZYSTANE), 0) wart FROM ek_absencje where ab_prc_id = " + prcId + " \n" +
                    "AND ab_data_od <= LAST_DAY( to_date('" + periodYYYYMM + "', 'YYYY-MM') ) \n " +
                    "AND ab_data_do >= to_date('" + periodYYYYMM + "', 'YYYY-MM') \n" +
                    "AND ab_rda_id IN (SELECT ga_rda_id FROM ek_grupy_absencji WHERE ga_dg_kod = 'CHOR1')";

            List result = em.createNativeQuery(sql).getResultList();
            for (Iterator iter = result.iterator() ; iter.hasNext();)
            {
                BigDecimal value = (BigDecimal) iter.next();
                return Long.valueOf(value.intValue());
            }
        } catch (Exception e) {
            return Long.parseLong("0");
        }
        return Long.parseLong("0");
    }


}
