package com.stockmarket.microservices.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.stockmarket.microservices.model.StockDTO;

@RestController
public class KafkaProducerController {

	@Autowired
	private KafkaTemplate<String , Object> kafkaTemplate;
	
	private String topic = "tester-topic";
	
	@GetMapping("/publish/{message}")
	public String publishMessage(@PathVariable String message) {
		kafkaTemplate.send(topic,"Message Sent "+ message);
		return "Message : "+message + " publish from kafka producer";
	}
	
	@GetMapping("/publishJson/{stockPrice}")
	public String publishJson(@PathVariable Double stockPrice) {
		StockDTO stockDTO = new StockDTO();
		stockDTO.setStockPrice(stockPrice);
		stockDTO.setCreatedTimestamp(String.valueOf(System.currentTimeMillis()));
		kafkaTemplate.send(topic,stockDTO);
		return "Json : publish from kafka producer";
	}
	
}
