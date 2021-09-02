package com.stockmarket.microservices.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockmarket.microservices.model.StockDTO;

@RestController
public class KafkaConsumerController {

	List<String> messages = new ArrayList<>();

	StockDTO stockDTO = null;

	@GetMapping("/consumesStringMessages")
	public List<String> consumesStringMessages() {
		return messages;
	}

	@GetMapping("/consumesJsonMessages")
	public StockDTO consumesJsonMessages() {
		return stockDTO;
	}

	@KafkaListener(groupId = "group-1", topics = "tester-topic", containerFactory = "kafkaListenerContainerFactory")
	public List<String> getMsgFromTopic(String data) {
		messages.add(data);
		return messages;
	}

	/*
	 * @KafkaListener(groupId = "group-2", topics = "stock-service-topic",
	 * containerFactory = "stockKafkaListenerContainerFactory") public StockDTO
	 * getJsonFromTopic(StockDTO dto) { stockDTO = dto; return stockDTO; }
	 */
}
