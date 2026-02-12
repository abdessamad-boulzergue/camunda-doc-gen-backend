package com.camunda.docgen.config;

import com.camunda.docgen.model.DocumentTemplate;
import com.camunda.docgen.repository.DocumentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Date;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(DocumentRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                String demoContent = "{\"schemaVersion\":4,\"exporter\":{\"name\":\"form-js\",\"version\":\"0.1.0\"},\"components\":[{\"key\":\"firstName\",\"label\":\"First Name\",\"type\":\"textfield\",\"id\":\"Field_firstName\"},{\"key\":\"lastName\",\"label\":\"Last Name\",\"type\":\"textfield\",\"id\":\"Field_lastName\"},{\"key\":\"email\",\"label\":\"Email\",\"type\":\"textfield\",\"id\":\"Field_email\"},{\"key\":\"department\",\"label\":\"Department\",\"type\":\"textfield\",\"id\":\"Field_department\"}],\"type\":\"default\"}";
                
                repository.saveAll(Arrays.asList(
                    new DocumentTemplate("1", "Data Binding Demo (Employees)", "Select \"John Doe\" or \"Jane Smith\" in Preview to see data binding.", null, new Date(), demoContent),
                    new DocumentTemplate("2", "Invoice Request", "Submit an invoice for payment", null, new Date(System.currentTimeMillis() - 86400000), null),
                    new DocumentTemplate("3", "Leave Application", "Apply for annual leave", null, new Date(System.currentTimeMillis() - 172800000), null)
                ));
            }
        };
    }
}
