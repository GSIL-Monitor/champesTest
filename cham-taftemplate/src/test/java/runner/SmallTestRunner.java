package runner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import runner.category.Small;

import com.tencent.cham.taftemplate.unitcase.deploy.MockHelloWordServantImplTest;
import com.tencent.cham.taftemplate.unitcase.util.ConfigUtilsTest;


/**
 * small级别的测试用例入口 Created by jamiemzhou on 2016/9/27.
 */
@RunWith(Categories.class)
@Categories.IncludeCategory(Small.class)
@Suite.SuiteClasses({MockHelloWordServantImplTest.class, ConfigUtilsTest.class})
public class SmallTestRunner {

}
