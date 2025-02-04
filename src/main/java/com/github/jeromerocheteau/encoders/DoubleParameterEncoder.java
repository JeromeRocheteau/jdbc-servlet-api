package com.github.jeromerocheteau.encoders;

import java.sql.PreparedStatement;
import java.sql.Types;

import javax.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class DoubleParameterEncoder implements JdbcEncoder {

	private int index;
	
	private String name;
	
	public DoubleParameterEncoder(int index, String name) {
		this.index = index;
		this.name = name;
	}
	
	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		String value = request.getParameter(this.name);
		if (value == null) {
			statement.setNull(this.index, Types.DOUBLE);
		} else {
			Double number = Double.valueOf(value);
			statement.setDouble(this.index, number.doubleValue());			
		}
	}

}