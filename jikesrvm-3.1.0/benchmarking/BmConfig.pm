package BmConfig;
use strict;

require Exporter;
our @ISA = qw{Exporter};
our @EXPORT = qw{ gen_command_line 
                  $jvm98_dir 
                  $dacapo2006_dir
                  $dacapo2009_dir
                  $pjbb2000_dir     };

# where are the benchmarks?
my $home_dir = $ENV{"HOME"};
our $jvm98_dir = "$home_dir/SPECjvm98";
our $dacapo2006_dir = "$home_dir/dacapo";
our $dacapo2009_dir = "$home_dir/dacapo-9.12";
our $pjbb2000_dir = "$home_dir/pseudojbb/src";

# arguments for both the rvm and the benchmarks
my $rvm_args = "-X:gc:variableSizeHeap=false";
my $jvm98_args = "-s100";   # use largest input size
my $dacapo2006_args = "-c MMTkCallback";
my $dacapo2009_args = "-c MMTkCallback";
my $pjbb2000_args = "-propfile SPECjbb.props";

# arguments:
#   $bmgroup = one of "jvm98", "dacapo2006", "dacapo2009", "pjbb2000"
#   $configurations = a reference to a hash of configurations
#   $config = the Jikes RVM configuration to use
#   $benchmark = benchmark to run
#   $heap_size = heap size to use
#   $iterations = number of iterations within a single invocation
sub gen_command_line {
  my ($bmgroup, $configurations, $config, $benchmark, $heap_size, $iterations) = @_;

  if ($bmgroup eq "jvm98") {
    my $cmdline = "$home_dir/$configurations->{$config}[0]/dist/$configurations->{$config}[1]_ia32-linux/rvm -Xms${heap_size}M -Xmx${heap_size}M $configurations->{$config}[2] ";
    $cmdline .= "$rvm_args ";
    $cmdline .= "SpecApplication ";
    $cmdline .= "$jvm98_args ";
    $cmdline .= "$benchmark " x ($iterations-1);
    $cmdline .= "_997_begin $benchmark _998_end ";
  } elsif ($bmgroup eq "dacapo2006") {
    my $cmdline = "";
    if ($benchmark eq "chart") {
      # special case chart -- have to run with xvfb
      $cmdline .= "xvfb-run ";
    }
    $cmdline .= "$home_dir/$configurations->{$config}[0]/dist/$configurations->{$config}[1]_ia32-linux/rvm -Xms${heap_size}M -Xmx${heap_size}M $configurations->{$config}[2] ";
    $cmdline .= "$rvm_args ";
    $cmdline .= "-jar dacapo-2006-10-MR2.jar ";
    $cmdline .= "$dacapo2006_args ";
    $cmdline .= "-n $iterations ";
    $cmdline .= "$benchmark ";
  } elsif ($bmgroup eq "dacapo2009") {
    my $cmdline = "";
    $cmdline .= "$home_dir/$configurations->{$config}[0]/dist/$configurations->{$config}[1]_ia32-linux/rvm -Xms${heap_size}M -Xmx${heap_size}M $configurations->{$config}[2] ";
    $cmdline .= "$rvm_args ";
    $cmdline .= "-jar dacapo-9.12-bach.jar ";
    $cmdline .= "$dacapo2009_args ";
    $cmdline .= "-n $iterations ";
    $cmdline .= "$benchmark ";
  } elsif ($bmgroup eq "pjbb2000") {
    my $cmdline = "$home_dir/$configurations->{$config}[0]/dist/$configurations->{$config}[1]_ia32-linux/rvm -Xms${heap_size}M -Xmx${heap_size}M $configurations->{$config}[2] ";
    $cmdline .= "$rvm_args ";
    $cmdline .= "spec.jbb.JBBmain ";
    $cmdline .= "$pjbb2000_args ";
    $cmdline .= "-iterations $iterations ";
  } else {
    die "Benchmark group $bmgroup not supported";
  }
}
