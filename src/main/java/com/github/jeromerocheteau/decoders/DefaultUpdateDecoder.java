package com.github.jeromerocheteau.decoders;

import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import com.github.jeromerocheteau.JdbcUpdateDecoder;

public class DefaultUpdateDecoder implements JdbcUpdateDecoder<Integer> {

	@Override
	public Integer doMap(HttpServletRequest request, int count, ResultSet resultSet) throws Exception {
		return count;
	}

}
