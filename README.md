[![Build Status](https://app.travis-ci.com/anr-ru/base.R.svg?branch=master)](https://travis-ci.com/anr-ru/base.R)


## A part of [Base.Platform Project](https://github.com/anr-ru/base.platform.parent)

### Base.R

Services for linking Java and R applications.

```bash
# 1. R_HOME and LD_LIBRARY_PATH must be properly set:

R_HOME=/usr/lib/R
LD_LIBRARY_PATH="$LD_LIBRARY_PATH:$R_HOME/lib:$R_HOME/bin"


# 2. When run tests, set the 'java.library.path' properly
mvn ...  -Djava.library.path=${env.R_HOME}/library/rJava/jri
```

To run tests please use: JUnit tests: RTest.java

You can check the results comparing to "pure" R invocation. Please run the following script in the /target/test-classes
directory:

```R
R CMD BATCH ./test-as-script.R out.txt
```

