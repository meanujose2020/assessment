package com.customer.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.ws.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.customer.entity.Customer;
import com.customer.entity.CustomerDTO;
import com.customer.repository.CustomerMongoRepository;
import com.customer.service.CustomerServiceImpl;

class CustomerServiceImplTest {

	@InjectMocks
	CustomerServiceImpl customerServiceImpl;
	@Mock
	CustomerMongoRepository customerMongoRepository;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testFindByCustomerNumber() throws Exception {
		Customer customer = new Customer();
		customer.setCustomerNumber(45);
		customer.setCustomername("XYZ");
		customer.setCustomeraddress("XYZT");
		when(customerMongoRepository.findByCustomerNumber(anyInt())).thenReturn(customer);
		CustomerDTO customerdto = customerServiceImpl.findByCustomerNumber(45);
		assertNotNull(customerdto);
		assertEquals("TOM", customerdto.getCustomername());
	}
	
	@Test
	public void createCustomerTest() throws Exception {
		Customer customer = new Customer();
		customer.setCustomerNumber(24);
		customer.setCustomername("XYZ");
		customer.setCustomeraddress("IND");
		when(customerMongoRepository.save(customer)).thenReturn(customer);
		assertEquals(customer, customerServiceImpl.addCustomer(customer));
	}
	
	@Test
	public void getAllCustomerDetailsTest() throws Exception {
		Customer customer = new Customer();
		customer.setCustomerNumber(22);
		customer.setCustomername("TOM");
		customer.setCustomeraddress("KOTTAYAM");
		when(customerMongoRepository.findAll()).thenReturn(Stream
				.of(new Customer()).collect(Collectors.toList()));
		assertEquals(1, customerServiceImpl.getAllCustomerDetails().size());
	}
	
	@Test
	public void deleteCustomerTest() throws Exception {
		Customer customer = new Customer();
		when(customerMongoRepository.findByCustomerNumber(anyInt())).thenReturn(customer);
		customerServiceImpl.deleteCustomer(customer.getCustomerNumber());
		verify(customerMongoRepository, times(1)).deleteByCustomerNumber(customer.getCustomerNumber());
	}
	

	@Test
	public void updateCustomerTest() throws Exception{
	String customername = "Anutomerd";
	Customer customer = new Customer();
	customer.setCustomername(customername);
	when(customerMongoRepository.save(customer)).thenReturn(customer);
	assertEquals(customer, customerServiceImpl.updateCustomer(customer));
	}
}
