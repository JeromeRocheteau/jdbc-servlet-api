package com.github.jeromerocheteau.encoders;

import java.sql.PreparedStatement;
import java.sql.Types;

import javax.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class IntegerValueEncoder implements JdbcEncoder {

	private int index;
	
	private Integer value;
	
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
		
	public IntegerValueEncoder(int index) {
		this.index = index;
	}

	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		if (value == null) {
			statement.setNull(this.index, Types.INTEGER);
		} else {
			statement.setInt(this.index, value);
		}
	}

}
