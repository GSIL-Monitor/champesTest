package com.tencent.cham.taftemplate.unitcase.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.google.gson.Gson;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import runner.category.Small;

import com.tencent.cham.taftemplate.util.ConfigUtils;

@Category({Small.class})
public class ConfigUtilsTest {
  @BeforeClass
  public static void setUpClass() {

  }

  @AfterClass
  public static void tearDownClass() {
    System.out.println("before:" + new Gson().toJson(ConfigUtils.getMap()));
    ConfigUtils.setMap(ConfigUtils.load(""));
    System.out.println("after:" + new Gson().toJson(ConfigUtils.getMap()));
  }

  @Test
  public void test() {
    InputStream inputStream = new ByteArrayInputStream("server.name=PipelineStoreServer\nmm.monitor.enable=true\ntest.int=1".getBytes());
    ConfigUtils.setMap(ConfigUtils.load(inputStream));

    Assert.assertEquals(ConfigUtils.getString("server.name"), "PipelineStoreServer");
    Assert.assertEquals(ConfigUtils.getString("server.name.no", "test"), "test");

    Assert.assertEquals(ConfigUtils.getBoolean("mm.monitor.enable"), true);
    Assert.assertEquals(ConfigUtils.getBoolean("mm.monitor.enable.no", false), false);

    Assert.assertEquals(ConfigUtils.getInt("test.int"), 1);
    Assert.assertEquals(ConfigUtils.getInt("test.int.no", 2), 2);

    Assert.assertEquals(ConfigUtils.getLong("test.int"), 1);
    Assert.assertEquals(ConfigUtils.getLong("test.int.no", 2), 2);
  }

  @Test
  public void testGetRemote() {
    Assert.assertNull(ConfigUtils.getRemoteConfig());
    Assert.assertEquals(ConfigUtils.getIntRemoteConfVal("int.remote.name"), 0);
    Assert.assertEquals(ConfigUtils.getIntRemoteConfVal("int.remote.name", 1), 1);
    Assert.assertEquals(ConfigUtils.getBooleanRemoteConfVal("int.remote.name", true), true);
  }
}
