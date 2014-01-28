package com.lpedrosa.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.lpedrosa.test.generic.TestsWithArguments;
import com.lpedrosa.test.utils.MethodParameterAggregator;
import com.lpedrosa.test.utils.ParametrizedSuite;

@RunWith(ParametrizedSuite.class)
@Suite.SuiteClasses({
	TestsWithArguments.class
})
public class ExampleSuite {
	
	@ParametrizedSuite.Parameters
	public static final MethodParameterAggregator testParameters = new MethodParameterAggregator()
		.withParameter(TestsWithArguments.class, "aTestWithInjectedArguments", "injectValue1")
		.withParameter(TestsWithArguments.class, "anotherTestWithInjectedArguments", "injectValue2");

}
