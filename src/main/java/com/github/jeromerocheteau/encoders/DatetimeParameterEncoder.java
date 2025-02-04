package com.github.jeromerocheteau.encoders;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class DatetimeParameterEncoder implements JdbcEncoder {

	private DateFormat parser;
	
	private int index;
	
	private String name;
	
	public DatetimeParameterEncoder(int index, String name) {
		this.parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.index = index;
		this.name = name;
	}
	
	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		String value = request.getParameter(this.name);
		if (value == null) {
			statement.setNull(this.index, Types.TIMESTAMP);
		} else {
			Timestamp timestamp = new Timestamp(this.parser.parse(value).getTime());
			statement.setTimestamp(this.index, timestamp);
		}
	}

}