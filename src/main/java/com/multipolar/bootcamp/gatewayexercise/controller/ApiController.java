package com.multipolar.bootcamp.gatewayexercise.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.multipolar.bootcamp.gatewayexercise.dto.ProductDTO;
import com.multipolar.bootcamp.gatewayexercise.dto.ErrorMessageDTO;
import com.multipolar.bootcamp.gatewayexercise.kafka.AccessLog;
import com.multipolar.bootcamp.gatewayexercise.service.AccessLogService;
import com.multipolar.bootcamp.gatewayexercise.util.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    private static final String PRODUCT_URL = "http://localhost:8081/products";
    private final RestTemplateUtil restTemplateUtil;
    private final ObjectMapper objectMapper;
    private final AccessLogService logService;

    @Autowired //supaya dibuat objectnya oleh spring, digunakan membuat object
    public ApiController(RestTemplateUtil restTemplateUtil, ObjectMapper objectMapper, AccessLogService logService) {
        this.restTemplateUtil = restTemplateUtil;
        this.objectMapper = objectMapper;
        this.logService = logService;
    }

    @GetMapping("/getProducts")
    public ResponseEntity<?> getProducts() throws JsonProcessingException {
        //akses api products
        try {
            ResponseEntity<?> response = restTemplateUtil.getList(PRODUCT_URL, new ParameterizedTypeReference<Object>() {});
            // Kirim access log
            AccessLog accessLog = new AccessLog(
                    "GET",
                    "mongodb://localhost:27017/product",
                    response.getStatusCode().value(),
                    LocalDateTime.now(),
                    response.getBody().toString()
            );
            logService.logAccess(accessLog);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException ex) {
            List<ErrorMessageDTO> errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), List.class);
            AccessLog accessLog = new AccessLog(
                    "GET",
                    "mongodb://localhost:27017/product",
                    ex.getStatusCode().value(),
                    LocalDateTime.now(),
                    ex.getResponseBodyAsString()
            );
            logService.logAccess(accessLog);
            return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
        }
    }

    @PostMapping("/createProduct")
    public ResponseEntity<?> postProduct(@RequestBody ProductDTO productDTO) throws JsonProcessingException {
        try {
            ResponseEntity<?> response = restTemplateUtil.post(PRODUCT_URL, productDTO, ProductDTO.class);
            AccessLog accessLog = new AccessLog(
                    "POST",
                    "mongodb://localhost:27017/product",
                    response.getStatusCode().value(),
                    LocalDateTime.now(),
                    response.getBody().toString()
            );
            logService.logAccess(accessLog);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }catch(HttpClientErrorException ex) {
            List<ErrorMessageDTO> errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), List.class);
            AccessLog accessLog = new AccessLog(
                    "POST",
                    "mongodb://localhost:27017/product",
                    ex.getStatusCode().value(),
                    LocalDateTime.now(),
                    ex.getResponseBodyAsString()
            );
            logService.logAccess(accessLog);
            return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
        }

    }

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable String id) throws JsonProcessingException {
        try {
            ResponseEntity<?> response = restTemplateUtil.put(PRODUCT_URL +"/"+ id, productDTO, ProductDTO.class);
            AccessLog accessLog = new AccessLog(
                    "PUT",
                    "mongodb://localhost:27017/product",
                    response.getStatusCode().value(),
                    LocalDateTime.now(),
                    response.getBody().toString()
            );
            logService.logAccess(accessLog);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }catch(HttpClientErrorException ex) {
            List<ErrorMessageDTO> errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), List.class);
            AccessLog accessLog = new AccessLog(
                    "PUT",
                    "mongodb://localhost:27017/product",
                    ex.getStatusCode().value(),
                    LocalDateTime.now(),
                    ex.getResponseBodyAsString()
            );
            logService.logAccess(accessLog);
            return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
        }

    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) throws JsonProcessingException {
        try {
            ResponseEntity<?> response = restTemplateUtil.delete(PRODUCT_URL +"/"+ id);
            AccessLog accessLog = new AccessLog(
                    "DELETE",
                    "mongodb://localhost:27017/product",
                    200,
                    LocalDateTime.now(),
                    "delete successfully"
            );
            logService.logAccess(accessLog);
            return ResponseEntity.noContent().build();
        }catch(HttpClientErrorException ex) {
            List<ErrorMessageDTO> errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), List.class);
            AccessLog accessLog = new AccessLog(
                    "DELETE",
                    "mongodb://localhost:27017/product",
                    400,
                    LocalDateTime.now(),
                    "delete failed"
            );
            logService.logAccess(accessLog);
            return ResponseEntity.noContent().build();
        }

    }
}
