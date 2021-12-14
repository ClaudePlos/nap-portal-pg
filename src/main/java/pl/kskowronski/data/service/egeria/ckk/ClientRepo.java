package pl.kskowronski.data.service.egeria.ckk;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kskowronski.data.entity.egeria.ckk.Client;

import java.util.Optional;

public interface ClientRepo  extends JpaRepository<Client, Integer> {

    Optional<Client> getClientByKlKod(Integer klKod);
}
