package pl.kskowronski.data.service.egeria.ek;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kskowronski.data.entity.egeria.ek.RodzajWymiaru;

import java.util.Optional;


public interface RodzajWymiaruRepo extends JpaRepository<RodzajWymiaru, String> {

    Optional<RodzajWymiaru> findByRwyDgKod( String dgKod );
}
