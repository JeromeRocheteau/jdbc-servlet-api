package com.github.jeromerocheteau.encoders;

import java.sql.PreparedStatement;
import java.sql.Types;

import jakarta.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class LongValueEncoder implements JdbcEncoder {

	private int index;
	
	private Long value;
	
	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}
		
	public LongValueEncoder(int index) {
		this.index = index;
	}

	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		if (value == null) {
			statement.setNull(this.index, Types.BIGINT);
		} else {
			statement.setLong(this.index, value);
		}
	}

}
