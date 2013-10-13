# Suite Test Arguments Example

## Introduction

This example tries to provide a solution for the issue one has when trying to pass arguments to specific test methods in a suite, when using JUnit as the testing framework.

Take the following example. You have a suite that tests several components (i.e. component A, B, C) in a webpage:

```java
@RunWith(Suite.class)
@Suite.SuiteClasses({
  TestComponentA.class,
  TestComponentB.class,
  TestComponentC.class
})
public class Suite {
}
```

Meanwhile, you decide to write another suite that targets another webpage. This new webpage has the same component (i.e. B) as the first webpage and you would like to reuse the same test class. You could simply do this:

```java
@RunWith(Suite.class)
@Suite.SuiteClasses({
  TestComponentB.class,
})
public class Suite2 {
}
```

However, lets assume the test class for component B depends on a value that changes on this new webpage:

```java
public void testIfClickingOnButtonShowsErrors() {
  
  String url = // this value depends on the page were this component resides
  open(url);
}
```

Since you have no way to instantiate test classes on suites without using the empty constructor, you'll have to find a way around this.

## Enter _@ClassRule_ and _ParametrizedTest_

After reading about different proposals for sharing fields between test classes in a suite, I came across the _@ClassRule_ annotation. When a _TestRule_ object (or method that returns it) is annotated with a _@ClassRule_, the TestRule's apply method will receive a statement that is composed by:

* Suite or Test class @BeforeClass method
* All the methods in a Test class or all the test classes in a Suite
* Suite or Test class @AfterClass method

For more information you can check [@ClassRule's javadoc](http://junit.org/javadoc/4.9/org/junit/ClassRule.html).

We could use this to our advantage. So I created an abstract class _ParemetrizedTest_ that your suite can extend. This class has a static field that can be used to access the parameters for each individual test method (i.e. it is a map \[methodName -> value \])

Now we just need and elegant way of adding parameters to the methods of test classes in the suite.

## MethodParameterAggregator

This class extends JUnit's [ExternalResouce](https://github.com/junit-team/junit/wiki/Rules#externalresource-rules) class. This class is used for rules that need to set up a resource before a test and clean it up afterwards.

That is exactly what _MethodParameterAggregator_ class does. It populates the static field in ParametrizedTests class before the suite runs, and cleans it up afterwards.

It provides an useful method _withParameter_ that takes the target test class, the method name and the parameter to share. Before it inserts the shared parameter to the map, it performs some sanity checks (i.e. checking if the target class actually has that method).

Returning to the example mentioned above, if you wanted to share the parameter to component B's test class, you could do the following:

```java
@RunWith(Suite.class)
@Suite.SuiteClasses({
  TestComponentB.class,
})
public class Suite2 {
  @ClassRule
  public static final MethodParameterAggregator testParameters = 
    new MethodParameterAggregator()
    .withParameter(TestComponentB.class, "testThatNeedsAnExternalParam", "theValue");
}
```


## Using the shared paramters

When writing your reusable test classes (and methods) you can now pull the methods shared by a _ParametrizedTest_ class. Just use _MethodParameterUtils_ to obtain those parameters.
Continuing with the example mentioned above, your test method that depends on an external value could be written as follows:

```java
public void testIfClickingOnButtonShowsErrors() {
  
  String url = MethodParameterUtils.getParametersFor(this.getClass(), "testIfClickingOnButtonShowsErrors"); // this value now depends on what you've injected on the parent test/suite
  open(url);
}
```

## Conclusion

I do understand it is not the most elegant solution (it uses a static field to inject values in the suite's classes!), but I do believe it has suitable sanity checks to prevent misuse.

Ideally the map in the _ParametrizedTest_ class would be immutable, to prevent different test classes in a suite from tempering with it (e.g. changing parameters of methods in another test).

I do hope it still fits your use case and reduces the amount of tests you have to write (being lazy but efficient is always nice!).
