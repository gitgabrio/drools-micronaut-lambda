# About

Example of a [Micronaut](https://micronaut.io/) Java lambda which uses
[Drools](https://www.drools.org/), and attempts to deploy this via GraalVM.

This currently does not work.

# Requirements

You need node 20 somehow. Probably best to install with your sytem
package manager, or use [nvm](https://github.com/nvm-sh/nvm).

Java was installed using [sdkman](https://sdkman.io/):

```
sdk install java 21.0.2-graalce
```

# Repeat the problem without deploying the stack

You can repeat the problem easily by running the nativeTest task:

```
cd java
./gradlew nativeTest
```

Produces a very similar output as running the actual lambda:

```
Micronaut version: 4.6.5
input: {"id":"color","val":"red"}
example.micronaut.FunctionRequestHandlerTest > testHandler() FAILED


Failures (1):
  JUnit Jupiter:FunctionRequestHandlerTest:testHandler()
    MethodSource [className = 'example.micronaut.FunctionRequestHandlerTest', methodName = 'testHandler', methodParameterTypes = '']
    => java.lang.RuntimeException: java.lang.NoSuchFieldException: SUPPLIER
       org.drools.compiler.kie.builder.impl.KieBuilderImpl.buildAll(KieBuilderImpl.java:207)
       org.drools.compiler.kie.builder.impl.KieBuilderImpl.getKieModule(KieBuilderImpl.java:515)
       org.drools.compiler.kie.builder.impl.KieBuilderImpl.getKieModule(KieBuilderImpl.java:505)
       org.drools.ruleunits.impl.RuleUnitProviderImpl.createRuleUnitKieModule(RuleUnitProviderImpl.java:115)
       org.drools.ruleunits.impl.RuleUnitProviderImpl.generateRuleUnit(RuleUnitProviderImpl.java:83)
       [...]
     Caused by: java.lang.NoSuchFieldException: SUPPLIER
       java.base@21.0.2/java.lang.Class.checkField(DynamicHub.java:1041)
       java.base@21.0.2/java.lang.Class.getField(DynamicHub.java:1026)
       org.drools.compiler.kie.builder.impl.KieBuilderImpl.getSupplier(KieBuilderImpl.java:212)
       org.drools.compiler.kie.builder.impl.KieBuilderImpl.buildAll(KieBuilderImpl.java:204)
       [...]
```

# Install

To actully deploy the lambda, first install the node packages:

```sh
npm install
```

Bootstrap your AWS account if you haven't done so (you will need to
have setup your access credentials):

```sh
npm run cdk bootstrap
```

# Build

Build the Java native lambda:

```sh
npm run build-native-lambda
```

# Deploy

Deploy your CDK stack:

```sh
npm run cdk deploy
```

This installs in us-east-1 by default.

# Test

From the command-line, if you have the aws cli installed:

```sh
aws lambda invoke --function-name micronaut-drools /dev/stdout
```

You will see this:

```JSON
{"errorMessage":"java.lang.NoSuchFieldException: SUPPLIER"}
    "StatusCode": 200,
    "FunctionError": "Unhandled",
    "ExecutedVersion": "$LATEST"
}
```

If you want the stack trace:

```sh
aws lambda invoke --function-name micronaut-drools response.json --log-type Tail --query LogResult --output text | base64 -d
```

Output:

```
TART RequestId: 5360ede4-0087-4f12-9724-1d9d4c9ea351 Version: $LATEST
Micronaut version: 4.6.5
input: {}
Invocation with requestId [5360ede4-0087-4f12-9724-1d9d4c9ea351] failed: java.lang.NoSuchFieldException: SUPPLIER
java.lang.RuntimeException: java.lang.NoSuchFieldException: SUPPLIER
at org.drools.compiler.kie.builder.impl.KieBuilderImpl.buildAll(KieBuilderImpl.java:207)
at org.drools.compiler.kie.builder.impl.KieBuilderImpl.getKieModule(KieBuilderImpl.java:515)
at org.drools.compiler.kie.builder.impl.KieBuilderImpl.getKieModule(KieBuilderImpl.java:505)
at org.drools.ruleunits.impl.RuleUnitProviderImpl.createRuleUnitKieModule(RuleUnitProviderImpl.java:115)
at org.drools.ruleunits.impl.RuleUnitProviderImpl.generateRuleUnit(RuleUnitProviderImpl.java:83)
at org.drools.ruleunits.dsl.RuleUnitProviderForDSL.generateRuleUnit(RuleUnitProviderForDSL.java:55)
at org.drools.ruleunits.impl.RuleUnitProviderImpl.getRuleUnit(RuleUnitProviderImpl.java:78)
at org.drools.ruleunits.api.RuleUnitProvider.createRuleUnitInstance(RuleUnitProvider.java:41)
at example.micronaut.FunctionRequestHandler.execute(FunctionRequestHandler.java:31)
at example.micronaut.FunctionRequestHandler.execute(FunctionRequestHandler.java:16)
at io.micronaut.function.aws.MicronautRequestHandler.handleRequest(MicronautRequestHandler.java:110)
at io.micronaut.function.aws.runtime.AbstractMicronautLambdaRuntime.handleInvocationForRequestHandler(AbstractMicronautLambdaRuntime.java:445)
at io.micronaut.function.aws.runtime.AbstractMicronautLambdaRuntime.startRuntimeApiEventLoop(AbstractMicronautLambdaRuntime.java:407)
at io.micronaut.function.aws.runtime.AbstractMicronautLambdaRuntime.run(AbstractMicronautLambdaRuntime.java:167)
at example.micronaut.FunctionLambdaRuntime.main(FunctionLambdaRuntime.java:12)
at java.base@21.0.4/java.lang.invoke.LambdaForm$DMH/sa346b79c.invokeStaticInit(LambdaForm$DMH)
Caused by: java.lang.NoSuchFieldException: SUPPLIER
at java.base@21.0.4/java.lang.Class.checkField(DynamicHub.java:1044)
at java.base@21.0.4/java.lang.Class.getField(DynamicHub.java:1029)
at org.drools.compiler.kie.builder.impl.KieBuilderImpl.getSupplier(KieBuilderImpl.java:212)
at org.drools.compiler.kie.builder.impl.KieBuilderImpl.buildAll(KieBuilderImpl.java:204)
... 15 more
END RequestId: 5360ede4-0087-4f12-9724-1d9d4c9ea351
REPORT RequestId: 5360ede4-0087-4f12-9724-1d9d4c9ea351  Duration: 2.62 ms       Billed Duration: 3 ms     Memory Size: 1024 MB    Max Memory Used: 74 MB
```

Or via the AWS console, find the "micronaut-drools" lambda, and on the
test tab supply this JSON:

```json
{
  "id": "color",
  "val": "red"
}
```

You can the same error message, but also the stack trace.

# How the project was created

Install the micronaut cli with sdkman:

```sh
sdk install micronaut
```

Then create the initial Java code:

```sh
mkdir drools-example
cd drools-example
cdk init app --language=typescript
mn create-function-app example.micronaut.lambda \
  --features=aws-lambda,graalvm \
  --build=gradle \
  --jdk=21
mv lambda java
```

Then copying in the [getting started Drools example](https://docs.drools.org/8.39.0.Final/drools-docs/docs-website/drools/getting-started/index.html).

## Useful commands

- `npm run build` compile typescript to js
- `npm run watch` watch for changes and compile
- `npm run test` perform the jest unit tests
- `npx cdk deploy` deploy this stack to your default AWS account/region
- `npx cdk diff` compare deployed stack with current state
- `npx cdk synth` emits the synthesized CloudFormation template

# Notes

If Java isn't compiled statically you get:

```
./func: /lib64/libc.so.6: version `GLIBC_2.32' not found (required by ./func)
./func: /lib64/libc.so.6: version `GLIBC_2.34' not found (required by ./func)
INIT_REPORT Init Duration: 10.17 ms Phase: init Status: error   Error Type: Runtime.ExitError
./func: /lib64/libc.so.6: version `GLIBC_2.32' not found (required by ./func)
./func: /lib64/libc.so.6: version `GLIBC_2.34' not found (required by ./func)
INIT_REPORT Init Duration: 4.87 ms  Phase: invoke   Status: error   Error Type: Runtime.ExitError
START RequestId: 00c4bc44-e48e-4e7d-ab38-a47800cd734b Version: $LATEST
RequestId: 00c4bc44-e48e-4e7d-ab38-a47800cd734b Error: Runtime exited with error: exit status 1
Runtime.ExitError
END RequestId: 00c4bc44-e48e-4e7d-ab38-a47800cd734b
REPORT RequestId: 00c4bc44-e48e-4e7d-ab38-a47800cd734b  Duration: 6.38 ms   Billed Duration: 7 ms   Memory Size: 1024 MB    Max Memory Used: 3 MB
```

# References

Getting started guides:

1. [Micronaut](https://guides.micronaut.io/latest/mn-application-aws-lambda-graalvm-gradle-java.html).
2. [Drools](https://docs.drools.org/8.39.0.Final/drools-docs/docs-website/drools/getting-started/index.html).
