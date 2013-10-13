package com.lpedrosa.test.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.junit.rules.ExternalResource;


public final class MethodParameterAggregator extends ExternalResource {
	
	private final Map<String,Object> params;
	
	public MethodParameterAggregator() {
		params = new HashMap<String, Object>();
	}
	
	public final <T> MethodParameterAggregator withParameter(Class<?> clazz, String methodName, T param) {
		Validate.notEmpty(methodName);
		Validate.notNull(param);
		
		SuiteParameterUtils.isValidMethodOf(clazz, methodName);
		
		params.put(clazz.getName() + "." + methodName, param);
		return this;
	}

	@Override
	protected final void before() throws Throwable {
		ParametrizedTest.globalMethodParameters.putAll(params);
	}
	
	@Override
	protected final void after() {
		ParametrizedTest.globalMethodParameters.clear();
	}
}
