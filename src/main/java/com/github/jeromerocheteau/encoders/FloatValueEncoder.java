package com.github.jeromerocheteau.encoders;

import java.sql.PreparedStatement;
import java.sql.Types;

import javax.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class FloatValueEncoder implements JdbcEncoder {

	private int index;
	
	private Float value;
	
	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}
		
	public FloatValueEncoder(int index) {
		this.index = index;
	}

	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		if (value == null) {
			statement.setNull(this.index, Types.FLOAT);
		} else {
			statement.setFloat(this.index, value);
		}
	}

}
