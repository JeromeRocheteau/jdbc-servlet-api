package com.github.jeromerocheteau.encoders;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class DateParameterEncoder implements JdbcEncoder {

	private DateFormat parser;
	
	private int index;
	
	private String name;
	
	public DateParameterEncoder(int index, String name) {
		this.parser = new SimpleDateFormat("yyyy-MM-dd");
		this.index = index;
		this.name = name;
	}
	
	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		String value = request.getParameter(this.name);
		if (value == null) {
			statement.setNull(this.index, Types.DATE);
		} else {
			Date date = new Date(parser.parse(value).getTime());
			statement.setDate(this.index, date);
		}
	}

}