package com.github.jeromerocheteau.encoders;

import java.sql.PreparedStatement;
import java.sql.Types;

import javax.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class IntegerParameterEncoder implements JdbcEncoder {

	private int index;
	
	private String name;
	
	public IntegerParameterEncoder(int index, String name) {
		this.index = index;
		this.name = name;
	}
	
	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		String value = request.getParameter(this.name);
		if (value == null) {
			statement.setNull(this.index, Types.INTEGER);
		} else {
			Integer innumberteger = Integer.valueOf(value);
			statement.setInt(this.index, innumberteger.intValue());			
		}
	}

}