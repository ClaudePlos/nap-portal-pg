package pl.kskowronski.data.service.egeria.css;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.entity.admin.NppSkForSupervisor;
import pl.kskowronski.data.entity.egeria.css.SK;
import pl.kskowronski.data.service.admin.NppSkForSupervisorRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SKService extends CrudService<SK, Integer> {

    @Autowired
    NppSkForSupervisorRepo nppSkForSupervisorRepo;

    //private List<SK> list  = new ArrayList<>();

    private SKRepo repo;

    public SKService(@Autowired SKRepo repo) {
        this.repo = repo;

//        SK sk = new SK(100725, "AINF","Dział Informatyczny");
//        SK sk1 = new SK(108469,"AKCF","Kadry Cała Firma");
//        list.add(sk);
//        list.add(sk1);
    }

    @Override
    protected SKRepo getRepository() {
        return repo;
    }

    public class PageResponse<T> {
        public List<T> content;
        public long size;

        public List<T> getContent() {
            return content;
        }

        public void setContent(List<T> content) {
            this.content = content;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }
    }

    public PageResponse<SK> getPage(int page, int size) {
        var dbPage = repo.findAll(PageRequest.of(page, size));

        var response = new PageResponse<SK>();
        response.content = dbPage.getContent();
        response.size = dbPage.getTotalElements();

        return response;
    }


    public Stream<SK> findAll(String filterString, int page, int pageSize){
        String likeFilter = "%" + filterString + "%";
        Stream<SK> list = repo.findAllWithPagination(likeFilter, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "skKod"))).stream();
        return list;
    }
    public SK findBySkKod( String skKod){
       // return list.stream().filter( item -> item.getSkKod().equals(skKod)).collect(Collectors.toList()).get(0);
        return repo.findBySkKod(skKod);
    }

    public List<SK> findSkForSupervisor(Integer prcId){
        List<SK> listSk = new ArrayList<>();
        List<NppSkForSupervisor> listSkForSupervisor = nppSkForSupervisorRepo.findSkForSupervisor(prcId);
        listSkForSupervisor.forEach( item ->{
            SK sk = new SK();
            sk.setSkId(item.getSkId());
            sk.setSkKod(item.getSkKod());
            listSk.add(sk);
        });
        return listSk;
    }


}
