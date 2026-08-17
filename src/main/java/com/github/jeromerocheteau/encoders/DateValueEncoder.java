package com.github.jeromerocheteau.encoders;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;

import jakarta.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class DateValueEncoder implements JdbcEncoder {

	private int index;
	
	private Date value;
	
	public Date getValue() {
		return value;
	}

	public void setValue(Date value) {
		this.value = value;
	}
		
	public DateValueEncoder(int index) {
		this.index = index;
	}

	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		if (value == null) {
			statement.setNull(this.index, Types.DATE);
		} else {
			statement.setDate(this.index, value);
		}
	}

}
