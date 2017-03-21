package com.github.jeromerocheteau;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.util.Scanner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;

public abstract class JdbcServlet extends HttpServlet {

	private static final long serialVersionUID = 01L;

	private DataSource dataSource;
	
	protected final Connection getConnection () throws Exception {
		Connection connection = dataSource.getConnection();
		connection.setAutoCommit(false);
		return connection;
	}

	private Gson gson;
	
	@Override
	public void init() throws ServletException {
		try {
			super.init();
			dataSource = (DataSource) this.getServletContext().getAttribute(JdbcProperties.DATA_SOURCE);
			gson = new Gson();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
	}
	
	protected final void doCall(HttpServletRequest request, HttpServletResponse response, String name) throws ServletException, IOException {
		ServletContext context = this.getServletContext();
		RequestDispatcher dispatcher = context.getNamedDispatcher(name);
		dispatcher.include(request, response);
	}

	protected final <T> T doRead(Reader reader, Class<T> type) throws IOException, ServletException {
		return gson.fromJson(reader, type);
	}
	
	protected final void doWrite(Object object, Writer writer) throws IOException, ServletException {
		gson.toJson(object, writer);
	}
	
	protected final String getContent(String name) throws IOException {
		StringBuilder builder = new StringBuilder();
		InputStream inputStream = this.getClass().getResourceAsStream(name);
		if (inputStream == null) {
			throw new NullPointerException(name);
		} else {
			this.doBuild(inputStream, builder);
			return builder.toString();
		}
	}

	private void doBuild(InputStream inputStream, StringBuilder builder) throws IOException {
		Scanner scanner = new Scanner(inputStream);
		scanner.useDelimiter("\n");
		while (scanner.hasNext()) {
			String line = scanner.next();
			builder.append(line).append("\n");
		}
		scanner.close();
	}
	
}
