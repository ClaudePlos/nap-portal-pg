package pl.kskowronski.data.service;

import org.springframework.stereotype.Service;
import pl.kskowronski.data.entity.egeria.AdvancePaymentDTO;
import pl.kskowronski.data.service.egeria.global.EatFirmaService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdvancePaymentService {

    @PersistenceContext
    private EntityManager em;

    private EatFirmaService eatFirmaService;

    public AdvancePaymentService(EatFirmaService eatFirmaService) {
        this.eatFirmaService = eatFirmaService;
    }

    public List<AdvancePaymentDTO> getAdvancePaymentForUser(Integer prcId, String year, String whetherClearing ){
        List<AdvancePaymentDTO> advancePayments = new ArrayList<>();

        String sql = "SELECT pl_frm_id, pl_data, ROZ_NUMER\n" +
                "     --,SUM(decode(rozl, 'N', decode(rozliczony, 'T', 0, 1 ), 1) * PL_KWOTA_WALUTY_WN) wn\n" +
                "     , SUM(case rozl when 'N' then case rozliczony when 'T' then 0 else 1 end else 1 end * PL_KWOTA_WALUTY_WN) wn\n" +
                "     , SUM(case rozl when 'N' then case rozliczony when 'T' then 0 else 1 end else 1 end * PL_KWOTA_WALUTY_MA) ma\n" +
                "     --, MIN(case PL_F_ROZNICA_KURSOWA when 'N' then pl_f_rozliczona(pl_id,null,data.potw) else 'T' end) test\n" +
                "     --,SUM(decode(rozl, 'N', decode(rozliczony, 'T', 0, 1 ), 1) * PL_KWOTA_WALUTY_MA) ma\n" +
                "     , pl_opis, pl_data_zaplaty, MIN(rozliczony_na_dzis) rozliczone\n" +
                "     --,SUM(decode(rozl, 'N', decode(rozliczony, 'T', 0, 1 ), 1) * PL_KWOTA_WN)\n" +
                "     --,SUM(decode(rozl, 'N', decode(rozliczony, 'T', 0, 1 ), 1) * PL_KWOTA_MA)\n" +
                "     --,SUM(PL_KWOTA_EURO_WN),SUM(PL_KWOTA_EURO_MA)\n" +
                "     , MIN (rozliczony) FROM NZT_ROZRACHUNKI, NZT_PLATNOSCI,\n" +
                "                             (SELECT to_date(to_char(CURRENT_TIMESTAMP, 'YYYY-MM-DD'),'YYYY-MM-DD') NA_DZIEN,'%' POTW,'X' rozl ) DATA,\n" +
                "                             (SELECT r.pl_id rozl_pl_id\n" +
                "                                   , CASE WHEN (to_date(to_char(CURRENT_TIMESTAMP, 'YYYY-MM-DD'),'YYYY-MM-DD') >= pl_data_zaplaty2(r.pl_id, '%') ) THEN case r.PL_F_ROZNICA_KURSOWA when 'N' then r.PL_F_ROZLICZONA else 'T' end ELSE  r.PL_F_ROZNICA_KURSOWA END rozliczony\n" +
                "                                   , case r.PL_F_ROZNICA_KURSOWA when 'N' then  pl_f_rozliczona(r.pl_id,NULL,'%') else 'T' end rozliczony_na_dzis\n" +
                "                              FROM nzt_rozrachunki, nzt_platnosci r where pl_roz_id = roz_id and roz_prc_id = " + prcId + "  AND  roz_rp_rok = " + year + ") rozl\n" +
                "WHERE ( pl_roz_id = roz_id AND pl_f_anulowana = 'N' and rozl.rozl_pl_id = pl_id ) AND (((( roz_prc_id = " + prcId + "))  AND ( roz_rp_rok = " + year + ")))\n" +
                "GROUP BY roz_numer, roz_kl_kod , pl_frm_id, pl_opis, pl_data_zaplaty, pl_data\n" +
                "HAVING ( MIN(case PL_F_ROZNICA_KURSOWA when 'N' then pl_f_rozliczona(pl_id,null,data.potw) else 'N' end) = '" + whetherClearing + "' ) order by pl_data;";


        List<Object[]> result = em.createNativeQuery(sql).getResultList();

        for ( Object[] item : result ) {
            AdvancePaymentDTO p = new AdvancePaymentDTO();
            p.setFrmName( eatFirmaService.findById( Integer.valueOf( ((BigDecimal) item[0]).toString() )).get().getFrmNazwa());
            p.setCreationDate( String.valueOf( (Date) item[1]) );
            p.setRozNumber( (String) item[2] );
            p.setDtAmount( (BigDecimal) item[3] );
            p.setCtAmount( (BigDecimal) item[4] );
            p.setDescription( (String) item[5] );
            p.setClearingDate( String.valueOf( (Date) item[6] != null ? (Date) item[6] : "") );
            p.setWhetherClearing( ((Character) item[7]).toString() );
            advancePayments.add(p);
        }

        return advancePayments;
    }


}
