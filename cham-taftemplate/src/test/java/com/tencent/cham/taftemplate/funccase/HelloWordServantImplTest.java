package com.tencent.cham.taftemplate.funccase;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import runner.category.Middle;

import com.tencent.cham.taftemplate.TestUtils.ServantTestTools;
import com.tencent.cham.taftemplate.jce.servant.HelloWordPrx;

/**
 * 对服务的taf测试环境接口进行测试.
 * 
 * @author andytan
 * @since 2016年9月27日
 */
@Category(Middle.class)
public class HelloWordServantImplTest {

  private static final String objName = "HelloWordObj";

  @BeforeClass
  public static void setUpClass() {

  }

  @AfterClass
  public static void tearDownClass() {

  }

  private HelloWordPrx getHelloWordPrx() {
    return ServantTestTools.getProxy(HelloWordPrx.class, objName);
  }

  @Test
  public void testSayHello() {
    HelloWordPrx helloWordPrx = getHelloWordPrx();
    String response = helloWordPrx.sayHello();

    Assert.assertEquals("hello word", response);
  }
}
