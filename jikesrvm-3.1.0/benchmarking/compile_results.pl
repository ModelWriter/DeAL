#!/usr/bin/perl -w
use strict;

use Stats;
use Cwd;

# This script processes the benchmark output files from run_dacapo.pl 
# and produces a summary of the results.  

my $confidence_value = 90;
my @benchmarks = ("dacapo2006", "dacapo2009", "jvm98", "pjbb2000");
my $iterations = 4;

my %gc_counts;
my %times_total;
my %times_mutator;
my %times_gc;
my %perfs_mutator;
my %perfs_gc;
my %times_refType;
my %times_scan;
my %times_finalize;
my %times_prepare;
my %times_precopy;
my %times_stacks;
my %times_root;
my %times_forward;
my %times_release;
my %times_init;
my %times_finish;

if (@ARGV == 1) {
  chdir $ARGV[0] or die "Cannot chdir to $ARGV[0]: $!";
}

# process all benchmark files in directory
foreach (<*>) {
  if (/^(\w*)\.(\w*)\.(\w*)\.(\d*)$/) {
    my $config = $1;
    my $bmgroup = $2;
    my $benchmark = $3;
    my $run_num = $4;
    my $name = "$config.$bmgroup.$benchmark";

    my $result_ok;    # this is set when we're sure the benchmark succeeded
    my $mmtk_stats_ok;
    my $result_count = 0;  # count of number of successful iterations

    # Success is defined as follows:
    # For Dacapo, we see the string "DaCapo $benchmark PASSED"
    # For SPECjvm98 and pseudoJBB2000, we see the string "$benchmark Finished"
    # $iterations times
    open(INFILE, "<", $_) or die "Cannot open file: $!";
    while (<INFILE>) {
      if (/DaCapo $benchmark PASSED/) {
        $result_ok = 1;
      } elsif (/$benchmark Finished/) {
        $result_count++;
      } elsif (/^=* MMTk Statistics Totals =*$/) {
        <INFILE>;  # throw away header line
        my $line = <INFILE>;
#        print $line;
        my @mmtk_stats = split /\s+/, $line;
        push @{ $gc_counts{"$name"} }, shift(@mmtk_stats);
        push @{ $times_mutator{"$name"} }, shift(@mmtk_stats);
        push @{ $times_gc{"$name"} }, shift(@mmtk_stats);
        push @{ $perfs_mutator{"$name"} }, shift(@mmtk_stats);
        push @{ $perfs_gc{"$name"} }, shift(@mmtk_stats);
        push @{ $times_refType{"$name"} }, shift(@mmtk_stats);
        push @{ $times_scan{"$name"} }, shift(@mmtk_stats);
        push @{ $times_finalize{"$name"} }, shift(@mmtk_stats);
        push @{ $times_prepare{"$name"} }, shift(@mmtk_stats);
        push @{ $times_precopy{"$name"} }, shift(@mmtk_stats);
        push @{ $times_stacks{"$name"} }, shift(@mmtk_stats);
        push @{ $times_root{"$name"} }, shift(@mmtk_stats);
        push @{ $times_forward{"$name"} }, shift(@mmtk_stats);
        push @{ $times_release{"$name"} }, shift(@mmtk_stats);
        push @{ $times_init{"$name"} }, shift(@mmtk_stats);
        push @{ $times_finish{"$name"} }, shift(@mmtk_stats);

        # get total time
        $line = <INFILE>;
        if ($line =~ /^Total time: ([\d\.]+) ms$/) {
          push @{ $times_total{"$name"} }, $1;
          $mmtk_stats_ok = 1;
        }
      }
    }
    close INFILE;

    # For SPECjvm98 and pseudojbb, we need to make sure all $iterations 
    # runs succeeded
    if ((($bmgroup eq "jvm98") || ($bmgroup eq "pjbb2000")) && ($result_count = $iterations)) {
      $result_ok = 1;
    }

    if (!$result_ok || !$mmtk_stats_ok) {
      print "WARNING: $config.$bmgroup.$benchmark.$run_num failed\n";
    }
  }
}


