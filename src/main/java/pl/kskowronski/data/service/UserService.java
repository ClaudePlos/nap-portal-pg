package pl.kskowronski.data.service;

import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import pl.kskowronski.data.entity.admin.User;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class UserService extends CrudService<User, Integer> {

    private UserRepository repository;

    public UserService(@Autowired UserRepository repository) {
        this.repository = repository;
    }

    @Override
    protected UserRepository getRepository() {
        return repository;
    }

    public Optional<User> findById(Integer prcId){ return repository.findById(prcId); }

    public Optional<User> findByUsername(String username){ return Optional.ofNullable(repository.findByUsername(username));}

    public Stream<User> findByPrcDgKodEk(String dgKod, String filterString, int page, int pageSize) {
        String likeFilter = "%" + filterString + "%";
        Stream<User> list = repository.findByPrcDgKodEk2( dgKod, likeFilter
                , PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "prcNazwisko") ) ).stream();
        return list;
    }



    public  boolean isMobileDevice() {
        WebBrowser webBrowser = VaadinSession.getCurrent().getBrowser();
        return webBrowser.isAndroid() || webBrowser.isIPhone() || webBrowser.isWindowsPhone();
    }

}
