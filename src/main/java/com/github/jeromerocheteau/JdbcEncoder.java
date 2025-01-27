package com.github.jeromerocheteau;

import java.sql.PreparedStatement;

import javax.servlet.http.HttpServletRequest;

public interface JdbcEncoder {

	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception;
	
}
