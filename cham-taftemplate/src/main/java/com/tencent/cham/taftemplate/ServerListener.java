package com.tencent.cham.taftemplate;

import com.google.gson.Gson;
import com.tencent.bugly.taf.brave.BraveHolder;
import com.tencent.tc.monitor.mm.MigMonitorReporter;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;

import com.qq.cloud.taf.server.core.AppContextEvent;
import com.qq.cloud.taf.server.core.AppContextListener;
import com.qq.cloud.taf.server.core.AppServiceEvent;
import com.qq.cloud.taf.support.om.ConfigHelper;
import com.tencent.bugly.crashreport.CrashReportService;

import com.tencent.cham.taftemplate.util.Constant;
import com.tencent.cham.taftemplate.util.LogUtils;
import com.tencent.cham.taftemplate.util.ConfigUtils;

/**
 * taf启动类. 初始化相关配置.
 */
public class ServerListener implements AppContextListener {
  private static final Logger log = LogUtils.getNormalLogger();

  @Override
  public void appContextStarted(AppContextEvent appContextEvent) {
    log.info("appContextStarted begin......");

    // 加载配置文件
    loadConfig();

    // 初始化bugly上报监控
    if (Constant.BUGLY_MONITOR_ENABLE) {
      CrashReportService.initCrashReport(Constant.BUGLY_MONITOR_APPID, Constant.SERVER_NAME);
      log.info("appContextStarted init bugly crash report finish. appId:{}, serverName:{}", Constant.BUGLY_MONITOR_APPID,
          Constant.SERVER_NAME);
    }

    if (isSelfTestEnv()) {
      try {
        BraveHolder.setEnable(false);
        MigMonitorReporter.setNeedReport(false);
      } catch (Exception er) {
        log.warn("brave init fail.er:{}", er);
      }
    }

    log.info("appContextStarted finish");
  }

  private void loadConfig() {
    try {
      // 非自测环境，需要将远程的配置文件application.properties加载到本地
      if (!isSelfTestEnv()) {
        ConfigHelper.getInstance().loadConfig("application.properties");
        // 加载远程配置文件shiro.ini
        // ConfigHelper.getInstance().loadConfig("shiro.ini");
        // 加载远程配置文件log4j.properties
        ConfigHelper.getInstance().loadConfig("log4j.properties");
      }

      // 加载日志配置文件
      PropertyConfigurator.configure(System.getProperty("server.root") + "conf/log4j.properties");

      //打印配置项
      log.info("load properties hashmap: {},", new Gson().toJson(ConfigUtils.getMap()));

    } catch (Exception et) {
      log.error("load config fail.", et);
    }
  }

  /**
   * 判断是否是自测环境(自测环境启动服务时会设置is.selftest.env),如果是自测环境,可以根据情况设置stub以进行中型测试.
   *
   * @return 如果是自测环境返回true.
   */
  public static boolean isSelfTestEnv() {
    String isTestEnv = System.getProperty("is.selftest.env");
    return "true".equals(isTestEnv);
  }

  @Override
  public void appServiceStarted(AppServiceEvent event) {
    log.info("appServiceStarted begin");
    log.info("appServiceStarted finish.");
  }
}
