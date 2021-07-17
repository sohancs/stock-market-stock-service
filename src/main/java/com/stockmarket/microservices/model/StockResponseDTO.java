package com.stockmarket.microservices.model;

import java.util.List;

import lombok.Data;

@Data
public class StockResponseDTO {
	private String companyCode;
	private List<StockDTO> stockPrice;
	private StockAnalysis stockAnalysis;
}
