package com.github.jeromerocheteau.encoders;

import java.sql.PreparedStatement;

import javax.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class DefaultEncoder implements JdbcEncoder {

	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		
	}

}
