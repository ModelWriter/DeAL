#!/usr/bin/perl -w
use strict;
use Cwd;
use File::Path;
use BmConfig;

# Benchmark setup for the SPECjvm98, pseudojbb 2000, dacapo 2006, and 
# dacapo 2009 benchmarks.  We will run each benchmark $iterations times, 
# take the last result, repeat this $invocations times, and compute the 
# mean, median, and std deviation of the $invocations final results.
#
# This script may be run from any directory and will leave results in the 
# current working directory under "results."
#
# We selected $iterations = 4 and $invocations = 20 based on a recommendation
# from Steve Blackburn via email 3/19/2009.
#
# Usage: ./run_benchmarks.pl [list of benchmarks]
# Valid benchmark names are jvm98, dacapo2006, dacapo2009, and pjbb2000.
# If no benchmarks are provided, runs all of them (SPECjvm98, pseudoJBB2000, 
# Dacapo 2006, and Dacapo 2009).  Otherwise it runs only the ones in the 
# list.

# benchmark run parameters
my $iterations = 4;       # number of iterations per benchmark run, timing only the last one
my $invocations = 20;     # number of times to repeat each benchmark
my $heap_multiple = 2;    # multiple of the minimum heap size to test

# minimum heap size for each benchmark
# Found experimentally by EEA 3/2010 for configuration
# FastAdaptiveMarkSweep with Jikes RVM 3.1.0
my %benchmarks = (
    jvm98 => {
      _201_compress => 20,
      _202_jess => 24,
      _205_raytrace => 12,
      _209_db => 20,
      _213_javac => 28,
      _222_mpegaudio => 12,
      _227_mtrt => 12,
      _228_jack => 28,
    },
    dacapo2006 => {
      antlr => 28,
      bloat => 52,
      chart => 52,
      eclipse => 112,
      fop => 44,
      hsqldb => 120,
      jython => 44,
      luindex => 28,
      lusearch => 40,
      pmd => 52,
      xalan => 52,
    },
    # Jikes RVM 3.1.0 FastAdaptiveMarkSweep fails many of the dacapo2009 benchmarks
    dacapo2009 => {
      avrora => 32,
      # batik => ??,
      # eclipse => ??,
      # fop => ??,
      # h2 => ??,
      # jython => ??,
      luindex => 28,
      lusearch => 32,
      # pmd => ??,
      sunflow => 28,
      # tomcat => ??,
      # tradebeans => ??,
      # tradesoap => ??,
      xalan => 36,
    },
    pjbb2000 => {
      pjbb2000 => 60,
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

# configurations include the directory that contains this version of jikesrvm
# and the name of the configuration to use
my %configurations = (
#  Production => ["jikesrvm-3.1.0-unmodified", "production"],
  Base => ["jikesrvm-3.1.0-unmodified", "FastAdaptiveMarkSweep", "" ],
#  NoAssertions => ["jikesrvm-3.1.0", "FastAdaptiveGCAssertions"],
);

# create results directory
my $cwd = getcwd;
my $result_dir = "$cwd/results";
if (not -d $result_dir) {
  mkdir($result_dir) or die "Cannot mkdir $result_dir: $!";
}

foreach my $bmgroup (@bmgroups) {
  my $minimum_heap_sizes = $benchmarks{$bmgroup};

  # chdir into benchmark directory
  if ($bmgroup eq "jvm98") {
    chdir($jvm98_dir) or die "Cannot chdir to $jvm98_dir: $!";
  } elsif ($bmgroup eq "dacapo2006") {
    chdir($dacapo2006_dir) or die "Cannot chdir to $dacapo2006_dir: $!";
  } elsif ($bmgroup eq "dacapo2009") {
    chdir($dacapo2009_dir) or die "Cannot chdir to $dacapo2009_dir: $!";
  } elsif ($bmgroup eq "pjbb2000") {
    chdir($pjbb2000_dir) or die "Cannot chdir to $pjbb2000_dir: $!";
  } else {
    die "Benchmark group $bmgroup not supported";
  }

  foreach my $config (sort keys %configurations) {
    foreach my $benchmark (sort keys %$minimum_heap_sizes) {

      # set heap size 
      my $heap_size = $$minimum_heap_sizes{$benchmark} * $heap_multiple;

      for my $i (1 .. $invocations) {
        my $outfile = "$config.$bmgroup.$benchmark.$i";
        print "Running $outfile with heap size $heap_size...\n";
        my $cmdline = gen_command_line($bmgroup, \%configurations, $config, $benchmark, $heap_size, $iterations);
        $cmdline .= " > $result_dir/$outfile 2>&1";

        my $failure_count = 0;
        while (1) {
          my $result = system($cmdline);
          if ($result == 0) {
            last;
          }
          $failure_count++;
          if ($failure_count >= 10) {
            print "$outfile failed $failure_count times, giving up\n";
            last;
          } else {
            print "$outfile failed, retrying\n";
          }
        }
      }
    }
  }

}
  
