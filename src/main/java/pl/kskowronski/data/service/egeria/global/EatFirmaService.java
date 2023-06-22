package pl.kskowronski.data.service.egeria.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.entity.egeria.global.EatFirma;

import java.util.List;
import java.util.Optional;

@Service
public class EatFirmaService extends CrudService<EatFirma, Integer> {

    private EatFirmaRepo repo;

    public EatFirmaService(@Autowired EatFirmaRepo repo) {
        this.repo = repo;
    }

    @Override
    protected EatFirmaRepo getRepository() {
        return repo;
    }

    public Optional<EatFirma> findById(Integer frmId){ return repo.findById(frmId);}

    public List<EatFirma> findAllF() {
        return null;
    }

}