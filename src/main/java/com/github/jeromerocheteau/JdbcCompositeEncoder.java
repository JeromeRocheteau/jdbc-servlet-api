package com.github.jeromerocheteau;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class JdbcCompositeEncoder implements JdbcEncoder {

	private List<JdbcEncoder> encoders;
	
	public JdbcCompositeEncoder(JdbcEncoder... encoders) {
		this.encoders = Arrays.asList(encoders);
	}
	
	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		for (JdbcEncoder encoder : encoders) {
			encoder.doFill(statement, request);
		}
	}

}
