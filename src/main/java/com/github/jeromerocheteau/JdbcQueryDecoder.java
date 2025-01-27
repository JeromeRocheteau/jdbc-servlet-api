package com.github.jeromerocheteau;

import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

public interface JdbcQueryDecoder<T> {

	public T doMap(HttpServletRequest request, ResultSet resultSet) throws Exception;

}
