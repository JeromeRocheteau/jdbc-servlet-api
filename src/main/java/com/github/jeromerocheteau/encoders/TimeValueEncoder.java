package com.github.jeromerocheteau.encoders;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.sql.Types;

import javax.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class TimeValueEncoder implements JdbcEncoder {

	private int index;
	
	private Time value;
	
	public Time getValue() {
		return value;
	}

	public void setValue(Time value) {
		this.value = value;
	}
		
	public TimeValueEncoder(int index) {
		this.index = index;
	}

	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		if (value == null) {
			statement.setNull(this.index, Types.TIME);
		} else {
			statement.setTime(this.index, value);
		}
	}

}
