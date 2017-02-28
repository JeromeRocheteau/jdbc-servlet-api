package com.github.servlet.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public abstract class JdbcQueryServlet<T> extends JdbcServlet {

	private static final long serialVersionUID = 02L;

	private String query;
	
	@Override
	public void init() throws ServletException {
		try {
			super.init();
			String name = this.getInitParameter(JdbcProperties.QUERY_NAME);
			query = this.getContent(name);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	protected abstract void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception;
	
	protected abstract T doMap(HttpServletRequest request, ResultSet resultSet) throws Exception;
	
	protected T doProcess(HttpServletRequest request) throws IOException, ServletException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = this.getConnection();
			statement = connection.prepareStatement(query);
			this.doFill(statement, request);
			ResultSet resultSet = statement.executeQuery();
			connection.commit();
			T result = this.doMap(request, resultSet);
			resultSet.close();
			return result;
		} catch (Exception e) {
			try {
				connection.rollback();
				throw new ServletException(e);
			} catch (Exception ex) {
				throw new ServletException(ex.getMessage(), e);
			}
		} finally {
		   try {
				statement.close();
				connection.close();
				connection = null;
		   } catch (Exception e) {
			   throw new ServletException(query, e);
		   }
		}
	}
		
}
