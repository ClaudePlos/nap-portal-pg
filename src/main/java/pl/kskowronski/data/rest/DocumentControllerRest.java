package pl.kskowronski.data.rest;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kskowronski.data.entity.portal.Document;
import pl.kskowronski.data.service.portal.DocumentRepo;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/document")
public class DocumentControllerRest {

    private final DocumentRepo documentRepo;

    @Autowired
    public DocumentControllerRest(DocumentRepo documentRepo) {
        this.documentRepo = documentRepo;
    }

    /* https://localhost:8181/api/document */
    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = documentRepo.findAll();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

/*
  Metoda: POST
  URL: http://localhost:8080/api/document
  Body (typ: JSON):
{
 "docType": "UM_PRAC",
 "dateCreate": "2023-05-16"
}
* */
    @PostMapping
    public ResponseEntity<Document> addDocument(@RequestBody Document document) {
        Document newDocument = documentRepo.save(document);
        return new ResponseEntity<>(newDocument, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable("id") Long id, @RequestBody Document document) {
        Optional<Document> existingDocument = documentRepo.findById(id);
        if (existingDocument.isPresent()) {
            Document updatedDocument = existingDocument.get();
            updatedDocument.setDocType(document.getDocType());
            //// ....
            Document savedDocument = documentRepo.save(updatedDocument);
            return new ResponseEntity<>(savedDocument, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable("id") Long id) {
        Optional<Document> existingDocument = documentRepo.findById(id);
        if (existingDocument.isPresent()) {
            documentRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
