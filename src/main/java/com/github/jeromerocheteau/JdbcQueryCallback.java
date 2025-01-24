package com.github.jeromerocheteau;

import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

public interface JdbcQueryCallback<T> extends JdbcCallback {

	public T doMap(HttpServletRequest request, ResultSet resultSet) throws Exception;

}
