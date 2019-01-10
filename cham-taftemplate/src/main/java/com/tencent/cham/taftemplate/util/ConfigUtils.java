package com.tencent.cham.taftemplate.util;

import com.qq.cloud.taf.server.config.ServerConfig;
import com.qq.cloud.taf.support.om.ConfigHelper;
import com.tencent.bugly.monitor.servicereport.util.ZkRemoteConfig;
import com.tencent.cham.taftemplate.ServerListener;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 读取配置文件配置工具类(读取taf远程配置文件application.properties).
 *
 * @author andytan
 * @version 2017年4月8日 上午11:14:03
 * @since JDK1.8
 */
public class ConfigUtils {
  private static final Logger logger = LogUtils.getNormalLogger();

  private static ZkRemoteConfig remoteConfig = null;

  // 程序用到的公共变量
  private static final String FILE_ENCODING = "utf-8";

  private static final String APP_CONFIG_FILE_NAME = "application.properties";

  private static HashMap<String, String> map = null;

  static {
    if (!ServerListener.isSelfTestEnv()) {
      try {
        ConfigHelper.getInstance().loadConfig("application.properties");
      } catch (Exception ex) {
        logger.error("load remote config fail.", ex);
      }
    }
    initConfig(null);
  }

  /**
   * 从配置文件中加载map.
   */
  public static void initConfig(String fileName) {
    if (StringUtils.isBlank(fileName)) {
      fileName = APP_CONFIG_FILE_NAME;
    }

    map = load(fileName);
  }

  /**
   * 设置map为指定内容.
   */
  public static void setMap(HashMap<String, String> configMap) {
    map = configMap;
  }

  /**
   * 返回当前配置项.
   */
  public static HashMap<String, String> getMap() {
    return map;
  }

  /**
   * 根据文件名加载配置项.
   *
   * @param fileName 文件名
   */
  public static HashMap<String, String> load(String fileName) {
    if (StringUtils.isBlank(fileName)) {
      logger.warn("application fileName is blank!");
      return null;
    }
    InputStream in = ServerConfig.class.getClassLoader().getResourceAsStream(fileName);
    return load(in);
  }

  /**
   * 根据文件名加载配置项.
   * 
   * @param in 输入流
   */
  public static HashMap<String, String> load(InputStream in) {
    HashMap<String, String> map = new HashMap<String, String>();
    logger.info("start to init application.properties......");
    try {
      PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();

      if (in != null) {
        propertiesConfiguration.load(in, FILE_ENCODING);
        propertiesConfiguration.setReloadingStrategy(new FileChangedReloadingStrategy());
        for (Iterator it = propertiesConfiguration.getKeys(); it != null && it.hasNext();) {
          String key = (String) it.next();
          if (key != null && !key.equals("")) {
            key = key.trim();
            if (!"".equals(key)) {
              String value = propertiesConfiguration.getString(key);
              logger.info(key + ":" + value);
              map.put(key, value);
            }
          }
        }
      } else {
        logger.error("loadConfiguration InputStream is null!");
      }
    } catch (Exception er) {
      logger.error("loadConfiguration error!" + er);
    } finally {
      close(in);
    }
    return map;
  }

  private static void close(InputStream in) {
    if (in != null) {
      try {
        in.close();
      } catch (IOException er) {
        logger.error("loadConfiguration close error!" + er);
      }
    }
  }

  /**
   * 设置String.
   */
  public static void setString(String key, String value) {
    if (map != null && key != null) {
      map.put(key, value);
    }
  }

  /**
   * 获取string类型配置项.
   * 
   * @param key 配置项key值
   * @param defaultValue 如果为空返回默认值
   * @return 返回配置项对应的value
   */
  public static String getString(String key, String defaultValue) {
    String val = getString(key);
    if (val == null) {
      return defaultValue;
    } else {
      return val;
    }
  }

  /**
   * 获取string类型配置.
   * 
   * @param key 配置的key值
   * @return 返回val,不存在则是null
   */
  public static String getString(String key) {
    String value = null;
    if (map == null || map.isEmpty()) {
      logger.info("map is null, reload....");
      map = load(APP_CONFIG_FILE_NAME);
    }
    if (map != null && key != null) {
      value = map.get(key);
    }
    return value;
  }

  /**
   * 获取int类型配置项.
   * 
   * @param key 配置项key值
   * @param defaultValue 如果为空返回默认值
   * @return 返回配置项对应的value
   */
  public static int getInt(String key, int defaultValue) {
    String val = getString(key);
    if (val == null) {
      return defaultValue;
    } else {
      try {
        return Integer.parseInt(val);
      } catch (Exception er) {
        logger.error("Integer.parseInt error " + er.getMessage());
        return 0;
      }
    }
  }

