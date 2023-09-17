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
import com.estore.entity.Order;
import com.estore.entity.OrderDetail;

@Transactional(readOnly = true)
@Repository
public class OrderDetailDAOImpl extends DBContext implements OrderDetailDAO{

	@Autowired
	SessionFactory factory;
	
	@Override
	public OrderDetail findById(Integer id) {
		Session session = factory.getCurrentSession();
		OrderDetail entity = session.find(OrderDetail.class, id);
		return entity;
	}

	@Override
	public List<OrderDetail> findAll() {
		String hql = "FROM OrderDetail";
		Session session = factory.getCurrentSession();
		TypedQuery<OrderDetail> query = session.createQuery(hql, OrderDetail.class);
		return query.getResultList();
	}

	@Override
	public void create(OrderDetail entity) {
		String sql = "INSERT INTO OrderDetails(OrderId, ProductId, UnitPrice, Quantity, Discount) VALUES(?, ?, ?, ?, ?)";
		try {
			PreparedStatement st = connection.prepareStatement(sql);
			st.setInt(1, entity.getOrder().getId());
			st.setInt(2, entity.getProduct().getId());
			st.setDouble(3, entity.getUnitPrice());
			st.setInt(4, entity.getQuantity());
			st.setDouble(5, entity.getDiscount());
			st.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}

	@Override
	public void update(OrderDetail entity) {
		Session session = factory.getCurrentSession();
		session.update(entity);
	}

	@Override
	public OrderDetail delete(Integer id) {
		Session session = factory.getCurrentSession();
		OrderDetail entity = session.find(OrderDetail.class, id);
		session.delete(entity);
		return entity;
	}

	@Override
	public List<OrderDetail> findByOrder(Order order) {
		String hql = "FROM OrderDetail d WHERE d.order.id=:oid";
		Session session = factory.getCurrentSession();
		TypedQuery<OrderDetail> query = session.createQuery(hql, OrderDetail.class);
		query.setParameter("oid", order.getId());
		List<OrderDetail> list = query.getResultList();
		return list;
	}

}
