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
import com.estore.entity.Customer;

@Transactional(readOnly = true)
@Repository
public class CustomerDAOImpl extends DBContext implements CustomerDAO{

	@Autowired
	SessionFactory factory;
	
	@Override
	public Customer findById(String id) {
		Session session = factory.getCurrentSession();
		Customer entity = session.find(Customer.class, id);
		return entity;
	}

	@Override
	public List<Customer> findAll() {
		String hql = "FROM Customer";
		Session session = factory.getCurrentSession();
		TypedQuery<Customer> query = session.createQuery(hql, Customer.class);
		List<Customer> list = query.getResultList();
		return list;
	}

	@Override
	public void create(Customer entity) {
		String hql = "INSERT INTO Customers values (?,?,?,?,?,?,?)";
		try {
			PreparedStatement st = connection.prepareStatement(hql);
			st.setString(1, entity.getId());
			st.setString(2, entity.getPassword());
			st.setString(3, entity.getFullname());
			st.setString(4, entity.getEmail());
			st.setString(5, entity.getPhoto());
			st.setBoolean(6, entity.getActivated());
			st.setBoolean(7, entity.getAdmin());
			st.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}

	@Override
	public void update(Customer entity) {
		Session session = factory.getCurrentSession();
		session.update(entity);
	}

	@Override
	public Customer delete(String id) {
		Session session = factory.getCurrentSession();
		Customer entity = session.find(Customer.class, id);
		session.delete(entity);
		return entity;
	}

	@Override
	public void changePassword(String id, String pw) {
		String hql = "UPDATE Customers SET Password=? WHERE Id=?";
		try {
			PreparedStatement st = connection.prepareStatement(hql);
			st.setString(1, pw);
			st.setString(2, id);
			st.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
		}
		
	}

}
