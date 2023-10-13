package com.multipolar.bootcamp.gatewayexercise.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ProductDTO {
    private String id;
    private String productName;
    private ProductTypeDTO productType;
    private double interestRate;
    private double minimumBalance;
    private double maximumLoanAmount;
    private String termsAndConditions;
    private LocalDateTime dateOfCreation = LocalDateTime.now();
}
