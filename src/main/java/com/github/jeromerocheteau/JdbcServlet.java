package com.github.jeromerocheteau;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public abstract class JdbcServlet extends HttpServlet {

	private static final long serialVersionUID = 01L;

	private DataSource dataSource;
	
	protected final Connection getConnection () throws Exception {
		Connection connection = dataSource.getConnection();
		connection.setAutoCommit(false);
		return connection;
	}
	
	@Override
	public void init() throws ServletException {
		try {
			super.init();
			dataSource = (DataSource) this.getServletContext().getAttribute(JdbcProperties.DATA_SOURCE);
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

	protected final void doPrint(Object object, HttpServletResponse response) throws IOException, ServletException {
		try {
			response.setContentType(JdbcProperties.RESPONSE_CONTENT_TYPE);
			PrintWriter writer = response.getWriter();
			writer.write(this.toString(object));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	private String toString(Object object) throws Exception {
		String string = null;
		if (object == null) {
			string = "";
		} else if (object instanceof String) {
			string = (String) object;
		} else if (object instanceof Boolean) {
			string = object.toString();
		} else if (object instanceof Byte) {
			string = object.toString();
		} else if (object instanceof Character) {
			string = object.toString();
		} else if (object instanceof Float) {
			string = object.toString();
		} else if (object instanceof Integer) {
			string = object.toString();
		} else if (object instanceof Long) {
			string = object.toString();
		} else if (object instanceof Short) {
			string = object.toString();
		} else if (object instanceof Double) {
			string = object.toString();
		} else {
			string = this.toJson(object);
		}
		return string;
	}
	
	private String toJson(Object object) throws Exception {
		String json = null;
		if (object == null) {
			json = "";
		} else if (object instanceof String) {
			json = "\"" + ((String) object) + "\"";
		} else if (object instanceof Boolean) {
			json = object.toString();
		} else if (object instanceof Byte) {
			json = object.toString();
		} else if (object instanceof Character) {
			json = object.toString();
		} else if (object instanceof Float) {
			json = object.toString();
		} else if (object instanceof Integer) {
			json = object.toString();
		} else if (object instanceof Long) {
			json = object.toString();
		} else if (object instanceof Short) {
			json = object.toString();
		} else if (object instanceof Double) {
			json = object.toString();
		} else if (object instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>) object;
			json = "[" + this.toJson(collection) + "]";
		} else if (object instanceof Map<?,?>) {
			Map<?,?> map = (Map<?,?>) object;
			json = "{" + this.toJson(map.entrySet()) + "}";
		} else {
			json = "{" + this.toJson(object, object.getClass()) + "}";
		}
		return json;
	}

	private String toJson(Collection<?> collection) throws Exception {
		String json;
		StringBuilder builder = new StringBuilder(1024);
		boolean first = true;
		Iterator<?> iterator = collection.iterator();
		while (iterator.hasNext()) {
			Object element = iterator.next();
			if (first) {
				first = false;
			} else {
				builder.append(",");
			}
			String string = this.toJson(element);
			builder.append(string);
		}
		json = builder.toString();
		return json;
	}

	private String toJson(Object object, Class<?> type) throws Exception {
		if (type == null) {
			return "";
		} else {
			String upper = this.toJson(object, type.getSuperclass());
			boolean first = upper.length() == 0;
			StringBuilder builder = new StringBuilder(1024);
			builder.append(upper);
			Field[] fields = type.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Object element = field.get(object);
				if (element == null) {
					continue;
				} else {
					String name = field.getName();
					String value = this.toJson(element);
					String item = "\"" + name + "\":" + value;
					if (first) {
						first = false;
					} else {
						builder.append(",");						
					}
					builder.append(item);
				}
			}
			return builder.toString();
		}
	}

	protected final String getContent(String name) throws IOException {
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
	
}
