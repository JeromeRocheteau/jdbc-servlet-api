package com.github.jeromerocheteau;

import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

public interface JdbcUpdateDecoder<T> {

	public T doMap(HttpServletRequest request, int count, ResultSet resultSet) throws Exception;

}
