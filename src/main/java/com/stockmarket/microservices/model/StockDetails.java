package com.stockmarket.microservices.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value = "stock_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockDetails {
	@Id
	private String id;
	@Field("companyCode")
	private String companyCode;
	@Field("stockPrice")
	private Double stockPrice;
	@CreatedDate
	private LocalDateTime createdTimestamp;
}
