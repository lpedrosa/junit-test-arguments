package com.lpedrosa.test.generic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.lpedrosa.test.utils.MethodParameterUtils;

public class TestsWithArguments {
	
	@Test
	public void aTestWithInjectedArguments() {
		String injectedValue = MethodParameterUtils.getParametersFor(this.getClass(), "aTestWithInjectedArguments");
		assertEquals("injectValue1", injectedValue);
	}
	
	@Test
	public void anotherTestWithInjectedArguments() {
		String injectedValue = MethodParameterUtils.getParametersFor(this.getClass(), "anotherTestWithInjectedArguments");
		assertEquals("injectValue2", injectedValue);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void aNormalTest() {
		MethodParameterUtils.getParametersFor(this.getClass(), "aNormalTest");
	}
}
