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

    public List<AdvancePaymentDTO> getAdvancePaymentForUser(Integer prcNumber, String period, String whetherClearing ){
        List<AdvancePaymentDTO> advancePayments = new ArrayList<>();

        String sql = "select tab.* from (\n" +
                "select pl_frm_id, pl_data, pl_kwota_wn, pl_kwota_ma, pl_opis, pl_data_zaplaty, spr_platnosci(pl_id) czRozliczone\n" +
                "from kg_konta, nzt_platnosci p1\n" +
                "where pl_knt_id = knt_id\n" +
                "  and knt_pelny_numer like '234-" + prcNumber + "-02' \n" +
                "  and to_char(pl_data,'YYYY') = '" + period + "' " +
                "  and pl_kwota_ma != 0\n" +
                "order by pl_data) tab\n" +
                "where czRozliczone ='" + whetherClearing + "' ";


//        String sql2 = "select pl_frm_id, pl_data, pl_kwota_wn, pl_kwota_ma, pl_opis, pl_data_zaplaty, pl_f_rozliczona \n" +
//                "from kg_konta, nzt_platnosci p1\n" +
//                "where pl_knt_id = knt_id \n" +
//                "and knt_pelny_numer like '234-" + prcNumber + "-02' \n" +
//                "and to_char(pl_data,'YYYY') = '" + period + "' " +
//                "and pl_kwota_ma != 0\n" +
//                "and case when PL_F_ZA_BO = 'T' then (select pl_f_rozliczona from nzt_platnosci p2 where p2.pl_pl_id_bo = p1.pl_id) else pl_f_rozliczona end  = '" + whetherClearing + "' " +
//                "and pl_f_rozliczona = '" + whetherClearing + "' " +
//                "order by pl_data";

        List<Object[]> result = em.createNativeQuery(sql).getResultList();

        for ( Object[] item : result ) {
            AdvancePaymentDTO p = new AdvancePaymentDTO();
            p.setFrmName( eatFirmaService.findById( Integer.valueOf( ((BigDecimal) item[0]).toString() )).get().getFrmNazwa());
            p.setCreationDate( String.valueOf( (Date) item[1]) );
            p.setDtAmount( (BigDecimal) item[2] );
            p.setCtAmount( (BigDecimal) item[3] );
            p.setDescription( (String) item[4] );
            p.setClearingDate( String.valueOf( (Date) item[5] != null ? (Date) item[5] : "") );
            p.setWhetherClearing( ((Character) item[6]).toString() );
            advancePayments.add(p);
        }

        return advancePayments;
    }


}
