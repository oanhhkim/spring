package com.estore.dao;

import java.util.List;

import com.estore.entity.Customer;

public interface CustomerDAO {
	Customer findById(String id);
	List<Customer> findAll();
	void create(Customer entity);
	void update(Customer entity);
	Customer delete(String id);
	void changePassword(String id, String pw);
}
