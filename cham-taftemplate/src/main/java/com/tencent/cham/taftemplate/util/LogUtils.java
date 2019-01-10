package com.tencent.cham.taftemplate.util;


import com.tencent.tc.monitor.mm.MigMonitorReporter;
import com.tencent.tc.monitor.mm.bean.InvokeLog;
import com.tencent.tc.monitor.mm.util.TafUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;

/**
 * Created by liyayxli on 2017/3/23.
 */
public class LogUtils {
  private static final Logger normal = LoggerFactory.getLogger("normal");
  private static final Logger access = LoggerFactory.getLogger("access");
  private static final Logger dbAnalysis = LoggerFactory.getLogger("dbAnalysis");

  public static final String MDC_SERIAL_NO = "SERIAL_NO";
  public static final String MDC_USER_ID = "USER_ID";
  private static final String MDC_CODE = "CODE";
  private static final String MDC_INTERFACE_NAME = "INTERFACE_NAME";
  private static final String MDC_ELAPSE = "ELAPSE";

  public static Logger getNormalLogger() {
    return normal;
  }

  public static Logger getAccessLogger() {
    return access;
  }

  public static Logger getDbAnalysisLogger() {
    return dbAnalysis;
  }

  /**
   * 初始化日志上下文信息.
   * @param context 请求上下文
   */
  public static void initLogMdc(Map<String, String> context, boolean checkRight) {
    String sn = "";
    String userId = "";
    if (context != null) {
      sn = context.get("sn");

      if (StringUtils.isBlank(sn)) {
        sn = UUID.randomUUID().toString();
      }

      if (checkRight) {
        // userId = AuthUtil.getUserId();
        userId = (userId == null ? "" : userId);
      } else {
        userId = context.get("userId");
        userId = (userId == null ? "" : userId);
      }
    }

    try {
      // 设置请求序列号SN
      if (StringUtils.isBlank(MDC.get(MDC_SERIAL_NO))) {
        MDC.put(MDC_SERIAL_NO, sn);
      }
      // 设置用户
      MDC.put(MDC_USER_ID, userId);
    } catch (Exception exception) {
      getNormalLogger().info("initLogMdc got exception: ", exception);
    }
  }

  /**
   * 记录用户请求.
   */
  public static void recordAccess(int returnCode, long elapse, String interfaceName) {
    try {
      MDC.put(MDC_CODE, String.valueOf(returnCode));
      MDC.put(MDC_ELAPSE, String.valueOf(elapse));
      MDC.put(MDC_INTERFACE_NAME, interfaceName);
      getAccessLogger().info("");
      // MDC清理
      MDC.clear();
    } catch (Exception exception) {
      getNormalLogger().info("recordAccess got exception: ", exception);
    }
  }

  /**
   * 上报第三方非taf服务 invoke log.
   */
  public static String reportInvokeMmLog(long startTime, int code, int result, String targetServer, String targetServerId, String action) {
    String env = ConfigUtils.getString("environment", "");
    if (env.equalsIgnoreCase("ST") || env.equalsIgnoreCase("LT")) {
      return "";
    }

    try {
      long duration = System.currentTimeMillis() - startTime;

      InvokeLog invokeLog = new InvokeLog();
      // 主调服务名
      invokeLog.setServerName(TafUtil.getServerName());
      // 被调服务名
      invokeLog.setTargetServerName(targetServer);
      // 被调服务Id（ip+port）
      invokeLog.setTargetServerId(targetServerId);
      // 在对象实体上实施的行为
      invokeLog.setAction(action);
      // 操作对象实体(如表名)
      invokeLog.setEntry("-");
      invokeLog.setResult(result);
      invokeLog.setCost(duration);
      invokeLog.setStatusCode(Integer.toString(code));
      //  上报调用监控项
      MigMonitorReporter.report(invokeLog);
      return invokeLog.getLog();
    } catch (Exception ex) {
      ex.printStackTrace();
      return "";
    }
  }
}
