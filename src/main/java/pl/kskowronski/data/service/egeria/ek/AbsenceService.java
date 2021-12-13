package pl.kskowronski.data.service.egeria.ek;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.MapperDate;
import pl.kskowronski.data.entity.egeria.ek.AbsenceDTO;
import pl.kskowronski.data.entity.egeria.ek.Absencja;
import pl.kskowronski.data.entity.egeria.ek.Zwolnienie;
import pl.kskowronski.data.entity.egeria.global.EatFirma;
import pl.kskowronski.data.service.egeria.global.EatFirmaRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AbsenceService extends CrudService<Absencja, Integer> {

    private AbsenceRepo repo;

    public AbsenceService(@Autowired AbsenceRepo repo) {
        this.repo = repo;
    }


    @Autowired
    private ZwolnienieRepo zwolnienieRepo;

    @Autowired
    private EatFirmaRepo eatFirmaRepo;

    @Autowired
    private AbsenceTypeRepo absenceTypeRepo;

    @Override
    protected AbsenceRepo getRepository() {
        return repo;
    }

    MapperDate mapperDate = new MapperDate();

    public Optional<List<AbsenceDTO>> findAllByAbPrcIdForYear(Integer prcId, String year) throws Exception {
        //consolidationService.setConsolidateCompany();
        Optional<List<Absencja>> listAbsence = repo.findAllByAbPrcIdForYear(prcId
                , mapperDate.dtYYYYMMDD.parse(year+"-01-01")
                , mapperDate.dtYYYYMMDD.parse(year+"-12-31"));
        Optional<List<AbsenceDTO>> listAbsenceDTO = Optional.of(new ArrayList<>());
        if (listAbsence.isPresent()){
            listAbsence.get().stream().forEach( item -> listAbsenceDTO.get().add( mapperAbsence(item)));
        }
        return listAbsenceDTO;
    }


    private AbsenceDTO mapperAbsence( Absencja ab){
        AbsenceDTO absence = new AbsenceDTO();
        absence.setAbId(ab.getAbId());
        absence.setAbZwolId(ab.getAbZwolId());
        absence.setAbRdaId(ab.getAbRdaId());
        absence.setAbPrcId(ab.getAbPrcId());
        absence.setAbDataOd(ab.getAbDataOd());
        absence.setAbDataDo(ab.getAbDataDo());
        absence.setAbKodFunduszu(ab.getAbKodFunduszu());
        absence.setAbDniWykorzystane(ab.getAbDniWykorzystane());
        absence.setAbGodzinyWykorzystane(ab.getAbGodzinyWykorzystane());
        absence.setAbFanulowana(ab.getAbFanulowana());
        Zwolnienie zwol = zwolnienieRepo.findById(ab.getAbZwolId()).get();
        EatFirma firma = eatFirmaRepo.findById( zwol.getZwolFrmId() ).get();
        absence.setAbFrmName( firma.getFrmNazwa() );
        absence.setAbTypeOfAbsence( absenceTypeRepo.findById(ab.getAbRdaId()).get().getRdaNazwa() );
        return absence;
    }

}
