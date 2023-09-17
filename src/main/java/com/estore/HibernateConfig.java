package com.estore;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@Configuration
public class HibernateConfig {
	
	@Autowired
	Environment env;

	@Bean
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		dataSource.setUrl("jdbc:sqlserver://localhost:1433;databaseName=eStore");
		dataSource.setUsername("sa");
		dataSource.setPassword("123");
		return dataSource;
	}
	

	@Bean
	public SessionFactory getSessionFactory(DataSource dataSource) throws IOException{
			LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
			factoryBean.setPackagesToScan(new String[]{"com.estore.entity"});
			factoryBean.setDataSource(dataSource);
			Properties props = factoryBean.getHibernateProperties();
			props.put("hibernate.dialect", "org.hibernate.dialect.SQLServer2012Dialect");
			props.put("hibernate.show_sql",false);
			props.put("current_session_context_class", "org.springframework.orm.hibernate5.SpringSessionContext");
			factoryBean.afterPropertiesSet();
			return factoryBean.getObject();
	}
	
	@Bean
	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
		return new HibernateTransactionManager(sessionFactory);
	}
}
