[![Build Status](https://travis-ci.org/anr-ru/base.R.svg?branch=master)](https://travis-ci.org/anr-ru/base.R)


## A part of [Base.Platform Project](https://github.com/anr-ru/base.platform.parent)

### Base.R

Services for linking Java and R applications.

To run tests please use: JUnit tests: RTest.java

You can check the results comparing to "pure" R invocation. Please run the following script in the /target/test-classes
directory:

```R
R CMD BATCH ./test-as-script.R out.txt
```

