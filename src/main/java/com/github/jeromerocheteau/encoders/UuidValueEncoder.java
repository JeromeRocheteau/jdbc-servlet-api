package com.github.jeromerocheteau.encoders;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcEncoder;

public class UuidValueEncoder implements JdbcEncoder {

	private int index;
	
	private UUID value;
	
	public UUID getValue() {
		return value;
	}

	public void setValue(UUID value) {
		this.value = value;
	}
		
	public UuidValueEncoder(int index) {
		this.index = index;
	}

	@Override
	public void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception {
		if (value == null) {
			statement.setNull(this.index, Types.VARCHAR);
		} else {
			statement.setString(this.index, value.toString());
		}
	}

}
