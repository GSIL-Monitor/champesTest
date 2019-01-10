package com.tencent.cham.taftemplate.util;

/**
 * 常量类.
 */
public interface Constant {

  // 服务名,监控及其它系统上用于区别服务的名字
  public static final String SERVER_NAME = ConfigUtils.getString("server.name");

  // 是否开启mm监控上报
  public static final boolean MM_MONITOR_ENABLE = ConfigUtils.getBoolean("mm.monitor.enable");

  // 监控中服务id=ip+port,port在这里指定
  public static final int MM_MONITOR_PORT = ConfigUtils.getInt("mm.monitor.port");

  // 自身处理监控名
  public static final String MM_MONITOR_PROCESS = ConfigUtils.getString("mm.monitor.process");

  // 调用第三方监控名
  public static final String MM_MONITOR_INVOKE = ConfigUtils.getString("mm.monitor.invoke");


  // 是否开启bugly监控上报
  public static final boolean BUGLY_MONITOR_ENABLE = ConfigUtils.getBoolean("bugly.monitor.enable");

  // 发生异常后,异常上报到哪个appId.具体参考http://km.oa.com/articles/show/288556
  public static final String BUGLY_MONITOR_APPID = ConfigUtils.getString("bugly.monitor.appId");
}
