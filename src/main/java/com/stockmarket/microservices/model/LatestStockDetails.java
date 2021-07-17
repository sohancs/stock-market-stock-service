package com.stockmarket.microservices.model;


import lombok.Data;

@Data
public class LatestStockDetails {
	private String companyCode;
	private Double stockPrice;	
}
