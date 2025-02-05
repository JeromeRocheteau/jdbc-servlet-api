package com.github.jeromerocheteau.encoders;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;

import javax.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class TimestampParameterEncoder implements JdbcEncoder {

	private int index;
	
	private String name;
	
	public TimestampParameterEncoder(int index, String name) {
		this.index = index;
		this.name = name;
	}
	
	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		String value = request.getParameter(this.name);
		if (value == null) {
			statement.setNull(this.index, Types.TIMESTAMP);
		} else {
			Long time = Long.valueOf(value);
			Timestamp ts = new Timestamp(time.longValue());
			System.out.println(value + "\t" + time.toString() + "\t" + ts.toString());
			statement.setTimestamp(this.index, ts);			
		}
	}

}