package runner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import runner.category.Middle;

import com.tencent.cham.taftemplate.funccase.HelloWordServantImplTest;


/**
 * Created by jamiemzhou on 2016/10/9.
 */
@RunWith(Categories.class)
@Categories.IncludeCategory(Middle.class)
@Suite.SuiteClasses({HelloWordServantImplTest.class})
public class MiddleTestRunner {}
