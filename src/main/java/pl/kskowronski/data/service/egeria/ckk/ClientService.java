package pl.kskowronski.data.service.egeria.ckk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.entity.egeria.ckk.Client;
import pl.kskowronski.data.service.egeria.ek.AbsenceRepo;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ClientService extends CrudService<Client, Integer> {

    private ClientRepo repo;

    public ClientService(@Autowired ClientRepo repo) {
        this.repo = repo;
    }

    @Override
    protected ClientRepo getRepository() {
        return repo;
    }

    public Optional<Client> getClientByKlKod(Integer klKod){ return repo.getClientByKlKod(klKod); };

}