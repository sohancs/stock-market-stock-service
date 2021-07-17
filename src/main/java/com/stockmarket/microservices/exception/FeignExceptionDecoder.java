package com.stockmarket.microservices.exception;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockmarket.microservices.model.ErrorDetails;

import feign.Response;
import feign.Response.Body;
import feign.codec.ErrorDecoder;

public class FeignExceptionDecoder implements ErrorDecoder {

	Logger LOGGER = LoggerFactory.getLogger(FeignExceptionDecoder.class);

	@Override
	public Exception decode(String methodKey, Response response) {
		ErrorDetails errorDetails = mapBodyToObject(response.body());
		LOGGER.error("inside Stock Service FeignExceptionDecoder response => {}, {}", errorDetails, methodKey);

		if (!StringUtils.isEmpty(errorDetails.getSourceClass())) {
			switch (errorDetails.getSourceClass().toString()) {
			case "CompanyNotFoundException":
				return new CompanyNotFoundException(errorDetails.getErrorMesssage());
			case "StockNotFoundException":
				return new StockNotFoundException(errorDetails.getErrorMesssage());
			default:
				return new RuntimeException("Generic Exception");
			}
		}
		return new RuntimeException("Generic Exception");
	}

	private ErrorDetails mapBodyToObject(Body responseBody) {
		InputStream inputStream = null;
		ErrorDetails errorDetails = new ErrorDetails();
		if (responseBody != null) {
			try {
				inputStream = responseBody.asInputStream();
				ObjectMapper mapper = new ObjectMapper();
				errorDetails = mapper.readValue(inputStream, ErrorDetails.class);
			} catch (IOException e) {
				LOGGER.error("IOException occured while parsing to ErrorDetails.");
				e.printStackTrace();
			} finally {
				try {
					if (inputStream != null)
						inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return errorDetails;
	}

}
