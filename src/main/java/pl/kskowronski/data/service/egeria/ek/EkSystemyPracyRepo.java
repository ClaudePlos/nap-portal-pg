package pl.kskowronski.data.service.egeria.ek;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kskowronski.data.entity.egeria.ek.EkSystemyPracy;

public interface EkSystemyPracyRepo extends JpaRepository<EkSystemyPracy, String> {
}
