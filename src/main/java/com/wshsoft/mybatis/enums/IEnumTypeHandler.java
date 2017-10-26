package com.wshsoft.mybatis.enums;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.wshsoft.mybatis.toolkit.EnumUtils;

/**
 * <p>
 * 自定义枚举属性转换器
 * </p>
 *
 * @author Carry xie
 * @Date 2017-10-11
 */
public class IEnumTypeHandler<E extends Enum<?> & IEnum> extends BaseTypeHandler<IEnum> {

	private Class<E> type;

	public IEnumTypeHandler(Class<E> type) {
		if (type == null) {
			throw new IllegalArgumentException("Type argument cannot be null");
		}
		this.type = type;
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, IEnum parameter, JdbcType jdbcType)
			throws SQLException {
		if (jdbcType == null) {
			ps.setObject(i, parameter.getValue());
		} else {
			ps.setObject(i, parameter.getValue(), jdbcType.TYPE_CODE);
		}
	}

	@Override
	public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
		if (null == rs.getString(columnName) && rs.wasNull()) {
			return null;
		}
		return EnumUtils.valueOf(type, rs.getObject(columnName));
	}

	@Override
	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		if (null == rs.getString(columnIndex) && rs.wasNull()) {
			return null;
		}
		return EnumUtils.valueOf(type, rs.getObject(columnIndex));
	}

	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		if (null == cs.getString(columnIndex) && cs.wasNull()) {
			return null;
		}
		return EnumUtils.valueOf(type, cs.getObject(columnIndex));
	}
}
