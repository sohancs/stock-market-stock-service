package com.stockmarket.microservices.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.stockmarket.microservices.exception.CompanyNotFoundException;
import com.stockmarket.microservices.exception.StockNotFoundException;
import com.stockmarket.microservices.mapper.StockMapper;
import com.stockmarket.microservices.model.CompanyDetails;
import com.stockmarket.microservices.model.LatestStockDetails;
import com.stockmarket.microservices.model.StockAnalysis;
import com.stockmarket.microservices.model.StockDTO;
import com.stockmarket.microservices.model.StockDetails;
import com.stockmarket.microservices.model.StockRequestDTO;
import com.stockmarket.microservices.model.StockResponseDTO;
import com.stockmarket.microservices.proxy.ManageCompanyServiceProxy;
import com.stockmarket.microservices.repository.StockRepository;

@Service
public class StockService {

	Logger LOGGER = LoggerFactory.getLogger(StockService.class);

	@Autowired
	private StockRepository repo;
	@Autowired
	private StockMapper mapper;
	@Autowired
	ManageCompanyServiceProxy companyProxy;
	@Autowired
	private MongoTemplate mongoTemplate;

	public boolean addStockDetails(StockRequestDTO requestDTO) throws CompanyNotFoundException {
		LOGGER.info("StockRequestDTO - {}", requestDTO);

		if (!fetchCompanyDetailsByCode(requestDTO.getCompanyCode()).isPresent()) {
			LOGGER.error("Company with code {} not registered in the system", requestDTO.getCompanyCode());
			throw new CompanyNotFoundException(
					"Company with company-code " + requestDTO.getCompanyCode() + " not registered into the system.");
		}

		StockDetails stockDetails = mapper.mapStockRequestDtoToEntity(requestDTO);
		LOGGER.info("Adding stock price into the database for company with code {}.", requestDTO.getCompanyCode());
		repo.save(stockDetails);
		LOGGER.info("Stock price added successfully...");
		return true;
	}

	private Optional<CompanyDetails> fetchCompanyDetailsByCode(String companyCode) throws CompanyNotFoundException {
		LOGGER.info("Fetching company details for company code {} through external service ", companyCode);
		ResponseEntity<CompanyDetails> companyDtlResponseEntity = companyProxy.fetchCompanyDetails(companyCode);
		CompanyDetails companyDetails = null;
		if (companyDtlResponseEntity.getStatusCode() == HttpStatus.OK) {
			companyDetails = companyDtlResponseEntity.getBody();
		}

		return Optional.ofNullable(companyDetails);
	}

	public StockResponseDTO fetchStockPrices(String companyCode, String startDate, String endDate)
			throws CompanyNotFoundException {
		LOGGER.info("inside fetchStockPrices method with companyCode : {}, startDate : {}, endDate : {}", companyCode,
				startDate, endDate);
		StockResponseDTO responseDTO = new StockResponseDTO();
		responseDTO.setCompanyCode(companyCode);

		if (!fetchCompanyDetailsByCode(companyCode).isPresent()) {
			LOGGER.error("Company with code {} not registered into the system", companyCode);

			throw new CompanyNotFoundException(
					"Company with company-code " + companyCode + " not registered into the system.");
		}

		// DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		Date start_date = stringToDateConverter(startDate, "STARTDATE");
		Date end_date = stringToDateConverter(endDate, "ENDDATE");

		LOGGER.info("Start date : {}, End date : {}", start_date, end_date);

		List<StockDetails> stockDetails = repo.findAllByCompanyCodeAndCreatedTimestampBetween(companyCode, start_date,
				end_date);
		List<StockDTO> stockDTOs = null;
		if (!CollectionUtils.isEmpty(stockDetails)) {
			stockDTOs = stockDetails.stream()
					.map(stock -> {
						StockDTO stockDTO = new StockDTO();
						stockDTO.setStockPrice(stock.getStockPrice());
						stockDTO.setCreatedTimestamp(stock.getCreatedTimestamp().toString());
						return stockDTO;
					})
					.collect(Collectors.toList());
		}
		
		StockAnalysis stockAnalysis = getstockAnalysis(companyCode, start_date, end_date);

		responseDTO.setStockPrice(stockDTOs);
		responseDTO.setStockAnalysis(stockAnalysis);
		LOGGER.info("fetchStockPrices response - {}", responseDTO);
		return responseDTO;
	}

