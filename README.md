# About

Example of a Micronaut Java lambda which uses Drools.

# Requirements

You need node 20 somehow. Probably best to install with your sytem
package manager, or use nvm.

Then you need aws cdk:

```
npm install -g aws-cdk
```

Bootstrap your AWS account if you haven't done so (you will need to
have setup your access credentials):

```
cdk bootstrap
```

Java was installed using sdkman:

```
sdk install java 21.0.2-graalce
sdk install micronaut
```

# Build

Build lambda:

```
npm run build-native-lambda
```

# Deploy

Deploy your CDK stack:

```
cdk deploy
```

This installs in us-east-1 by default.

# Test

```json
{
  "id": "color",
  "val": "red"
}
```

# How the project was created

```
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
