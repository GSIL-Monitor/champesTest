package com.tencent.cham.taftemplate.jce.servant;

import com.qq.cloud.taf.common.annotation.*;
import com.qq.cloud.taf.common.support.Holder;

/**
 * Generated code, DO NOT modify it!
 * @author qq-central:maven-taf-plugin:1.0.0-SNAPSHOT:jce2java
 */
@JceService
public interface HelloWordPrx {

	public String sayHello();

	public String sayHello(@JceContext java.util.Map<String, String> ctx);

	public void async_sayHello(@JceCallback HelloWordPrxCallback callback);

	public void async_sayHello(@JceCallback HelloWordPrxCallback callback, @JceContext java.util.Map<String, String> ctx);
}
