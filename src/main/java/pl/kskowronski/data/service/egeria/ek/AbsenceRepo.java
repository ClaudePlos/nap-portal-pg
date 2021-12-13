package pl.kskowronski.data.service.egeria.ek;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kskowronski.data.entity.egeria.ek.Absencja;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AbsenceRepo extends JpaRepository<Absencja, Integer> {

    @Query("select a from Absencja a where a.abPrcId = :prcId and a.abDataOd >= :dateFrom and a.abDataOd <= :dateTo " +
            "and a.abRdaId != 100641" +
            "order by a.abDataOd desc")
    Optional<List<Absencja>> findAllByAbPrcIdForYear(@Param("prcId") Integer prcId
            , @Param("dateFrom") Date dateFrom
            , @Param("dateTo") Date dateTo);

}