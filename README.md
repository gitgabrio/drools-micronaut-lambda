# About

Example of a [Micronaut](https://micronaut.io/) Java lambda which uses
[Drools](https://www.drools.org/), and attempts to deploy this via GraalVM.

This currently does not work.

# Requirements

You need node 20 somehow. Probably best to install with your sytem
package manager, or use [nvm](https://github.com/nvm-sh/nvm).

Java was installed using sdkman:

```
sdk install java 21.0.2-graalce
```

# Install

Install packages:

```sh
npm install
```

Bootstrap your AWS account if you haven't done so (you will need to
have setup your access credentials):

```sh
npm run cdk bootstrap
```

# Build

Build lambda:

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
Invocation with requestId [5360ede4-0087-4f12-9724-1d9d4c9ea351] failed: java.lang.NoSuchFieldException: SUPPLIERjava.lang.RuntimeException: java.lang.NoSuchFieldException: SUPPLIER
at org.drools.compiler.kie.builder.impl.KieBuilderImpl.buildAll(KieBuilderImpl.java:207)
at org.drools.compiler.kie.builder.impl.KieBuilderImpl.getKieModule(KieBuilderImpl.java:515)
...
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
cdk init app --language=typescript
mn create-function-app example.micronaut.lambda \
                      --features=aws-lambda,graalvm \
                      --build=gradle \
                      --jdk=21
```

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
