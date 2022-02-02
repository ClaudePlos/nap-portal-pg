package pl.kskowronski.data.service.egeria.global;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kskowronski.data.entity.egeria.css.SK;
import pl.kskowronski.data.entity.egeria.global.NapUser;

import java.math.BigDecimal;
import java.util.Optional;

public interface NapUserRepo extends JpaRepository<NapUser, Integer>  {
    public Optional<NapUser> findByUsername(String username);

    @Query("SELECT u FROM NapUser u WHERE upper(u.username) like upper(:likeFilter)")
    Page<NapUser> findAllWithPagination(String likeFilter, Pageable pageable);
}
