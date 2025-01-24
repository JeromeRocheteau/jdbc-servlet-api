package com.github.jeromerocheteau;

import java.sql.PreparedStatement;

import javax.servlet.http.HttpServletRequest;

public interface JdbcCallback {

	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception;
	
}
