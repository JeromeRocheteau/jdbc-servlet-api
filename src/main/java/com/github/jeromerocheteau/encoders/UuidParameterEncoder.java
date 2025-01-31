package com.github.jeromerocheteau.encoders;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class UuidParameterEncoder implements JdbcEncoder {

	private int index;
	
	private String name;
	
	public UuidParameterEncoder(int index, String name) {
		this.index = index;
		this.name = name;
	}
	
	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		String value = request.getParameter(this.name);
		if (value == null) {
			statement.setNull(this.index, Types.VARCHAR);
		} else {
			UUID uuid = UUID.fromString(value);
			statement.setString(this.index, uuid.toString());			
		}
	}

}