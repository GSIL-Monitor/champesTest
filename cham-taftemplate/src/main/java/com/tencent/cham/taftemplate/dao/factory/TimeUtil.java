package com.tencent.cham.taftemplate.dao.factory;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间相关工具类
 * Created by lusiferli on 16/8/25.
 */
public class TimeUtil {

  public static final String FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";

  /**
   * .
   * 解析日期字符串
   *
   * @param dateStr 日期字符串
   * @return 时间戳
   */
  public static long parseStr(String dateStr) {
    if (StringUtils.isBlank(dateStr)) {
      return 0L;
    }
    Date date;
    try {
      SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
      date = sdf.parse(dateStr);
    } catch (ParseException parseException) {
      date = null;
    }
    if (date == null || date.before(new Date(1))) {
      return 0L;
    }
    return date.getTime();
  }
}
