package com.lpedrosa.test.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class ParametrizedSuite extends Suite {
	
	private final Map<String, Object> suiteMethodParameters;
	
	public ParametrizedSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
		super(klass, builder);
		// TODO learn how to change the runner used by the children
		this.suiteMethodParameters = getParametersMap(klass);
    }
	
	private static Map<String,Object> getParametersMap(Class<?> klass) throws InitializationError {
		for(Field field : klass.getFields()) {
			if(field.isAnnotationPresent(Parameters.class)) {
				try {
					MethodParameterAggregator paramAggregator = (MethodParameterAggregator) field.get(null);
					return paramAggregator.getParams();
				} catch (NullPointerException e) {
					throw new InitializationError(String.format("class '%s' @Parameters field must be static", klass.getName()));
				} catch (IllegalArgumentException e) {
					throw new InitializationError(String.format("class '%s' @Parameters field must be static", klass.getName()));
				} catch (IllegalAccessException e) {
					throw new InitializationError(String.format("class '%s' @Parameters field must be public", klass.getName()));
				} catch (ClassCastException e) {
					// TODO not sure if should keep this (might be simple map)
					throw new InitializationError(String.format("class '%s' @Parameters field must be of type MethodParameterAggregator", klass.getName()));
				}
			}
		}
		throw new InitializationError(String.format("class '%s' must have a Parameters annotation", klass.getName()));
	}
	
    @Override
    protected void runChild(Runner runner, final RunNotifier notifier) {
        runner.run(notifier);
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @Inherited
    public @interface Parameters {
    }

}
