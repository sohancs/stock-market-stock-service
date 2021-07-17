package com.stockmarket.microservices.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockmarket.microservices.exception.CompanyNotFoundException;
import com.stockmarket.microservices.exception.StockNotFoundException;
import com.stockmarket.microservices.model.StockRequestDTO;
import com.stockmarket.microservices.model.StockResponseDTO;
import com.stockmarket.microservices.service.StockService;

@RestController
@RequestMapping("/api/v1.0/market/stock")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StockController {

	@Autowired
	private StockService stockService;

	@PostMapping("/add")
	public ResponseEntity<Object> addStockDetails(@RequestBody @Valid StockRequestDTO requestDTO)
			throws CompanyNotFoundException {

		stockService.addStockDetails(requestDTO);
		
		return new ResponseEntity<Object>("Stock details added successfully.", HttpStatus.CREATED);
	}

	@GetMapping("/get/{companycode}/{startdate}/{enddate}")
	public ResponseEntity<StockResponseDTO> fetchStockDetailsByCode(@PathVariable("companycode") String companycode,
			@PathVariable("startdate") String startdate, @PathVariable("enddate") String enddate)
			throws CompanyNotFoundException {

		StockResponseDTO responseDTO = stockService.fetchStockPrices(companycode, startdate, enddate);
		return new ResponseEntity<StockResponseDTO>(responseDTO, HttpStatus.OK);
	}

	@GetMapping("/getLatestStockPrice/{companycode}")
	public ResponseEntity<Object> fetchLatestStockPrices(@PathVariable("companycode") String companycode)
			throws StockNotFoundException {

		return new ResponseEntity<Object>(stockService.fetchLatestStockPrices(companycode), HttpStatus.OK);
	}

	@GetMapping("/getAllLatestStockPrice")
	public ResponseEntity<Object> getAllLatestStockPrice() {

		return new ResponseEntity<Object>(stockService.fetchAllStockPrice(), HttpStatus.OK);
	}

	@DeleteMapping("/delete/{companyCode}")
	public ResponseEntity<Object> deleteStockDetails(@PathVariable("companyCode") String companyCode)
			throws StockNotFoundException {
		stockService.deleteStockDetails(companyCode);

		return new ResponseEntity<Object>("Stock Details deleted successfully", HttpStatus.NO_CONTENT);
	}
}
