package pl.kskowronski.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kskowronski.data.entity.admin.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    Optional<List<User>> findByPrcDgKodEkOrderByPrcNazwisko(String dgKod);
}