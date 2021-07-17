package com.stockmarket.microservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.stockmarket.microservices.exception.FeignExceptionDecoder;

import feign.codec.ErrorDecoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.stockmarket.microservices.*")
@EnableMongoAuditing
@EnableFeignClients("com.stockmarket.microservices")
@EnableSwagger2
public class StockServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockServiceApplication.class, args);
	}
	
	@Bean
    public ErrorDecoder errorDecoder() {
        return new FeignExceptionDecoder();
    }

}
