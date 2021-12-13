package pl.kskowronski.data.service.egeria.global;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kskowronski.data.entity.egeria.global.EatFirma;

import java.util.Optional;

public interface EatFirmaRepo extends JpaRepository<EatFirma, Integer> {

    Optional<EatFirma> findById(Integer frmId);

}
