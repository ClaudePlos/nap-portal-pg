package pl.kskowronski.data.service.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.entity.admin.NppAdvertisement;

import java.util.List;

@Service
public class NppAdvertisementService extends CrudService<NppAdvertisement, Integer>  {

    private NppAdvertisementRepo repo;

    public NppAdvertisementService(@Autowired NppAdvertisementRepo repo) {
        this.repo = repo;
    }

    @Override
    protected NppAdvertisementRepo getRepository() {
        return repo;
    }

    public List<NppAdvertisement> findAll() { return repo.findAll(Sort.by(Sort.Direction.DESC, "lp")); }

    public void save(NppAdvertisement nppAdvertisement) {
        repo.save(nppAdvertisement);
    }

    public void deleteById(Integer id) {
        repo.deleteById(id);
    }
}
