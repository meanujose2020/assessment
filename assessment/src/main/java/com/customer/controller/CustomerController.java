package com.customer.controller;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.customer.entity.Customer;
import com.customer.entity.CustomerDTO;
import com.customer.service.CustomerManager;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerManager customerService;

	
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CustomerDTO> getAllCustomerDetails() throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Entered : getAllCustomerDetails");
		}
		List<CustomerDTO> allCustomerDetails;
		try {
			allCustomerDetails = customerService.getAllCustomerDetails();
		} catch (Exception exception) {
			throw exception;
		}
		return allCustomerDetails;
	}

	@RequestMapping(value = "/{customerNumber}", method = RequestMethod.GET)
	public CustomerDTO getCustomerByCustomerNumber(@PathVariable("customerNumber") Integer customerNumber)
			throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Entered: getEndPoint endPointId {}", customerNumber);
		}
		CustomerDTO customerDTO = null;
		try {
			customerDTO = customerService.findByCustomerNumber(customerNumber);
		} catch (Exception exception) {
			LOGGER.error("Failed to get the cutomer details for customerNumber{}", exception);
			throw exception;
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Exiting: getCustomerByCustomerNumber customerDTO {}", customerDTO);
		}
		return customerDTO;
	}

	@RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Entered: createCustomer customerDTO {}", customerDTO);
		}
		ResponseEntity<CustomerDTO> responseEntity = null;
		try {
			customerDTO = customerService.saveCustomerDetails(customerDTO);
			HttpHeaders headers = new HttpHeaders();
			responseEntity = new ResponseEntity<CustomerDTO>(customerDTO, headers, HttpStatus.OK);
		} catch (Exception exception) {
			LOGGER.error("Failed to save the customer details {} ", customerDTO);
			throw exception;
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Exiting: createCustomer responseEntity {}", responseEntity);
		}
		return responseEntity;
	}

	@RequestMapping(value = "/{customerNumber}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteCustomer(@PathVariable("customerNumber") Integer customerNumber)
			throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Entered: deleteCustomer {}", customerNumber);
		}
		ResponseEntity<String> responseEntity = null;
		try {

			if (customerService.deleteCustomer(customerNumber) != null) {
				responseEntity = new ResponseEntity<String>("Deleted the entry", HttpStatus.OK);
			}
		} catch (Exception exception) {
			LOGGER.error("Failed to deleteCustomer for customerNumber{}", exception);
			throw exception;
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Exiting: deleteCustomer..");
		}
		return responseEntity;
	}
	
	@RequestMapping(value = "updatecustomer", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity updateUser(@RequestParam("customerNumber") Integer customerNumber, @RequestBody Customer customer) 
	throws Exception {
	    HashMap<String, Object> resp = new HashMap<>();
	    customerService.updateCustomer(customer);
	    resp.put("customer", customer);
	    return new ResponseEntity<>(resp, HttpStatus.OK);
	}

}
