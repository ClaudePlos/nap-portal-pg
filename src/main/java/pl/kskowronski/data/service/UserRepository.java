package pl.kskowronski.data.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kskowronski.data.entity.admin.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    Optional<List<User>> findByPrcDgKodEkOrderByPrcNazwisko(String dgKod);

    List<User> findByPrcDgKodEk(String dgKod, PageRequest req);

    @Query("SELECT u FROM User u WHERE u.prcDgKodEk = :dgKod and upper(u.prcNazwisko) like upper(:likeFilter)")
    Page<User> findByPrcDgKodEk2(String dgKod, String likeFilter, Pageable pageable);

}