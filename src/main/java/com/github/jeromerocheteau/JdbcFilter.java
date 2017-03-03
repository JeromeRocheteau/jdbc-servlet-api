package com.github.jeromerocheteau;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.sql.DataSource;

public class JdbcFilter implements Filter {

	private DataSource dataSource;
	
	protected Connection getConnection () throws Exception {
		return dataSource.getConnection();
	}
	
	private String query;
	
	private String requestEncoding;
	
	private String responseEncoding;
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		try {
			dataSource = (DataSource) config.getServletContext().getAttribute(JdbcProperties.DATA_SOURCE);
			this.setQuery(config);
			this.setRequestEncoding(config);
			this.setResponseEncoding(config);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void setQuery(FilterConfig config) throws IOException {
		String name = config.getInitParameter(JdbcProperties.QUERY_NAME);
		if (name == null || name.length() == 0) {
			query = null;
		} else {
			query = this.getContent(name);							
		}
	}

	private void setRequestEncoding(FilterConfig config) {
		String name = config.getInitParameter(JdbcProperties.REQUEST_ENCODING_NAME);
		if (name == null || name.length() == 0) {
			this.requestEncoding = JdbcProperties.DEFAULT_ENCODING;
		} else {
			this.requestEncoding = name;
		}
	}

	private void setResponseEncoding(FilterConfig config) {
		String name = config.getInitParameter(JdbcProperties.RESPONSE_ENCODING_NAME);
		if (name == null || name.length() == 0) {
			this.responseEncoding = JdbcProperties.DEFAULT_ENCODING;
		} else {
			this.responseEncoding = name;
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding(requestEncoding);
		response.setCharacterEncoding(responseEncoding);
		if (query != null) this.doProcess(request);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() { }
		
	private final String getContent(String name) throws IOException {
		String resource = name.startsWith("/") ? name : "/" + name;
		StringBuilder builder = new StringBuilder();
		InputStream inputStream = this.getClass().getResourceAsStream(resource);
		if (inputStream == null) {
			throw new NullPointerException(name);
		} else {
			Scanner scanner = new Scanner(inputStream);
			scanner.useDelimiter("\n");
			while (scanner.hasNext()) {
				String line = scanner.next();
				builder.append(line).append("\n");
			}
			scanner.close();
			return builder.toString();
		}
	}

	protected void doFill(PreparedStatement statement, ServletRequest request) throws Exception { }
	
	protected void doMap(ServletRequest request, ResultSet resultSet) throws Exception { }
	
	private void doProcess(ServletRequest request) throws IOException, ServletException {
		try {
			Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			this.doFill(statement, request);
			ResultSet resultSet = statement.executeQuery();
			this.doMap(request, resultSet);
			resultSet.close();
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

}
