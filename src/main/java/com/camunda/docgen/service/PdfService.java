package com.camunda.docgen.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PdfService {

    @Value("${pdf.engine.url:http://localhost:3001/generate-pdf}")
    private String pdfEngineUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public byte[] generatePdf(Map<String, Object> schema, Map<String, Object> data) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("schema", schema);
            requestBody.put("data", data);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            return restTemplate.postForObject(pdfEngineUrl, request, byte[].class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage());
        }
    }
}
