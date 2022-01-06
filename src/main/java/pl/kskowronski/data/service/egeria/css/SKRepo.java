package pl.kskowronski.data.service.egeria.css;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kskowronski.data.entity.egeria.css.SK;


public interface SKRepo extends JpaRepository<SK, Integer> {

    SK findBySkKod( String skKod);

    @Query("SELECT s FROM SK s WHERE upper(s.skKod) like upper(:likeFilter)")
    Page<SK> findAllWithPagination(String likeFilter, Pageable pageable);

}
