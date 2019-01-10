package com.tencent.cham.taftemplate.dao.factory;

import com.tencent.cham.taftemplate.util.ConfigUtils;
import com.tencent.cham.taftemplate.util.LogUtils;
import com.tencent.tc.monitor.mm.MigMonitorReporter;
import com.tencent.tc.monitor.mm.bean.Constant;
import com.tencent.tc.monitor.mm.bean.InvokeLog;
import com.tencent.tc.monitor.mm.util.TafUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;


/**
 * 监控sql的执行情况，以mapper id标识.
 */
@Intercepts( {@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,
        ResultHandler.class})})
public class SqlExecuteElapseHandler implements Interceptor {

  protected static final Logger log = LogUtils.getDbAnalysisLogger();
  private static final String SERVER_NAME_MDB = "MDB";
  private Properties properties;

  public static final long SQL_TIME_OUT = 1000;

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
    Object parameter = null;
    if (invocation.getArgs().length > 1) {
      parameter = invocation.getArgs()[1];
    }
    // 定义的sql的id, 例如 : <select id="query" ></select> 中的query
    final String sqlId = mappedStatement.getId();
    BoundSql boundSql = mappedStatement.getBoundSql(parameter);
    Configuration configuration = mappedStatement.getConfiguration();
    Object returnValue = null;
    boolean isException = false;

    // 真实执行源sql; 进行耗时统计以及异常判断
    long start = System.currentTimeMillis();
    try {
      returnValue = invocation.proceed();
    } catch (Exception exception) {
      isException = true;
      log.error("db execute except. boundSql={}.", boundSql.getSql(), exception);
    }
    // 耗时
    long duration = (System.currentTimeMillis() - start);

    boolean isTimeout = isTimeout(duration);

    //获取数据库url
    String invokeUrl = getDataBaseUrl(configuration);
    // 替换参数, 拿到最终的sql语句
    String sql = showSql(configuration, boundSql);
    log.info("{} | isTimeout={},isException={} | db url is {}", sql, isTimeout, isException, invokeUrl);


    sendMm(invokeUrl, sqlId, start, isTimeout, isException);
    return returnValue;
  }

  private void sendMm(String invokeUrl, String sqlId, long start, boolean isTimeout, boolean isException) {
    String env = ConfigUtils.getString("environment", "");
    if (env.equalsIgnoreCase("ST") || env.equalsIgnoreCase("LT")) {
      return;
    }

    int result = Constant.RESULT_FAIL;
    if (!isTimeout && !isException) {
      result = Constant.RESULT_SUCCESS;
    }

    String mmLog = LogUtils.reportInvokeMmLog(start, 0, result, SERVER_NAME_MDB, invokeUrl, sqlId);
    log.info("reportLog db execute to mm, detail = {}", mmLog);
  }

  /**
   * 判断SQL是否超时.
   *
   * @param duration sql耗时
   * @return 是否超时
   */
  private boolean isTimeout(long duration) {
    return duration > SQL_TIME_OUT;
  }


  /**
   * 按名称取传入的参数.
   *
   * @param obj 参数名称
   * @return 参数值
   */
  private static String getParameterValue(Object obj) {
    String value = null;
    if (obj instanceof String) {
      value = "'" + obj.toString() + "'";
    } else if (obj instanceof Date) {
      DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
      value = "'" + formatter.format(new Date()) + "'";
    } else {
      if (obj != null) {
        value = obj.toString();
      } else {
        value = "";
      }
    }
    return value;
  }

  /**
   * 将完整的SQL语句还原出来.
   *
   * @param configuration 配置
   * @param boundSql map中的sql文件
   * @return 真实运行的sql
   */
  private static String showSql(Configuration configuration, BoundSql boundSql) {
    String sql = "";
    try {
      Object parameterObject = boundSql.getParameterObject();
      List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
      sql = boundSql.getSql().replaceAll("[\\s]+", " ");
      if (parameterMappings.size() > 0 && parameterObject != null) {
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
          sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
        } else {
          MetaObject metaObject = configuration.newMetaObject(parameterObject);
          for (ParameterMapping parameterMapping : parameterMappings) {
            String propertyName = parameterMapping.getProperty();
            if (metaObject.hasGetter(propertyName)) {
              Object obj = metaObject.getValue(propertyName);
              sql = sql.replaceFirst("\\?", getParameterValue(obj));
            } else if (boundSql.hasAdditionalParameter(propertyName)) {
              Object obj = boundSql.getAdditionalParameter(propertyName);
              sql = sql.replaceFirst("\\?", getParameterValue(obj));
            }
          }
        }
      }
    } catch (Exception exception) {
      log.error("parse sql error.", exception);
    }
    return sql;
  }

  private String getDataBaseUrl(Configuration configuration) {
    //数据库名，如rdm
    String environmentId = configuration.getEnvironment().getId();

    //获取：rdm.url=jdbc:mysql://10.219.131.28:8808/db_rdm?useUnicode=true&characterEncoding=UTF-8
    String url = ConfigUtils.getString(environmentId + ".url");

    //以?分隔
    StringTokenizer strT = new StringTokenizer(url, "?");

    //返回?之前内容
    return strT.nextToken();
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {
    this.properties = properties;
  }
}
