package com.lpedrosa.test.generic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.lpedrosa.test.utils.SuiteParameterUtils;

public class TestsWithArguments {
	
	@Test
	public void aTestWithInjectedArguments() {
		String injectedValue = SuiteParameterUtils.getParametersFor(this.getClass(), "aTestWithInjectedArguments");
		assertEquals("injectValue1", injectedValue);
	}
	
	@Test
	public void anotherTestWithInjectedArguments() {
		String injectedValue = SuiteParameterUtils.getParametersFor(this.getClass(), "anotherTestWithInjectedArguments");
		assertEquals("injectValue2", injectedValue);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void aNormalTest() {
		SuiteParameterUtils.getParametersFor(this.getClass(), "aNormalTest");
	}
}
