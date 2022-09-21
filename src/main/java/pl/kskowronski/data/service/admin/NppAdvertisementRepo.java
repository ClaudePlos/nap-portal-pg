package pl.kskowronski.data.service.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kskowronski.data.entity.admin.NppAdvertisement;

public interface NppAdvertisementRepo extends JpaRepository<NppAdvertisement, Integer> {
}
