package com.estore.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.estore.DBContext;
import com.estore.entity.Category;

@Transactional(readOnly = true)
@Repository
public class CategoryDAOImpl extends DBContext implements CategoryDAO{

	@Autowired
	SessionFactory factory;
	
	@Override
	public Category findById(Integer id) {
		Session session = factory.getCurrentSession();
		Category entity = session.find(Category.class, id);
		return entity;
	}

	@Override
	public List<Category> findAll() {
		String hql = "FROM Category";
		Session session = factory.getCurrentSession();
		TypedQuery<Category> query = session.createQuery(hql, Category.class);
		List<Category> list = query.getResultList();
		return list;
	}

	@Override
	public void create(Category entity) {
		String sql = "INSERT INTO Categories VALUES(?, ?, ?)";
		try {
			PreparedStatement st = connection.prepareStatement(sql);
			st.setInt(1, entity.getId());
			st.setString(2, entity.getName());
			st.setString(3, entity.getNameVN());
			st.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}

	@Override
	public void update(Category entity) {
		String sql = "UPDATE Categories SET name=?, nameVN=? WHERE id=?";
		try {
			PreparedStatement st = connection.prepareStatement(sql);
			st.setString(1, entity.getName());
			st.setString(2, entity.getNameVN());
			st.setInt(3, entity.getId());
			st.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}

	@Override
	public void delete(Integer id) {
		String sql = "DELETE FROM Categories WHERE id=?";
		try {
			PreparedStatement st = connection.prepareStatement(sql);
			st.setInt(1, id);
			st.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}

}
