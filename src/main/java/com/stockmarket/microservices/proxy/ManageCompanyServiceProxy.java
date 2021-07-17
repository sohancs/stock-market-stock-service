package com.stockmarket.microservices.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.stockmarket.microservices.exception.CompanyNotFoundException;
import com.stockmarket.microservices.model.CompanyDetails;

@FeignClient(name = "manage-company-service", path = "/api/v1.0/market/company")
public interface ManageCompanyServiceProxy {

	@GetMapping("/info/{companyCode}")
	public ResponseEntity<CompanyDetails> fetchCompanyDetails(@PathVariable("companyCode") String companyCode)
			throws CompanyNotFoundException;
}
