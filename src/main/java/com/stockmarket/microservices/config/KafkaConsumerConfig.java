package com.stockmarket.microservices.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import com.stockmarket.microservices.model.StockDTO;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

	// Config for plain text data

	@Bean
	public ConsumerFactory<String, Object> consumerFactory() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, "group-1");
		return new DefaultKafkaConsumerFactory<String, Object>(configs);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	// Config for json data

	/*
	 * @Bean public ConsumerFactory<String, StockDTO> stockConsumerFactory() {
	 * Map<String, Object> configs = new HashMap<>();
	 * configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	 * configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
	 * StringDeserializer.class);
	 * configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
	 * JsonDeserializer.class); configs.put(ConsumerConfig.GROUP_ID_CONFIG,
	 * "group-2"); configs.put(JsonDeserializer.TRUSTED_PACKAGES,
	 * "com.stockmarket.microservices.model.StockDTO"); return new
	 * DefaultKafkaConsumerFactory<String, StockDTO>(configs, new
	 * StringDeserializer(), new JsonDeserializer<>(StockDTO.class)); }
	 * 
	 * @Bean public ConcurrentKafkaListenerContainerFactory<String, StockDTO>
	 * stockKafkaListenerContainerFactory() {
	 * ConcurrentKafkaListenerContainerFactory<String, StockDTO> factory = new
	 * ConcurrentKafkaListenerContainerFactory<>();
	 * factory.setConsumerFactory(stockConsumerFactory()); return factory; }
	 */

}
