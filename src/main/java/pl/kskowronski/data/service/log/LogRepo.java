package pl.kskowronski.data.service.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kskowronski.data.entity.log.Log;

import java.util.List;

public interface LogRepo extends JpaRepository<Log, Integer> {

    @Query("select new pl.kskowronski.data.entity.log.Log(max(l.id), l.prcId, l.event, max(l.auditDc), l.description) from Log l group by l.prcId, l.event, l.description")
    List<Log> findAllGroupByPrcId();

}
