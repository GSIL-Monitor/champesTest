package runner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import runner.category.Big;


/**
 * Big策略级别的测试用例执行入口
 * Created by jamiemzhou on 2016/9/27.
 */

@RunWith(Categories.class)
@Categories.IncludeCategory(Big.class)
@Suite.SuiteClasses({})
public class BigTestRunner {

}
