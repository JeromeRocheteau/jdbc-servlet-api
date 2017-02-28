package com.github.servlet.jdbc;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

public class JdbcListener implements ServletContextListener {

	private DataSource dataSource;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			ServletContext context = event.getServletContext();
			String name = context.getInitParameter(JdbcProperties.RESOURCE_NAME);
			
			Context initialContext = new InitialContext();
			Context envContext = (Context) initialContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup(name);
			
			event.getServletContext().setAttribute(JdbcProperties.DATA_SOURCE, dataSource);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) { }

}
