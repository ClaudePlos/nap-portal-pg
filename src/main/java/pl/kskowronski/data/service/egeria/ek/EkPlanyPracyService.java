package pl.kskowronski.data.service.egeria.ek;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EkPlanyPracyService {

    private EkPlanyPracyRepo repo;

    public EkPlanyPracyService(@Autowired EkPlanyPracyRepo repo) {
        this.repo = repo;
    }

}
