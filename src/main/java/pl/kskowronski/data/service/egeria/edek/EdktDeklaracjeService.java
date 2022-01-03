package pl.kskowronski.data.service.egeria.edek;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.MapperDate;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.egeria.eDek.EdktDeklaracje;
import pl.kskowronski.data.entity.egeria.eDek.EdktDeklaracjeDTO;
import pl.kskowronski.data.service.egeria.ek.ZatrudnienieService;
import pl.kskowronski.data.service.egeria.global.EatFirmaRepo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EdktDeklaracjeService extends CrudService<EdktDeklaracje, Integer> {

    private EdktDeklaracjeRepo repo;

    @Autowired
    private EatFirmaRepo eatFirmaRepo;

    @Autowired
    private ZatrudnienieService zatrudnienieService;

    public EdktDeklaracjeService(@Autowired EdktDeklaracjeRepo repo) {
        this.repo = repo;
    }

    SimpleDateFormat dtYYYY = new SimpleDateFormat("yyyy");

    @Override
    protected EdktDeklaracjeRepo getRepository() {
        return repo;
    }

    MapperDate mapperDate = new MapperDate();

    public Optional<EdktDeklaracje> findByDklId(Integer dklId){
        //repo.setConsolidate();
        return repo.findByDklId(dklId);
    };

    public Optional<List<EdktDeklaracjeDTO>> findAllByDklPrcId(Integer prcId, String year) throws ParseException {
        //repo.setConsolidate();
        Optional<List<EdktDeklaracje>> listDek = repo.findAllByDklPrcIdForYear(prcId, mapperDate.dtYYYYMMDD.parse(year+"-01-01")
                , mapperDate.dtYYYYMMDD.parse(year+"-12-31"));
        Optional<List<EdktDeklaracjeDTO>> listDekDTO = Optional.of(new ArrayList<>());
        if (listDek.isPresent()){
            listDek.get().stream().forEach( item -> listDekDTO.get().add( mapperEdktDeklaracje(item)));
        }
        return listDekDTO;
    }

    public Optional<List<EdktDeklaracjeDTO>> getListPit11ForSupervisor(String year, Integer skId) throws ParseException {
        Optional<List<EdktDeklaracjeDTO>> listDekForSK = Optional.of(new ArrayList<>());
        //repo.setConsolidate();
        //1. find workers on SK in year
        List<User> listWorker = zatrudnienieService.getListWorkerOnSKinYear(skId,year);
        //2. find dek and put
        listWorker.forEach(item -> {
            try {
                Optional<List<EdktDeklaracjeDTO>> listDek = this.findAllByDklPrcId(item.getPrcId(), year);
                if (listDek.isPresent()) {
                    listDek.get().forEach(itemDek -> {
                        itemDek.setDklPrcImie(item.getPrcImie());
                        itemDek.setDklPrcNazwisko(item.getPrcNazwisko());
                        listDekForSK.get().add(itemDek);
                    });
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return listDekForSK;
    }

    private EdktDeklaracjeDTO mapperEdktDeklaracje( EdktDeklaracje dek){
        EdktDeklaracjeDTO dekDTO = new EdktDeklaracjeDTO();
        dekDTO.setDklId(dek.getDklId());
        dekDTO.setDklFrmId(dek.getDklFrmId());
        dekDTO.setDklPrcId(dek.getDklPrcId());
        dekDTO.setDklDataOd(dek.getDklDataOd());
        dekDTO.setDklDataDo(dek.getDklDataDo());
        dekDTO.setDklStatus(dek.getDklStatus());
        dekDTO.setDklTdlKod(dek.getDklTdlKod());
        dekDTO.setDklXmlVisual(dek.getDklXmlVisual());
        dekDTO.setDklFrmNazwa( eatFirmaRepo.findById(dek.getDklFrmId()).get().getFrmNazwa() );
        dekDTO.setDklYear(dtYYYY.format(dek.getDklDataOd()));
        return dekDTO;
    }



}
