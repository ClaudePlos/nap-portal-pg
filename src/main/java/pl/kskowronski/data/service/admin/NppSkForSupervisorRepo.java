package pl.kskowronski.data.service.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kskowronski.data.entity.admin.NppSkForSupervisor;

import java.util.List;

public interface NppSkForSupervisorRepo extends JpaRepository<NppSkForSupervisor, Integer> {

    List<NppSkForSupervisor> findAllBySkKod(String skKod);

    @Query("select s from NppSkForSupervisor s where s.prcId = :prcId")
    List<NppSkForSupervisor> findSkForSupervisor(@Param("prcId") Integer prcId);


}
