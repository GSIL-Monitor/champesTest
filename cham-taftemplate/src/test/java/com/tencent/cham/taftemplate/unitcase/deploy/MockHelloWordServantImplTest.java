package com.tencent.cham.taftemplate.unitcase.deploy;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.powermock.modules.junit4.rule.PowerMockRule;

import runner.category.Small;
import runner.category.Smoke;

import com.tencent.bugly.monitor.mm.MigMonitorReporter;
import com.tencent.cham.taftemplate.deploy.HelloWordServantImpl;

/**
 * 对服务的接口进行测试,关联第三方使用mock方式构造出来.
 * 
 * @author andytan
 * @since 2016年9月27日
 */
@Category({Small.class, Smoke.class})
public class MockHelloWordServantImplTest {

  @Rule
  public PowerMockRule rule = new PowerMockRule();

  private static HelloWordServantImpl storeProxyServant = new HelloWordServantImpl();

  @BeforeClass
  public static void setUpClass() {
    // 关闭mm监控上报
    MigMonitorReporter.setNeedReport(false);
  }

  /**
   * 执行完后处理一些事件.
   *
   */
  @AfterClass
  public static void tearDownClass() {}

  /**
   * 测试插入场景.
   */
  @Test
  public void testInsert() {
    String response = storeProxyServant.sayHello();
    Assert.assertEquals("hello word", response);
  }
}
