package com.stockmarket.microservices.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CompanyDetails {
	private String companyCode;
	private String companyName;
	private String companyCeo;
	private BigDecimal turnOver;
	private String stockExchange;
	private String companyWebsite;
	private Double latestStockPrice;
}
