package com.camunda.docgen.repository;

import com.camunda.docgen.model.DocumentTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentTemplate, String> {
}
