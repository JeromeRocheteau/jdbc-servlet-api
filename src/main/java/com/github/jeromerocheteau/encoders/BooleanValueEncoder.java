package com.github.jeromerocheteau.encoders;

import java.sql.PreparedStatement;
import java.sql.Types;

import javax.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class BooleanValueEncoder implements JdbcEncoder {

	private int index;
	
	private Boolean value;
	
	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}
		
	public BooleanValueEncoder(int index) {
		this.index = index;
	}

	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		if (value == null) {
			statement.setNull(this.index, Types.BOOLEAN);
		} else {
			statement.setBoolean(this.index, value);
		}
	}

}
