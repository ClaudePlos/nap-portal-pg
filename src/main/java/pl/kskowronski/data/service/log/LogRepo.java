package pl.kskowronski.data.service.log;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kskowronski.data.entity.log.Log;

public interface LogRepo extends JpaRepository<Log, Integer> {
}
