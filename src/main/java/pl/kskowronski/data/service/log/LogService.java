package pl.kskowronski.data.service.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.entity.log.Log;

@Service
public class LogService extends CrudService<Log, Integer> {

    private LogRepo repo;

    public LogService(@Autowired LogRepo repo) {
        this.repo = repo;
    }

    @Override
    protected LogRepo getRepository() {
        return repo;
    }

    public void save(Log log){ repo.save(log);}
}
