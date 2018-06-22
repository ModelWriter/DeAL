#! /usr/bin/awk -f

BEGIN {
  accumulator = 0.0;
  count = 0;
}

{
  data[count++] = $1;
  accumulator += $1;
}

END {
  mean = accumulator / count;

  variance_sum = 0.0;
  for (i = 0; i < count; i++) {
    delta = (data[i] - mean);
    variance_sum = delta*delta;
  }
  variance = variance_sum / count;

  printf "mean     \t%f\n", mean;
  printf "variance \t%f\n", variance;
  printf "standard-dev\t%f\n", sqrt(variance);
}

