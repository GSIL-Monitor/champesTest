package com.tencent.cham.taftemplate.deploy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tencent.bugly.monitor.mm.MigMonitorReporter;
import com.tencent.bugly.monitor.mm.bean.Constant;
import com.tencent.bugly.monitor.mm.bean.ProcessLog;
import com.tencent.bugly.taf.aspect.TafServant;

import com.tencent.cham.taftemplate.util.LogUtils;

import com.tencent.cham.taftemplate.jce.servant.HelloWordServant;


/**
 * hellword示例服务,线上服务删除.
 *
 * @author andytan
 * @version 2017年4月8日 上午11:01:44
 * @since JDK1.8
 */
@TafServant
public class HelloWordServantImpl implements HelloWordServant {

  private static final Logger log = LogUtils.getNormalLogger();
  
  /**
   * 示例接口.
   */
  public String sayHello() {
    long begin = System.currentTimeMillis();

    String response = "hello word";
    log.info("rec sayHello request.return:{}", response);
    // 记录mm监控日志
    ProcessLog processLog = new ProcessLog();
    processLog.setCost(System.currentTimeMillis() - begin);
    processLog.setAppId("");
    processLog.setResult(Constant.RESULT_SUCCESS);
    processLog.setReturnCode("");
    processLog.setCmd("sayHello");
    MigMonitorReporter.report(processLog);

    return response;
  }
}
