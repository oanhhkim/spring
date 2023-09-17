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
import com.estore.entity.Product;

@Transactional(readOnly = true)
@Repository
public class ProductDAOImpl extends DBContext implements ProductDAO{

	@Autowired
	SessionFactory factory;
	
	@Override
	public Product findById(Integer id) {
		Session session = factory.getCurrentSession();
		Product entity = session.find(Product.class, id);
		return entity;
	}

	@Override
	public List<Product> findAll() {
		String hql = "FROM Product";
		Session session = factory.getCurrentSession();
		TypedQuery<Product> query = session.createQuery(hql, Product.class);
		List<Product> list = query.getResultList();
		return list;
	}

	@Override
	public void create(Product entity) {
		String sql = "INSERT INTO Products VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement st = connection.prepareStatement(sql);
			st.setInt(1, entity.getId());
			st.setString(2, entity.getName());
			st.setDouble(3, entity.getUnitPrice());
			st.setString(4, entity.getImage());
			st.setString(5, entity.getProductDate());
			st.setBoolean(6, entity.getAvailable());
			st.setInt(7, entity.getCategory().getId());
			st.setInt(8, entity.getQuantity());
			st.setString(9, entity.getDescription());
			st.setDouble(10, entity.getDiscount());
			st.setInt(11, entity.getViewCount());
			st.setBoolean(12, entity.getSpecial());
			st.executeUpdate();
			
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}

	@Override
	public void update(Product entity) {
		String sql = "UPDATE Products SET Name=?, UnitPrice=?, Image=?, ProductDate=?, Available=?, CategoryId=?, Quantity=?, Description=?, Discount=?, ViewCount=?, Special=? WHERE id=?";
		try {
			PreparedStatement st = connection.prepareStatement(sql);
			st.setString(1, entity.getName());
			st.setDouble(2, entity.getUnitPrice());
			st.setString(3, entity.getImage());
			st.setString(4, entity.getProductDate());
			st.setBoolean(5, entity.getAvailable());
			st.setInt(6, entity.getCategory().getId());
			st.setInt(7, entity.getQuantity());
			st.setString(8, entity.getDescription());
			st.setDouble(9, entity.getDiscount());
			st.setInt(10, entity.getViewCount());
			st.setBoolean(11, entity.getSpecial());
			st.setInt(12, entity.getId());
			st.executeUpdate();
			
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}

	@Override
	public void delete(Integer id) {
		String sql = "DELETE FROM Products WHERE id=?";
		try {
			PreparedStatement st = connection.prepareStatement(sql);
			st.setInt(1, id);
			st.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}

	@Override
	public List<Product> findByCategoryId(Integer categoryId) {
		String hql = "FROM Product p WHERE p.category.id=:cid";
		Session session = factory.getCurrentSession();
		TypedQuery<Product> query = session.createQuery(hql, Product.class);
		query.setParameter("cid", categoryId);
		List<Product> list = query.getResultList();
		return list;
	}
	
	@Override
	public List<Product> findByKeywords(String keywords) {
		String hql = "FROM Product p "
				+ "WHERE p.name LIKE :kw OR p.category.name LIKE :kw OR p.category.nameVN LIKE :kw";
		Session session = factory.getCurrentSession();
		TypedQuery<Product> query = session.createQuery(hql, Product.class);
		query.setParameter("kw", "%"+keywords+"%");
		List<Product> list = query.getResultList();
		return list;
	}

	@Override
	public List<Product> findByIds(String ids) {
		String hql = "FROM Product p WHERE p.id IN ("+ids+")";
		Session session = factory.getCurrentSession();
		TypedQuery<Product> query = session.createQuery(hql, Product.class);
		List<Product> list = query.getResultList();
		return list;
	}

	@Override
	public List<Product> findBySpecial(Integer id) {
		Session session = factory.getCurrentSession();
		String hql = "FROM Product p";
		TypedQuery<Product> query = session.createQuery(hql, Product.class);
		switch(id) {
		case 0://moi
			hql = "FROM Product p ORDER BY p.productDate DESC";
			break;
		case 1://ban chay
			hql = "FROM Product p ORDER BY size(p.orderDetails) DESC";
			break;	
		case 2://xem nhieu
			hql = "FROM Product p ORDER BY p.viewCount DESC";
			break;
		case 3://giam gia
			hql = "FROM Product p ORDER BY p.discount DESC";
			break;
		}
		
		query = session.createQuery(hql, Product.class);
		query.setMaxResults(12);
		List<Product> list = query.getResultList();
		return list;
	}


}
