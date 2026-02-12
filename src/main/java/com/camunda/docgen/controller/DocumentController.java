package com.camunda.docgen.controller;

import com.camunda.docgen.model.DocumentTemplate;
import com.camunda.docgen.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentRepository repository;

    @GetMapping
    public List<DocumentTemplate> getAllDocuments() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentTemplate> getDocumentById(@PathVariable String id) {
        Optional<DocumentTemplate> document = repository.findById(id);
        return document.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public DocumentTemplate createDocument(@RequestBody DocumentTemplate document) {
        if (document.getId() == null || document.getId().isEmpty()) {
            document.setId(UUID.randomUUID().toString());
        }
        if (document.getUpdatedAt() == null) {
            document.setUpdatedAt(new Date());
        }
        return repository.save(document);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentTemplate> updateDocument(@PathVariable String id, @RequestBody DocumentTemplate documentDetails) {
        Optional<DocumentTemplate> optionalDocument = repository.findById(id);
        if (optionalDocument.isPresent()) {
            DocumentTemplate document = optionalDocument.get();
            document.setTitle(documentDetails.getTitle());
            document.setDescription(documentDetails.getDescription());
            document.setThumbnailUrl(documentDetails.getThumbnailUrl());
            document.setContent(documentDetails.getContent());
            document.setUpdatedAt(new Date());
            return ResponseEntity.ok(repository.save(document));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Autowired
    private com.camunda.docgen.service.PdfService pdfService;

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generatePdf(@PathVariable String id) {
        return repository.findById(id).map(doc -> {
            try {
                // Parse schema from content string, if it exists
                if (doc.getContent() == null || doc.getContent().isEmpty()) {
                    return ResponseEntity.badRequest().<byte[]>build();
                }

                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                java.util.Map<String, Object> schema = mapper.readValue(doc.getContent(), java.util.Map.class);
                
                // For now, use empty data. Future: accept data in body.
                java.util.Map<String, Object> data = java.util.Collections.emptyMap();
                
                byte[] pdfBytes = pdfService.generatePdf(schema, data);
                
                return ResponseEntity.ok()
                    .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getTitle() + ".pdf\"")
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.internalServerError().<byte[]>build();
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
