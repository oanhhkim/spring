package com.estore.dao;

import java.util.List;

import com.estore.entity.Product;

public interface ProductDAO {
	Product findById(Integer id);
	List<Product> findAll();
	void create(Product entity);
	void update(Product entity);
	void delete(Integer id);
	List<Product> findByCategoryId(Integer categoryId);
	List<Product> findByKeywords(String keywords);
	List<Product> findByIds(String ids);
	List<Product> findBySpecial(Integer id);
}
