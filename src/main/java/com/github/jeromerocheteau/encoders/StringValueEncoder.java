package com.github.jeromerocheteau.encoders;

import java.sql.PreparedStatement;
import java.sql.Types;

import jakarta.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class StringValueEncoder implements JdbcEncoder {

	private int index;
	
	private String value;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
		
	public StringValueEncoder(int index) {
		this.index = index;
	}

	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		if (value == null) {
			statement.setNull(this.index, Types.VARCHAR);
		} else {
			statement.setString(this.index, value);
		}
	}

}
