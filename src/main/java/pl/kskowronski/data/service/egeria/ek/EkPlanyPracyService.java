package pl.kskowronski.data.service.egeria.ek;

import org.springframework.stereotype.Service;
import pl.kskowronski.data.entity.egeria.ek.EkPlanyPracy;

@Service
public class EkPlanyPracyService {

    private EkPlanyPracyRepo repo;

    public EkPlanyPracyService(EkPlanyPracyRepo repo) {
        this.repo = repo;
    }

    public Integer getSpId( Integer ppId ) {
        EkPlanyPracy pp = repo.getById(ppId);
        return pp.getPpSpId();
    }

}
