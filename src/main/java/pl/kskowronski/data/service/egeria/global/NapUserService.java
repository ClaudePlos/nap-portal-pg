package pl.kskowronski.data.service.egeria.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.entity.egeria.global.NapUser;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class NapUserService  extends CrudService<NapUser, BigDecimal> {
    private NapUserRepo repo;

    public NapUserService(@Autowired NapUserRepo repo) {
        this.repo = repo;
    }

    @Override
    protected NapUserRepo getRepository() {
        return repo;
    }

    public Optional<NapUser> findByUsername(String username){return repo.findByUsername(username);};
}