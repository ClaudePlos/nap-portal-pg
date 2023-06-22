package pl.kskowronski.data.service.egeria.ek;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EkSystemyPracyService {

    private EkSystemyPracyRepo repo;

    public EkSystemyPracyService(@Autowired EkSystemyPracyRepo repo) {
        this.repo = repo;
    }

    public Double getNormaDobowaForSpId( Integer spId ) {
        return repo.getById(spId).getSpDCzasWartosc();
    }

    public Double getNormaTygodniowaForSpId( Integer spId ) {
        return repo.getById(spId).getSpTCzasWartosc();
    }

}
