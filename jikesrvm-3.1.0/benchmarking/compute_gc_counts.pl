#!/usr/bin/perl -w
use strict;

if (@ARGV != 1) {
  print "Usage: script.pl input_file\n";
  exit;
}

open(INFILE, "<", $ARGV[0]) or die "Cannot open file: $!";
my $curr_config;
my $curr_benchmark;
my %results;
while (<INFILE>) {
  if (/^(Base|NoAssertions)\.(.+)$/) {
    $curr_config = $1;
    $curr_benchmark = $2;
    if ($curr_benchmark =~ /_[\d]{3}_(\w+)/) {
      $curr_benchmark = $1;
    }
  } elsif (/^gc count -- avg ([\d\.]+), stddev ([\d\.]+)$/) {
    if ($curr_config eq "Base") {
      $results{$curr_benchmark}[0] = $1;
    } elsif ($curr_config eq "NoAssertions") {
      $results{$curr_benchmark}[1] = $1;
    } else {
      print "Error processing line $_: no current config found\n";
    }
  } 
}
close INFILE;

foreach my $benchmark (sort keys %results) {
  my $percent_increase = 100 * ($results{$benchmark}[1] - $results{$benchmark}[0]) / $results{$benchmark}[0];
  print "$benchmark $results{$benchmark}[0] $results{$benchmark}[1] $percent_increase\n";
}

