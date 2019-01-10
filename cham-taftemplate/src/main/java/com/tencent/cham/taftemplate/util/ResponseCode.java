package com.tencent.cham.taftemplate.util;

/**
 * 请求响应码定义.参考http://git.code.oa.com/WSRD-Tech-Center-Docs/ResponseDesign.
 * Created by liyayxli on 2017/10/23.
 */
public class ResponseCode {
  /**
   * 创建及查询类返回 int 数字类的方法出错, 后台逻辑使用，不会返回给前端.
   */
  public static final int ERR_RETURN_INT_NUM = -1;

  // 通用状态码.
  /**
   * 成功.
   */
  public static final int SUCCESS = 0;

  /**
   * 未知错误.
   */
  public static final int SERVER_ERR_UNKNOWN = 100001;

  /**
   * 服务暂时不可用.
   */
  public static final int SERVER_TEMP_UNAVAILABLE = 100002;

  /**
   * 未授权.
   */
  public static final int REQUEST_UNAUTHORIZED = 100003;

  /**
   * 参数不合法.
   */
  public static final int REQUEST_PARAM_INVALID = 100004;

  /**
   * 无权限、鉴权失败.
   */
  public static final int REQUEST_PERMISSION_DENIED = 100007;

  /**
   * 无匹配信息.
   */
  public static final int REQUEST_NO_MATCH_DATA = 100009;

  /**
   * 请求频率达到上限.
   */
  public static final int REQUEST_TOO_FREQUENT = 100011;
}