	private StockAnalysis getstockAnalysis(String companyCode, Date startDate, Date endDate) {
		StockAnalysis stockAnalysis = null;
		Aggregation aggregation = Aggregation.newAggregation(
			Aggregation.match(new Criteria()
					.andOperator(Criteria.where("companyCode").is(companyCode)
						.and("createdTimestamp").gte(startDate).lte(endDate))),
			Aggregation.group()
				.min("stockPrice").as("minPrice")
				.max("stockPrice").as("maxPrice")
				.avg("stockPrice").as("averagePrice")
				);
		
		AggregationResults<StockAnalysis> aggResult = 
				mongoTemplate.aggregate(aggregation, "stock_details",StockAnalysis.class);
		
		LOGGER.info("result : "+aggResult.toString());
		stockAnalysis = aggResult.getUniqueMappedResult();
		
		LOGGER.info("stock analysis for company-code {} from {} to {} is {}",companyCode,startDate,endDate,stockAnalysis);
		return stockAnalysis;
	}
	
	public Double fetchLatestStockPrices(String companyCode) throws StockNotFoundException {
		LOGGER.info("inside fetchLatestStockPrices with companyCode : {}", companyCode);

		Optional<StockDetails> stockDtlOptional = repo.findFirstByCompanyCodeOrderByCreatedTimestampDesc(companyCode);

		if (stockDtlOptional.isPresent()) {
			return stockDtlOptional.get().getStockPrice();
		} else {
			LOGGER.error("Stock details for company-code {} not present in the DB.", companyCode);

			throw new StockNotFoundException(
					"Stock details for company-code " + companyCode + " not present in the DB.");
		}
	}

	public Map<String, Double> fetchAllStockPrice() {
		LOGGER.info("inside fetchAllStockPrice method ");
		Map<String, Double> resultMap = new LinkedHashMap<>();

		SortOperation sortByTs = Aggregation.sort(Direction.DESC, "createdTimestamp");
		GroupOperation companyCodeGrp = Aggregation.group("companyCode").first("createdTimestamp").as("latestTs")
				.first("stockPrice").as("latestStockPrice");

		ProjectionOperation project = Aggregation.project().andExpression("_id").as("companyCode")
				.andExpression("latestStockPrice").as("stockPrice");

		Aggregation aggr = Aggregation.newAggregation(sortByTs, companyCodeGrp, project);

		AggregationResults<LatestStockDetails> result = mongoTemplate.aggregate(aggr, "stock_details",
				LatestStockDetails.class);

		List<LatestStockDetails> latestStocksList = result.getMappedResults();
		LOGGER.info("query result - {}", latestStocksList);

		resultMap = Optional.ofNullable(latestStocksList).orElseGet(ArrayList::new).stream()
				.collect(Collectors.toMap(LatestStockDetails::getCompanyCode, LatestStockDetails::getStockPrice));

		return resultMap;
	}

	private Date stringToDateConverter(String date, String dateType) {
		LocalDate localDate = LocalDate.parse(date);
		LocalDateTime localDateTime = null;
		if (dateType.equals("STARTDATE")) {
			localDateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
		} else {
			localDateTime = LocalDateTime.of(localDate, LocalTime.of(23, 59, 59));
		}
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}

	public boolean deleteStockDetails(String companyCode) throws StockNotFoundException {
		LOGGER.info("deleteStockDetails for comany-code : {}", companyCode);
		Integer deletedRows = repo.deleteByCompanyCode(companyCode);
		if (deletedRows > 0) {
			return true;
		}

		throw new StockNotFoundException(
				"Stock details for company-code " + companyCode + " does not exist or already deleted.");
	}

}