  /**
   * 获取int类型配置项.
   * 
   * @param key 配置项key值
   * @return 返回配置项对应的value
   */
  public static int getInt(String key) {
    int result = 0;
    String str = getString(key);
    if (str != null) {
      try {
        result = Integer.parseInt(str);
      } catch (Exception er) {
        logger.error("Integer.parseInt error " + er.getMessage());
      }
    }
    return result;
  }

  /**
   * 获取boolean类型配置项.
   * 
   * @param key 配置项key值
   * @param defaultValue 如果为空返回默认值
   * @return 返回配置项对应的value
   */
  public static boolean getBoolean(String key, boolean defaultValue) {
    String str = getString(key);
    if (str == null) {
      return defaultValue;
    } else {
      return str.equalsIgnoreCase("true");
    }
  }

  /**
   * 获取boolean类型配置项.
   * 
   * @param key 配置项key值
   * @return 返回配置项对应的value
   */
  public static boolean getBoolean(String key) {
    boolean flag = false;
    String str = getString(key);
    if (str != null && str.equalsIgnoreCase("true")) {
      flag = true;
    }
    return flag;
  }

  /**
   * 获取long类型配置项.
   * 
   * @param key 配置项key值
   * @param defaultValue 如果为空返回默认值
   * @return 返回配置项对应的value
   */
  public static long getLong(String key, long defaultValue) {
    long result = 0L;
    String str = getString(key);
    if (str != null) {
      try {
        result = Long.parseLong(str);
      } catch (Exception er) {
        logger.error("Long.parseLong error " + er.getMessage());
      }
    } else {
      return defaultValue;
    }
    return result;
  }

  /**
   * 获取long类型配置项.
   * 
   * @param key 配置项key值
   * @return 返回配置项对应的value
   */
  public static long getLong(String key) {
    long result = 0L;
    String str = getString(key);
    if (str != null) {
      try {
        result = Long.parseLong(str);
      } catch (Exception er) {
        logger.error("Long.parseLong error " + er.getMessage());
      }
    }
    return result;
  }


  public static ZkRemoteConfig getRemoteConfig() {
    return remoteConfig;
  }

  /**
   * 如果使用了zookeeper远程配置,需要在服务初始化时设置zookeeper设置.
   *
   * @param remoteConfig zookeeper远程配置
   */
  public static void setRemoteConfig(ZkRemoteConfig remoteConfig) {
    ConfigUtils.remoteConfig = remoteConfig;
  }

  /**
   * 获取远程(zookeeper)string类型配置.
   * 
   * @param name 配置名
   * @return 返回对应的配置val.不存在则返回null
   */
  public static String getRemoteConfVal(String name) {
    if (remoteConfig != null) {
      try {
        return remoteConfig.get(name);
      } catch (Exception er) {
        logger.warn("getRemoteConfVal name:{}, fail:{}", name, er);
      }
    } else {
      logger.warn("getRemoteConfVal and zkRemote is null name:{}", name);
    }
    return null;
  }

  /**
   * 获取远程(zookeeper)int类型配置.
   * 
   * @param name 配置名
   * @return 返回对应的配置val.不存在则返回默认值
   */
  public static int getIntRemoteConfVal(String name) {
    String val = getRemoteConfVal(name);
    if (StringUtils.isNotBlank(val)) {
      try {
        return Integer.parseInt(val);
      } catch (NumberFormatException er) {
        logger.warn("getIntRemoteConfVal and NumberFormatException name:{}, e:{}", name, er);
        return 0;
      }
    }
    return 0;
  }

  /**
   * 获取远程(zookeeper)int类型配置.
   * 
   * @param name 配置名
   * @param defaultVal 不存在时返回默认值
   * @return 返回对应的配置val.不存在则返回默认值
   */
  public static int getIntRemoteConfVal(String name, int defaultVal) {
    String val = getRemoteConfVal(name);
    if (StringUtils.isNotBlank(val)) {
      try {
        return Integer.parseInt(val);
      } catch (NumberFormatException er) {
        logger.warn("getIntRemoteConfVal and NumberFormatException name:{}, e:{}", name, er);
        return defaultVal;
      }
    }
    return defaultVal;
  }

  /**
   * 获取远程(zookeeper)boolean类型配置.
   * 
   * @param name 配置名
   * @param defaultVal 不存在时返回默认值
   * @return 返回对应的配置val.不存在则返回默认值
   */
  public static boolean getBooleanRemoteConfVal(String name, boolean defaultVal) {
    String val = getRemoteConfVal(name);
    if (StringUtils.isNotBlank(val)) {
      return "true".equals(val);
    }
    return defaultVal;
  }
}
