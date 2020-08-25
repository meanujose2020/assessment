package com.customer.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.customer.entity.Customer;

public interface CustomerMongoRepository extends MongoRepository<Customer, String> {

	Customer findByCustomerNumber(int customerNumber);

	List<Customer> findAll();

	void deleteByCustomerNumber(int customerNumber);

	Customer save(Customer customer);
	
	Customer findByCustomername(String customername);


}
