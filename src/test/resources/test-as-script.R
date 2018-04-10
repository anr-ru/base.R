
# Input variables
x = 12.45;
arr = c(3.14, 2.72);

source("test-definitions.R");
source("test-functions.R");
source("test-main.R");

# Check and compare results
stopifnot(rs == 186.75, out == 5.86, sx == c('1','2','3'));