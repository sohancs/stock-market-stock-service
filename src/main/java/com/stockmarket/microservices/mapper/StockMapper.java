package com.stockmarket.microservices.mapper;

import org.springframework.stereotype.Component;

import com.stockmarket.microservices.model.StockDetails;
import com.stockmarket.microservices.model.StockRequestDTO;

@Component
public class StockMapper {

	
	public StockDetails mapStockRequestDtoToEntity(StockRequestDTO requestDTO) {
		StockDetails stockDetails = new StockDetails();
		stockDetails.setCompanyCode(requestDTO.getCompanyCode());
		stockDetails.setStockPrice(requestDTO.getStockPrice());
		return stockDetails;
	}
}
