package com.github.jeromerocheteau.encoders;

import java.sql.PreparedStatement;
import java.sql.Types;

import javax.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class BooleanParameterEncoder implements JdbcEncoder {

	private int index;
	
	private String name;
	
	public BooleanParameterEncoder(int index, String name) {
		this.index = index;
		this.name = name;
	}
	
	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		String value = request.getParameter(this.name);
		if (value == null) {
			statement.setNull(this.index, Types.BOOLEAN);
		} else {
			Boolean number = Boolean.valueOf(value);
			statement.setBoolean(this.index, number.booleanValue());			
		}
	}

}