package com.example.bookmarketpassport.utils;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class ImageUtils extends BaseTypeHandler<String> {
    public static String encodeImageString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static InputStream decodeImageString(String imageString) {
        byte[] bytes = Base64.getDecoder().decode(imageString);
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        throw new UnsupportedOperationException("This type handler is read-only.");
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        byte[] bytes = rs.getBytes(columnName);
        return bytes != null ? encodeImageString(bytes) : null;
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        byte[] bytes = rs.getBytes(columnIndex);
        return bytes != null ? encodeImageString(bytes) : null;
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        byte[] bytes = cs.getBytes(columnIndex);
        return bytes != null ? encodeImageString(bytes) : null;
    }
}
