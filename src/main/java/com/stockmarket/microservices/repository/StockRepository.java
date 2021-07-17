package com.stockmarket.microservices.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.stockmarket.microservices.model.StockDetails;

@Repository
public interface StockRepository extends MongoRepository<StockDetails, String> {

	@Query(value = "{companyCode : ?0, createdTimestamp : {$gte : ?1, $lte : ?2}}")
	List<StockDetails> findAllByCompanyCodeAndCreatedTimestampBetween(String companyCode, Date startDate,
			Date endDate);

	Optional<StockDetails> findFirstByCompanyCodeOrderByCreatedTimestampDesc(String companyCode);
	
	Integer deleteByCompanyCode(String companyCode);
	 
}
