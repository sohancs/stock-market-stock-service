package com.stockmarket.microservices.model;

import lombok.Data;

@Data
public class StockAnalysis {
	private double minPrice;
	private double maxPrice;
	private double averagePrice;
}
