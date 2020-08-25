package com.camelot.openplatform.dao.orm.mybatis;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class SerializableTypeHandler extends BaseTypeHandler<Serializable> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			Serializable parameter, JdbcType jdbcType) throws SQLException {
		ps.setObject(i, parameter);
	}

	@Override
	public Serializable getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		return (Serializable) rs.getObject(columnName);
	}

	@Override
	public Serializable getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		return (Serializable) rs.getObject(columnIndex);
	}

	@Override
	public Serializable getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		return (Serializable) cs.getObject(columnIndex);
	}

}
