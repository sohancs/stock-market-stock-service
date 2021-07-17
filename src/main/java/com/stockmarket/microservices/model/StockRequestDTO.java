package com.stockmarket.microservices.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockRequestDTO {
	@NotBlank(message = "Company code is required.")
	private String companyCode;
	@NotNull(message = "Stock price is required.")
	@DecimalMin(value = "0.00", inclusive = false, message = "Stock price must be fractional.")
	@Digits(integer = 10, fraction = 2, message = "Stock price should be rounded upto 2 digits. ex: 1000.12")
	private Double stockPrice;
}
