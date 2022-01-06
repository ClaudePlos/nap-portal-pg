package pl.kskowronski.data.service.egeria.css;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kskowronski.data.entity.egeria.css.SK;


public interface SKRepo extends JpaRepository<SK, Integer> {

    SK findBySkKod( String skKod);

}
