package pl.kskowronski.data.service.egeria.edek;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kskowronski.data.entity.egeria.eDek.EdktDeklaracje;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EdktDeklaracjeRepo extends JpaRepository<EdktDeklaracje, Integer> {

    Optional<EdktDeklaracje> findByDklId(Integer dklId);

    @Query("select e from EdktDeklaracje e where e.dklPrcId = :prcId and e.dklDataOd>= :dateFrom and e.dklDataDo <= :dateTo " +
            "and e.dklStatus in (50) " +
            "order by e.dklDataOd desc")
    Optional<List<EdktDeklaracje>> findAllByDklPrcIdForYear(@Param("prcId") Integer prcId
            , @Param("dateFrom") Date dateFrom
            , @Param("dateTo") Date dateTo);

}
