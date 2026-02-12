package com.camunda.docgen.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "document_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentTemplate {

    @Id
    private String id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private Date updatedAt;
    
    @jakarta.persistence.Column(columnDefinition = "TEXT")
    private String content;
}
