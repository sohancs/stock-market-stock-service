package com.stockmarket.microservices.model;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
	private HttpStatus httpStatus;
	private String errorMesssage;
	private String sourceClass;
	private List<String> errors;
}
