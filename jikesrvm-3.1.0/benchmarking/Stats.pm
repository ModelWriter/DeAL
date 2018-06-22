package Stats;
use strict;

require Exporter;
our @ISA = qw{Exporter};
our @EXPORT = qw{average
       stddev
       confidence
       };

sub average {
  my $sum;
  my $count;
  foreach (@_) {
    $sum += $_;
  }
  return $sum/@_;
}

# For standard deviation, we use "sample standard deviation" since we do not
# have the whole population.  The formula is:
# sqrt( sum ((x - m)^2) / (n - 1))
# where x is each sample, m is the mean, and n is the number of samples
sub stddev {
  my $avg = &average(@_);
  my $temp;
  foreach (@_) {
    $temp += ($_ - $avg)**2;  
  }
  $temp /= (@_ - 1);
  return sqrt $temp;
}

# We compute confidence intervals here using the formulas in Georges et al., 
# "Statistically Rigorous Java Performance Evaluation."
# For n <= 30, we base this on a Gaussian distribution; for n < 30, we use a 
# Student's t-distribution.  We take critical values for the Student's 
# t-distribution from the Wikipedia page: 
# http://en.wikipedia.org/wiki/Student%27s_t-distribution
#
# Note that the Wikipedia page gives *one-sided* confidence intervals, so we
# have to halve them to get our confidence intervals.  For example, our 90%
# confidence interval corresponds to Wikipedia's 95%.
#
# Formula for this is as follows:
# m +- a * s / sqrt(n)
# where m is the mean, a is the critical number, s is the standard deviation,
# and n is the number of samples
# 
sub confidence {
  my ($confidence_value, @values) = @_;
  my $stddev = &stddev(@values);
  my $n = scalar(@values);
  my $a = &get_critical_value($n, $confidence_value);

  return $a * $stddev / sqrt($n);
}

# critical values for Student's t-distribution. for use when n < 30.
# can fill in more of these as needed.
my %students_critical_values = (
  19 => {90 => 1.729, 95 => 2.093, 99 => 2.861},
);

# critical values for Gaussian distribution. for use when n >= 30
my %gaussian_critical_values = (90 => 1.645, 95 => 1.960, 99 => 2.576);


sub get_critical_value {
  my ($n, $confidence_value) = @_;

  if ($n >= 30) {
    return $gaussian_critical_values{$confidence_value};
  } else {
    return $students_critical_values{$n-1}->{$confidence_value};
  }
}
