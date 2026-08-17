package com.github.jeromerocheteau.encoders;

import java.sql.PreparedStatement;
import java.sql.Types;

import jakarta.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class DoubleValueEncoder implements JdbcEncoder {

	private int index;
	
	private Double value;
	
	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
		
	public DoubleValueEncoder(int index) {
		this.index = index;
	}

	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		if (value == null) {
			statement.setNull(this.index, Types.DOUBLE);
		} else {
			statement.setDouble(this.index, value);
		}
	}

}
