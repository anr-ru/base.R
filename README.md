## [Base.Platform Project](https://github.com/anr-ru/base.platform.parent)

### Base.Platform.R

Base Services for R language integrations.

To run tests please use: JUnit tests: RTest.java

You can check the results comparing to "pure" R invocation. Please run the following script in the /target/test-classes
directory:

```R
R CMD BATCH ./test-as-script.R out.txt
```

