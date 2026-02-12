package com.camunda.docgen;

import com.camunda.docgen.model.DocumentTemplate;
import com.camunda.docgen.repository.DocumentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;

@SpringBootApplication
public class DocGenBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocGenBackendApplication.class, args);
	}


}