foreach (sort keys %times_total) {
  print "$_\n";
  print "gc count -- avg " . &average(@{$gc_counts{$_}}) . 
    ", stddev " . &stddev(@{$gc_counts{$_}}) . 
    ", confidence " . &confidence($confidence_value, @{$gc_counts{$_}}) . "\n";
  print "total time -- avg " . &average(@{$times_total{$_}}) . 
    ", stddev " . &stddev(@{$times_total{$_}}) . 
    ", confidence " . &confidence($confidence_value, @{$times_total{$_}}) . "\n";
  print "mutator time -- avg " . &average(@{$times_mutator{$_}}) . 
    ", stddev " . &stddev(@{$times_mutator{$_}}) . 
    ", confidence " . &confidence($confidence_value, @{$times_mutator{$_}}) . "\n";
  print "gc time -- avg " . &average(@{$times_gc{$_}}) . 
    ", stddev " . &stddev(@{$times_gc{$_}}) . 
    ", confidence " . &confidence($confidence_value, @{$times_gc{$_}}) . "\n";
  print "mutator perfctr -- avg " . &average(@{$perfs_mutator{$_}}) . 
    ", stddev " . &stddev(@{$perfs_mutator{$_}}) .
    ", confidence " . &confidence($confidence_value, @{$times_mutator{$_}}) . "\n";
  print "gc perfctr -- avg " . &average(@{$perfs_gc{$_}}) . 
    ", stddev " . &stddev(@{$perfs_gc{$_}}) . 
    ", confidence " . &confidence($confidence_value, @{$perfs_gc{$_}}) . "\n";
  print "refType -- avg " . &average(@{$times_refType{$_}}) . 
    ", stddev " . &stddev(@{$times_refType{$_}}) . 
    ", confidence " . &confidence($confidence_value, @{$times_refType{$_}}) . "\n";
  print "scan -- avg " . &average(@{$times_scan{$_}}) . 
    ", stddev " . &stddev(@{$times_scan{$_}}) .
    ", confidence " . &confidence($confidence_value, @{$times_scan{$_}}) . "\n";
  print "finalize -- avg " . &average(@{$times_finalize{$_}}) . 
    ", stddev " . &stddev(@{$times_finalize{$_}}) . 
    ", confidence " . &confidence($confidence_value, @{$times_finalize{$_}}) . "\n";
  print "prepare -- avg " . &average(@{$times_prepare{$_}}) . 
    ", stddev " . &stddev(@{$times_prepare{$_}}) .
    ", confidence " . &confidence($confidence_value, @{$times_prepare{$_}}) . "\n";
  print "precopy -- avg " . &average(@{$times_precopy{$_}}) . 
    ", stddev " . &stddev(@{$times_precopy{$_}}) .
    ", confidence " . &confidence($confidence_value, @{$times_precopy{$_}}) . "\n";
  print "stacks -- avg " . &average(@{$times_stacks{$_}}) . 
    ", stddev " . &stddev(@{$times_stacks{$_}}) . 
    ", confidence " . &confidence($confidence_value, @{$times_stacks{$_}}) . "\n";
  print "root -- avg " . &average(@{$times_root{$_}}) . 
    ", stddev " . &stddev(@{$times_root{$_}}) . 
    ", confidence " . &confidence($confidence_value, @{$times_root{$_}}) . "\n";
  print "forward -- avg " . &average(@{$times_forward{$_}}) . 
    ", stddev " . &stddev(@{$times_forward{$_}}) . 
    ", confidence " . &confidence($confidence_value, @{$times_forward{$_}}) . "\n";
  print "release -- avg " . &average(@{$times_release{$_}}) . 
    ", stddev " . &stddev(@{$times_release{$_}}) . 
    ", confidence " . &confidence($confidence_value, @{$times_release{$_}}) . "\n";
  print "init -- avg " . &average(@{$times_init{$_}}) . 
    ", stddev " . &stddev(@{$times_init{$_}}) . 
    ", confidence " . &confidence($confidence_value, @{$times_init{$_}}) . "\n";
  print "finish -- avg " . &average(@{$times_finish{$_}}) . 
    ", stddev " . &stddev(@{$times_finish{$_}}) . 
    ", confidence " . &confidence($confidence_value, @{$times_finish{$_}}) . "\n";
  print "\n";
}

