package pl.kskowronski.data.service.portal;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kskowronski.data.entity.portal.Document;

public interface DocumentRepo extends JpaRepository<Document, Long> {
}
