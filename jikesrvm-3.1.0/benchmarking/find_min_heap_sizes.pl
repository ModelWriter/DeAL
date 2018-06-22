#!/usr/bin/perl -w
use strict;
use BmConfig;
use Cwd;
use TimedSystem;

# This script determines the minimum heap size as a multiple of 4 MB for 
# each benchmark in the SPEC JVM 98, Dacapo 2006, Dacapo 9.12 (2009 release),
# and Pseudo JBB 2000 suites, for a given set of configurations of Jikes 
# RVM.
#
# We run each benchmark for $iterations iterations because repeating a 
# benchmark multiple times inside the same JVM instance can increase 
# memory requirements. 
#
# We repeat each benchmark $invocations times because the results were 
# unstable with only 1 iteration (even with 5 iterations).  We were seeing a 
# lot of situations where a benchmark would run one time with a certain heap 
# size but wouldn't run the next.
#
# We selected $iterations = 4 and $invocations = 10 to mimic our benchmarking
# methodology.
#
# Usage: ./find_min_heap_sizes.pl [list of benchmarks]
# Valid benchmark names are jvm98, dacapo2006, dacapo2009, and pjbb2000.
# If no benchmarks are provided, runs all of them (SPECjvm98, pseudoJBB2000, 
# Dacapo 2006, and Dacapo 2009).  Otherwise it runs only the ones in the 
# list.
#
# Note that chart from dacapo2006 is a special case in two ways:
# 1) It requires an X Server, so we start it with xvfb-run.
# 2) If you run two invocations too close to one another it fails (probably
#    because of xvfb).  So we sleep for 1 second between invocations.

# benchmark run parameters
my $iterations = 4;       # number of iterations per benchmark run
my $invocations = 10;     # number of times to repeat each benchmark

my $min_heap_size = 12;   # Jikes RVM will not boot with an 8 MB heap
my $timeout = 20 * 60;    # benchmark timeout in seconds

# success strings for each benchmark
my $jvm98_success = "_998_end Finished";
my $dacapo2006_success = "PASSED";
my $dacapo2009_success = "PASSED";
my $pjbb2000_success = "End MMTk Statistics";

# failure string
# We record a failure only if it is an OutOfMemoryError.  This is to deal
# with instability of Jikes RVM itself.
my $failure_string = "OutOfMemoryError";

# Initial heap size for each benchmark -- we start our search from this
# heap size.  Initialize this hash with values that should be close to the
# actual minimum heap size for each benchmark.  This shouldn't affect
# correctness but will make the search faster.
my %benchmarks = (
    jvm98 => {
      _201_compress => 24,
      _202_jess => 28,
      _205_raytrace => 16,
      _209_db => 24,
      _213_javac => 36,
      _222_mpegaudio => 16,
      _227_mtrt => 16,
      _228_jack => 32,
    },
    dacapo2006 => {
      antlr => 32,
      bloat => 68,
      chart => 52,
      eclipse => 116,
      fop => 44,
      hsqldb => 128,
      jython => 48,
      luindex => 56,
      lusearch => 56,
      pmd => 56,
      xalan => 56,
    },
    dacapo2009 => {
      avrora => 100,
      batik => 100,
      eclipse => 100,
      fop => 100,
      h2 => 100,
      jython => 100,
      luindex => 100,
      lusearch => 100,
      pmd => 100,
      sunflow => 100,
      tomcat => 100,
      tradebeans => 100,
      tradesoap => 100,
      xalan => 100,
    },
    pjbb2000 => {
      pjbb2000 => 64,
    },
);

# benchmark setup
my @bmgroups;
if (@ARGV > 0) {
  # check to make sure arguments are all legitimate benchmarks
  foreach my $bmgroup (@ARGV) {
    if (not exists $benchmarks{$bmgroup}) {
      die "Unsupported benchmark: $bmgroup";
    }
  }
  @bmgroups = @ARGV;
} else {
  @bmgroups = ("jvm98", "dacapo2006", "dacapo2009", "pjbb2000");
}

# Provide configurations to test here.  The first string in each pair is the
# Jikes RVM directory, the second is the configuration to use, and the third
# is the command-line options to pass to Jikes RVM
my %configurations = (
  Base => [ "jikesrvm-3.1.0-unmodified", "FastAdaptiveMarkSweep", "" ],
);

