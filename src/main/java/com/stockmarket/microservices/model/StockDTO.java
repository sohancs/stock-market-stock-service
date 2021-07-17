package com.stockmarket.microservices.model;

import lombok.Data;

@Data
public class StockDTO {
	private Double stockPrice;
	private String createdTimestamp;
}
