package com.tencent.cham.taftemplate.dao.factory;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

@MappedJdbcTypes({JdbcType.TIME})
public class MyDateTypeHandler implements TypeHandler<Long> {

  @Override
  public Long getResult(ResultSet arg0, String arg1) throws SQLException {
    String dateStr = arg0.getString(arg1);
    if (null == dateStr) {
      return 0L;
    }
    return TimeUtil.parseStr(dateStr);
  }

  @Override
  public Long getResult(ResultSet arg0, int arg1) throws SQLException {
    String dateStr = arg0.getString(arg1);
    if (null == dateStr) {
      return 0L;
    }
    return TimeUtil.parseStr(dateStr);
  }

  @Override
  public Long getResult(CallableStatement arg0, int arg1) throws SQLException {
    String dateStr = arg0.getString(arg1);
    if (null == dateStr) {
      return 0L;
    }
    return TimeUtil.parseStr(dateStr);
  }

  @Override
  public void setParameter(PreparedStatement arg0, int arg1, Long arg2, JdbcType arg3)
          throws SQLException {
    if (arg2 == null) {
      arg2 = 0L;
    }
    Date date = new Date(arg2);
    SimpleDateFormat sdf = new SimpleDateFormat(TimeUtil.FORMAT_DATE);
    String datetime = sdf.format(date);
    arg0.setString(arg1, datetime);
  }
}
