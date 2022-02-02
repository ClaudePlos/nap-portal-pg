package pl.kskowronski.data.service.egeria.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.entity.egeria.css.SK;
import pl.kskowronski.data.entity.egeria.global.NapUser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class NapUserService extends CrudService<NapUser, Integer> {

    private NapUserRepo repo;

    public NapUserService(@Autowired NapUserRepo repo) {
        this.repo = repo;
    }

    @Override
    protected NapUserRepo getRepository() {
        return repo;
    }

    public Optional<NapUser> findByUsername(String username){return repo.findByUsername(username);}

    public Optional<NapUser> findById(Integer prcId){ return repo.findById(prcId); }

    public Stream<NapUser> findAll(String filterString, int page, int pageSize){
        String likeFilter = "%" + filterString + "%";
        Stream<NapUser> list = repo.findAllWithPagination(likeFilter, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "username"))).stream();
        return list;
    }
}