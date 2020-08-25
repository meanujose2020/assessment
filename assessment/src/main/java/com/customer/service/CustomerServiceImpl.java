package com.customer.service;

import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.customer.entity.Customer;
import com.customer.entity.CustomerDTO;
import com.customer.repository.CustomerMongoRepository;

@Service
public class CustomerServiceImpl implements CustomerManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	CustomerMongoRepository customerMongoRepository;


	public static CustomerDTO convertCustomerToCustomerDTO(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();

		customerDTO.setCustomerNumber(customer.getCustomerNumber());
		customerDTO.setCustomername(customer.getCustomername());
		customerDTO.setCustomeraddress(customer.getCustomeraddress());
		return customerDTO;
	}

	@Cacheable(value = "customers")
	@Override
	public List<CustomerDTO> getAllCustomerDetails() throws Exception {
		List<CustomerDTO> customerList = new ArrayList<>();
		List<Customer> customer = customerMongoRepository.findAll();
		for (Customer customervalues : customer) {
			if (customervalues != null) {
				CustomerDTO dto = convertCustomerToCustomerDTO(customervalues);
				customerList.add(dto);
			}
		}
		return customerList;
	}

	@Cacheable(value = "customers-single", key = "#customerNumber")
	@Override
	public CustomerDTO findByCustomerNumber(Integer customerNumber) throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Entered : findByCustomerNumber customerNumber {}", customerNumber);
		}
		Customer customerList = null;
		CustomerDTO customerdto = null;
		try {
			
			customerList = customerMongoRepository.findByCustomerNumber(customerNumber);
		
		} catch (Exception exception) {
			LOGGER.error("Error inside Exception findByCustomerNumber customerNumber {} exception {}", customerNumber, exception);
			throw new Exception(String.valueOf(customerNumber));
		}
		String status = customerList.getStatus();
		if(status.equalsIgnoreCase("ACTIVE")) {
			LOGGER.info("Active customer");
		}else {
			throw new Exception("Not an active customer");
		}
		if((null != customerList)) {
		customerdto = convertCustomerToCustomerDTO(customerList);
		}
		return customerdto;
	}

	@Cacheable(value = "post")
	@Override
	@Transactional
	public CustomerDTO saveCustomerDetails(CustomerDTO customerDTO) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered :  saveCustomerDetails customerDTO {}", customerDTO);
		}
		Customer customer = new Customer();
		try {
			customer.setCustomerNumber(customerDTO.getCustomerNumber());
			customer.setCustomername(customerDTO.getCustomername());
			customer.setCustomeraddress(customerDTO.getCustomeraddress());
			customer.setStatus(customerDTO.getStatus());
			customer = customerMongoRepository.save(customer);
			customerDTO.setCustomerNumber(customer.getCustomerNumber());
			customerDTO.setCustomername(customer.getCustomername());
			customerDTO.setCustomeraddress(customer.getCustomeraddress());
			customerDTO.setStatus(customer.getStatus());
		} catch (Exception exception) {
			LOGGER.error("Error inside Exception saveCustomerDetails customerDTO {} exception {}", customerDTO,
					exception);
			throw new Exception(String.valueOf(customerDTO));
		}
		return customerDTO;
	}

	@CacheEvict(value = "delete-single", key = "#customerNumber")
	@Override
	public Customer deleteCustomer(Integer customerNumber) throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Entered : deleteCustomer customerNumber {}", customerNumber);
		}
		Customer existingCustomer = null;
		try {
			existingCustomer = customerMongoRepository.findByCustomerNumber(customerNumber);
			if(existingCustomer!=null) {
			customerMongoRepository.deleteByCustomerNumber(customerNumber);
			}{
		        LOGGER.info("Exiting : deleteCustomer :  customer Number  not found{}", customerNumber);
		        throw new Exception("customer Number not found");
		      }
		} catch (Exception exception) {
			LOGGER.error("Error inside Exception deleteCustomer customerNumber {} exception {}", customerNumber, exception);
			throw new Exception(String.valueOf(customerNumber));
		}
	}
	
	public Customer addUser(Customer customer) {
		return customerMongoRepository.save(customer);
	}
	
	@CachePut(value = "update", key = "#customer.customerNumber")
	  @Override
	    public Customer updateCustomer(Customer customer) throws Exception {
		  customer = customerMongoRepository.save(customer);
	        return customer;
	    }

	
	

}
