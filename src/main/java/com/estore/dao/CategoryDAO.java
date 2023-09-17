package com.estore.dao;

import java.util.List;

import com.estore.entity.Category;

public interface CategoryDAO {
	Category findById(Integer id);
	List<Category> findAll();
	void create(Category entity);
	void update(Category entity);
	void delete(Integer id);
}
