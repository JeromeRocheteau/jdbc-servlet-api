package com.github.jeromerocheteau.encoders;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class TimeParameterEncoder implements JdbcEncoder {

	private DateFormat parser;
	
	private int index;
	
	private String name;
	
	public TimeParameterEncoder(int index, String name) {
		this.parser = new SimpleDateFormat("HH:mm:ss");
		this.index = index;
		this.name = name;
	}
	
	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		String value = request.getParameter(this.name);
		if (value == null) {
			statement.setNull(this.index, Types.TIME);
		} else {
			Time time = new Time(this.parser.parse(value).getTime());
			statement.setTime(this.index, time);
		}
	}

}