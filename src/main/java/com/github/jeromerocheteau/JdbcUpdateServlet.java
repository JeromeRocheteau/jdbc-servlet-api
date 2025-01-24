package com.github.jeromerocheteau;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public abstract class JdbcUpdateServlet<T> extends JdbcServlet {

	private static final long serialVersionUID = 03L;

	private String query;
	
	private JdbcUpdateCallback<T> callback;
	
	@Override
	public void init() throws ServletException {
		try {
			super.init();
			String name = this.getInitParameter(JdbcProperties.QUERY_NAME);
			query = this.getContent(name);
			callback = this.getCallback(this);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	protected abstract JdbcUpdateCallback<T> getCallback(HttpServlet servlet);
		
	protected T doProcess(HttpServletRequest request) throws IOException, ServletException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = this.getConnection();
			statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			callback.doFill(statement, request);
			int count = statement.executeUpdate();
			connection.commit();
			ResultSet resultSet = statement.getGeneratedKeys();
			return callback.doMap(request, count, resultSet);
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
