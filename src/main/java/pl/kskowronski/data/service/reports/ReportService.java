package pl.kskowronski.data.service.reports;

import org.springframework.stereotype.Service;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.egeria.css.SK;

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

    public List<User> getWorkersListWithPassForSK(String skKod){
        List<User> listWorkers = new ArrayList<>();
        LocalDate lt = LocalDate.now();
        var year = lt.getYear()-1;
        String sql = "select distinct prc_numer PrcNumer, prc_nazwisko NAZWISKO, prc_imie IMIE, case when prc_dg_kod_ek = 'EK04' then null else prc_identyfikacja end HASLO\n" +
                "from ek_pracownicy, ek_zatrudnienie, css_Stanowiska_kosztow sk, ek_skladniki, ek_listy  \n" +
                "where zat_prc_id = prc_id and zat_sk_id = sk.sk_id\n" +
                "and sk_lst_id = lst_id and sk_zat_id = zat_id\n" +
                "and to_char(lst_data_wyplaty, 'YYYY') = to_char(" + year + ",'YYYY')\n" +
                "and sk_dsk_id in (200,999)\n" +
                "and sk_kod LIKE '" + skKod + "'" +
                "order by prc_nazwisko, prc_imie";
        Optional<List<Object[]>> results = Optional.ofNullable(em.createNativeQuery(sql).getResultList());
        if (results.isPresent())
            results.get().forEach( item -> listWorkers.add( new User(
                    ((BigDecimal) item[0]).intValue(), (String) item[3], (String) item[2], (String) item[1])) );
        return listWorkers;
    }

}