# set up scratch directory
my $scratch_dir = getcwd . "/scratch";
mkdir($scratch_dir);

foreach my $bmgroup (@bmgroups) {
  my $heap_sizes = $benchmarks{$bmgroup};

  my $success_string = "";

  # chdir into benchmark directory
  if ($bmgroup eq "jvm98") {
    chdir($jvm98_dir) or die "Cannot chdir to $jvm98_dir: $!";
    $success_string = $jvm98_success;
  } elsif ($bmgroup eq "dacapo2006") {
    chdir($dacapo2006_dir) or die "Cannot chdir to $dacapo2006_dir: $!";
    $success_string = $dacapo2006_success;
  } elsif ($bmgroup eq "dacapo2009") {
    chdir($dacapo2009_dir) or die "Cannot chdir to $dacapo2009_dir: $!";
    $success_string = $dacapo2009_success;
  } elsif ($bmgroup eq "pjbb2000") {
    chdir($pjbb2000_dir) or die "Cannot chdir to $pjbb2000_dir: $!";
    $success_string = $pjbb2000_success;
  } else {
    die "Benchmark group $bmgroup not supported";
  }

  foreach my $config (sort keys %configurations) {
    foreach my $benchmark (sort keys %$heap_sizes) {

      # set heap size
      my $heap_size = $heap_sizes->{$benchmark};
      my $max_failed_heap_size = $min_heap_size - 4;
      my $min_succeeded_heap_size = 1000;   # assume all programs work in 1 GB heap

      while ($min_succeeded_heap_size - $max_failed_heap_size > 4) {
        print "Running $benchmark with heap size $heap_size... ";
        my $success = 1;
        my $successes = 0;
        while ($successes < $invocations) {

          # generate command line
          my $cmdline = gen_command_line($bmgroup, \%configurations, $config, $benchmark, $heap_size, $iterations);
          $cmdline .= " > $scratch_dir/output.txt 2>&1";

          # execute benchmark
          timed_system $cmdline, $timeout;

          # check result
          open OUTPUT, "$scratch_dir/output.txt";
          my $curr_success = 0;
          my $curr_failure = 0;
          while (<OUTPUT>) {
            if (/$success_string/) {
              $curr_success = 1;
            }
            if (/$failure_string/) {
              $curr_failure = 1;
            }
          }
          close OUTPUT;

          # sleep for chart
          if ($benchmark eq "chart") {
            sleep(1);
          }

          # check for success and failure in same run
          if ($curr_success && $curr_failure) {
            die "both succeeded and failed?! quitting";
          }

          if ($curr_success) {
            $successes++;
          } else {

            # save output file for failures
            my $failed_filename;
            my $count = 1;
            while (1) {
              $failed_filename = "$scratch_dir/$benchmark-failed.$count";
              if (not -e $failed_filename) {
                rename("$scratch_dir/output.txt", $failed_filename);
                last;
              }
              $count++;
            }

            if ($curr_failure) { 
              $success = 0;
              last;
            }
          }
        }

        if ($success) {
          ($heap_size < $min_succeeded_heap_size) or die "Assertion failure";
          print "SUCCEEDED\n";
          $min_succeeded_heap_size = $heap_size;
          $heap_size = round_to_nearest($heap_size - ($heap_size - $max_failed_heap_size) / 2, 4);
        } else {
          ($heap_size > $max_failed_heap_size) or die "Assertion failure";
          print "FAILED\n";
          $max_failed_heap_size = $heap_size;
          $heap_size = round_to_nearest($heap_size + ($min_succeeded_heap_size - $heap_size) / 2, 4);
        }
      }
      $heap_sizes->{$benchmark} = $min_succeeded_heap_size;
    }

    print "Results for $config\n";
    foreach my $benchmark (sort keys %$heap_sizes) {
      print "$benchmark => $heap_sizes->{$benchmark}\n";
    }
  }

  unlink "$scratch_dir/output.txt";
}

sub round_to_nearest {
  my ($val, $nearest) = @_;

  $val /= $nearest;
  $val = int($val + 0.5);
  return $val * $nearest;
}
