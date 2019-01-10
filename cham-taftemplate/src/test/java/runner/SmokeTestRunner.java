package runner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.tencent.cham.taftemplate.unitcase.deploy.MockHelloWordServantImplTest;

import runner.category.Smoke;

/**
 * 冒烟测试用例入口. Created by liyayxli on 2017/4/6.
 */
@RunWith(Categories.class)
@Categories.IncludeCategory(Smoke.class)
@Suite.SuiteClasses({MockHelloWordServantImplTest.class})
public class SmokeTestRunner {}
