package pl.kskowronski.data.service.egeria.ek;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kskowronski.data.entity.egeria.ek.Absencja;
import pl.kskowronski.data.entity.egeria.ek.Zatrudnienie;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ZatrudnienieRepo extends JpaRepository<Zatrudnienie, Integer> {

    @Query("select z from Zatrudnienie z where z.zatPrcId = :prcId and z.zatTypUmowy = 0 " +
            "order by z.zatDataZmiany desc")
    Optional<List<Zatrudnienie>> findAllByPrcId(@Param("prcId") Integer prcId);


}
