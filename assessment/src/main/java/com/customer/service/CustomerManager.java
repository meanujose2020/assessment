package com.customer.service;

import java.util.List;

import com.customer.entity.Customer;
import com.customer.entity.CustomerDTO;

public interface CustomerManager {

	

	CustomerDTO findByCustomerNumber(Integer studentNumber) throws Exception;

	CustomerDTO saveCustomerDetails(CustomerDTO customerDTO) throws Exception;

    public Customer deleteCustomer(Integer customerNumber) throws Exception;

	List<CustomerDTO> getAllCustomerDetails()throws Exception;

	Customer updateCustomer(Customer customer) throws Exception;

	
}
