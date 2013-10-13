package com.lpedrosa.test.utils;

import org.apache.commons.lang3.Validate;


public final class MethodParameterUtils {

	@SuppressWarnings("unchecked")
	public static final <T> T getParametersFor(Class<?> clazz, String methodName) {
		Validate.notEmpty(methodName);
		isValidMethodOf(clazz, methodName);
		String key = clazz.getName()+ "." + methodName;

		if(!ParametrizedTest.globalMethodParameters.containsKey(key)) {
			throw new IllegalArgumentException(methodName + " requires parameters");
		}

		return (T) ParametrizedTest.globalMethodParameters.get(key);
	}
	
	public static final void isValidMethodOf(Class<?> clazz, String methodName) {
		try {
			clazz.getDeclaredMethod(methodName);
		} catch(NoSuchMethodException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
}
