package com.github.jeromerocheteau;

import java.sql.ResultSet;

import jakarta.servlet.http.HttpServletRequest;

public interface JdbcUpdateDecoder<T> {

	public T doMap(HttpServletRequest request, int count, ResultSet resultSet) throws Exception;

}
