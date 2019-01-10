package com.tencent.cham.taftemplate.TestUtils;

import com.qq.cloud.taf.client.Communicator;
import com.qq.cloud.taf.client.CommunicatorConfig;
import com.qq.cloud.taf.client.CommunicatorFactory;
import com.tencent.cham.taftemplate.ServerListener;
import com.tencent.cham.taftemplate.util.LogUtils;
import org.slf4j.Logger;

/**
 * Created by liyayxli on 2017/4/24.
 */
public class ServantTestTools {
  private static final Logger log = LogUtils.getNormalLogger();

  private static String locator = "cham.TafTemplateServer.OBJ_NAME@tcp -h 100.115.151.196 -t 60000 -p 8080";

  // taf 147测试环境地址(统一都是100.115.151.196,不需要修改)
  private static String servant_147 = "cham.TafTemplateServer.OBJ_NAME@tcp -h 100.115.151.196 -t 60000 -p 8080";

  // docker上自测环境地址,这里需填入自测环境的ip及端口。中型测试使用
  private static String servant_self_test = "cham.TafTemplateServer.OBJ_NAME@tcp -h %1$s -t 60000 -p %2$s";


  /**
   * 获取客户端代理.
   */
  public static <T> T getProxy(Class<T> clazz, String objName) {
    Communicator communicator = CommunicatorFactory.getInstance().getCommunicator(locator.replaceAll("OBJ_NAME", objName));
    CommunicatorConfig config = communicator.getCommunicatorConfig();
    config.setCharsetName("UTF-8");
    config.setStat("taf.tafstat.StatObj");
    //设置同步调用超时时间为5000ms
    //config.setSyncInvokeTimeout(5000);
    String servantAddr = null;
    if (ServerListener.isSelfTestEnv()) {
      String selftestIp = System.getProperty("selftest.ip");
      String selftestPort = System.getProperty("selftest.port");
      servantAddr = String.format(servant_self_test.replaceAll("OBJ_NAME", objName), selftestIp, selftestPort);
      log.info("selftestIp is {}, selftestPort is {}, servantAddr is {}", selftestIp, selftestPort, servantAddr);
    } else {
      servantAddr = servant_147.replaceAll("OBJ_NAME", objName);
    }

    // 生成客户端代理
    return communicator.stringToProxy(clazz, servantAddr);
  }

}
