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
import com.estore.entity.Order;
import com.estore.entity.OrderDetail;

@Transactional(readOnly = true)
@Repository
public class OrderDAOImpl extends DBContext implements OrderDAO{

	@Autowired
	SessionFactory factory;
	
	@Autowired
	OrderDetailDAO ddao;
	
	@Override
	public Order findById(Integer id) {
		Session session = factory.getCurrentSession();
		Order entity = session.find(Order.class, id);
		return entity;
	}

	@Override
	public List<Order> findAll() {
		String hql = "FROM Order";
		Session session = factory.getCurrentSession();
		TypedQuery<Order> query = session.createQuery(hql, Order.class);
		List<Order> list = query.getResultList();
		return list;
	}

	@Override
	public void create(Order entity) {
		String sql= "INSERT INTO Orders VALUES(?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement st = connection.prepareStatement(sql);
			st.setInt(1, entity.getId());
			st.setString(2, entity.getCustomer().getId());
			st.setString(3, entity.getOrderDate());
			st.setString(4, entity.getAddress());
			st.setDouble(5, entity.getAmount());
			st.setString(6, entity.getDescription());
			st.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}

	@Override
	public void update(Order entity) {
		Session session = factory.getCurrentSession();
		session.update(entity);
	}

	@Override
	public Order delete(Integer id) {
		Session session = factory.getCurrentSession();
		Order entity = session.find(Order.class, id);
		session.delete(entity);
		return entity;
	}

	@Override
	public void create(Order order, List<OrderDetail> details) {
		List<Order> list = this.findAll();
		order.setId(list.size()+1);
		create(order);
		for(OrderDetail detail:details) {
			ddao.create(detail);
		}
	}

	@Override
	public List<Order> findByUser(Customer user) {
		String hql = "FROM Order o WHERE o.customer.id=:uid ORDER BY o.orderDate DESC";
		Session session = factory.getCurrentSession();
		TypedQuery<Order> query = session.createQuery(hql, Order.class);
		query.setParameter("uid", user.getId());
		List<Order> list = query.getResultList();
		return list;
	}

}
