package com.github.jeromerocheteau.encoders;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;

import javax.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class TimestampValueEncoder implements JdbcEncoder {

	private int index;
	
	private Timestamp value;
	
	public Timestamp getValue() {
		return value;
	}

	public void setValue(Timestamp value) {
		this.value = value;
	}
		
	public TimestampValueEncoder(int index) {
		this.index = index;
	}

	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		if (value == null) {
			statement.setNull(this.index, Types.TIMESTAMP);
		} else {
			statement.setTimestamp(this.index, value);
		}
	}

}
