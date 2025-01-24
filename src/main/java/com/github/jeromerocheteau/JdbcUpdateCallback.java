package com.github.jeromerocheteau;

import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

public interface JdbcUpdateCallback<T> extends JdbcCallback {

	public T doMap(HttpServletRequest request, int count, ResultSet resultSet) throws Exception;

}
