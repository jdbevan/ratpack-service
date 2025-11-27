This repo contains some code implemented using Ratpack's Promise implementation, there are some bugs for you to fix.

Tests will fail on first run and the code needs fixing to make them pass.

You will need Java 17 SDK installed and JAVA_HOME set on your PATH environment variable.

You should be able to run `./gradlew test` in order to run the tests in the repo. If you are running on a Windows machine you can run `./gradlew.bat test` instead.

This is the expected output for test failure:

```
$ ./gradlew test

> Task :lib:test FAILED
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by org.codehaus.groovy.reflection.CachedClass (file:/Users/candidate/.gradle/caches/modules-2/files-2.1/org.codehaus.groovy/groovy/2.5.13/ac054525fdc81cbd0bc2552b57052ebb1a93cd40/groovy-2.5.13.jar) to method java.lang.Object.finalize()
WARNING: Please consider reporting this to the maintainers of org.codehaus.groovy.reflection.CachedClass
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release

com.adaptavist.cloud.interviews.DynamicModulesRegistrarSpec > should return a promise of true if a dynamic module is registered successfully FAILED
    org.spockframework.runtime.ConditionNotSatisfiedError at DynamicModulesRegistrarSpec.groovy:69

com.adaptavist.cloud.interviews.DynamicModulesRegistrarSpec > should return a promise of false if dynamic module registration request experiences a socket timeout FAILED
    org.spockframework.runtime.ConditionNotSatisfiedError at DynamicModulesRegistrarSpec.groovy:105

8 tests completed, 2 failed

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':lib:test'.
> There were failing tests. See the report at: file:///Users/candidate/interviews/ratpack-service/lib/build/reports/tests/test/index.html

* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output. Run with --scan to get full insights.

* Get more help at https://help.gradle.org

BUILD FAILED in 2s
3 actionable tasks: 1 executed, 2 up-to-date

```
