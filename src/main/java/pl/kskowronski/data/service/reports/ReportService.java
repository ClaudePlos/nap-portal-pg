package pl.kskowronski.data.service.reports;

import org.springframework.stereotype.Service;
import pl.kskowronski.data.entity.admin.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @PersistenceContext
    private EntityManager em;

    public ReportService() {
    }

    public List<User> getWorkersListWithPassForSK(Integer skId){
        List<User> listWorkers = new ArrayList<>();
        LocalDate lt = LocalDate.now();
        var year = lt.getYear()-1;
        String sql = "select distinct prc_numer, prc_nazwisko, prc_imie, case when prc_dg_kod_ek = 'EK04' then null else prc_identyfikacja end pass\n" +
                " from ek_skladniki, ek_listy, ek_pracownicy, ek_zatrudnienie\n" +
                "where sk_lst_id = lst_id\n" +
                "  and prc_id = sk_prc_id\n" +
                "  and zat_prc_id = prc_id\n" +
                "  and sk_zat_id = zat_id\n" +
                "  and zat_sk_id = " + skId +
                "  and to_char(lst_data_wyplaty, 'YYYY') = '" + year + "'\n" +
                "  and sk_dsk_id in (200,999) order by prc_nazwisko";
        Optional<List<Object[]>> results = Optional.ofNullable(em.createNativeQuery(sql).getResultList());
        if (results.isPresent())
            results.get().forEach( item -> listWorkers.add( new User(
                    ((BigDecimal) item[0]).intValue(), (String) item[3], (String) item[2], (String) item[1])) );
        return listWorkers;
    }

}
