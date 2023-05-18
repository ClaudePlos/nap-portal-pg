package pl.kskowronski.data.service.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.entity.log.LogPit11;
import pl.kskowronski.data.entity.portal.Document;
import pl.kskowronski.data.service.log.LogPit11Repo;

@Service
public class DocumentService extends CrudService<Document, Long> {

    private DocumentRepo repo;

    public DocumentService(@Autowired DocumentRepo repo) {
        this.repo = repo;
    }

    @Override
    protected DocumentRepo getRepository() {
        return repo;
    }

    public void save(Document doc){ repo.save(doc);}

}
