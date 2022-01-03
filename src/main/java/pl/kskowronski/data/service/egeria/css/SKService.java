package pl.kskowronski.data.service.egeria.css;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.entity.admin.NppSkForSupervisor;
import pl.kskowronski.data.entity.egeria.css.SK;
import pl.kskowronski.data.service.admin.NppSkForSupervisorRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SKService extends CrudService<SK, Integer> {

    @Autowired
    NppSkForSupervisorRepo nppSkForSupervisorRepo;

    private List<SK> list  = new ArrayList<>();

    private SKRepo repo;

    public SKService(@Autowired SKRepo repo) {
        this.repo = repo;

        SK sk = new SK(100725, "AINF","Dział Informatyczny");
        SK sk1 = new SK(108469,"AKCF","Kadry Cała Firma");
        list.add(sk);
        list.add(sk1);
    }

    @Override
    protected SKRepo getRepository() {
        return repo;
    }


    public List<SK> findAll(){
        return list;
        //return repo.findAll();
    }

    public SK findBySkKod( String skKod){
        return list.stream().filter( item -> item.getSkKod().equals(skKod)).collect(Collectors.toList()).get(0);
        //return repo.findAll();
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
