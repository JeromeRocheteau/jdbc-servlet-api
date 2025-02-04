package com.github.jeromerocheteau.encoders;

import java.sql.PreparedStatement;
import java.sql.Types;

import javax.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class LongParameterEncoder implements JdbcEncoder {

	private int index;
	
	private String name;
	
	public LongParameterEncoder(int index, String name) {
		this.index = index;
		this.name = name;
	}
	
	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		String value = request.getParameter(this.name);
		if (value == null) {
			statement.setNull(this.index, Types.BIGINT);
		} else {
			Long number = Long.valueOf(value);
			statement.setLong(this.index, number.longValue());			
		}
	}

}