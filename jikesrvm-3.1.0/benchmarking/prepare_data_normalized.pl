#!/usr/bin/perl -w
use strict;

# jgraph file output format -- we will output two jgraph files, one for total time and one
# for gc time

my $total_time_file = "fig1.j";
my $gc_time_file = "fig2.j";

my @benchmarks = qw(antlr
    bloat
    chart
    eclipse
    fop
    hsqldb
    jython
    luindex
    lusearch
    pmd
    xalan
    pseudojbb
    compress
    jess
    raytrace
    db
    javac
    mpegaudio
    mtrt
    jack);

if (@ARGV != 1) {
  print "Usage: script.pl input_file\n";
  exit;
}

open(INFILE, "<", $ARGV[0]) or die "Cannot open file: $!";
my $curr_config;
my $curr_benchmark;
my %means;
my %confidences;

# format of the array for each benchmark:
# 0: total time Base
# 1: total time NoAssertions
# 2: total time Assertions
# 3: mutator time Base
# 4: mutator time NoAssertions
# 5: mutator time Assertions
# 6: gc time Base
# 7: gc time NoAssertions
# 8: gc time Assertions
# 9: scan Base
# 10: scan NoAssertions
# 11: scan Assertions
while (<INFILE>) {
  if (/^(Base|NoAssertions|Assertions)\.(.+)$/) {
    $curr_config = $1;
    $curr_benchmark = $2;
    if ($curr_benchmark =~ /_[\d]{3}_(\w+)/) {
      $curr_benchmark = $1;
    }
  } elsif (/^total time -- avg ([\d\.]+), stddev ([\d\.]+), confidence ([\d\.]+)$/) {
    if ($curr_config eq "Base") {
      $means{$curr_benchmark}[0] = $1;
      $confidences{$curr_benchmark}[0] = $3;
    } elsif ($curr_config eq "NoAssertions") {
      $means{$curr_benchmark}[1] = $1;
      $confidences{$curr_benchmark}[1] = $3;
    } elsif ($curr_config eq "Assertions") {
      $means{$curr_benchmark}[2] = $1;
      $confidences{$curr_benchmark}[2] = $3;
    } else {
      print "Error processing line $_: no current config found\n";
    }
  } elsif (/^mutator time -- avg ([\d\.]+), stddev ([\d\.]+), confidence ([\d\.]+)$/) {
    if ($curr_config eq "Base") {
      $means{$curr_benchmark}[3] = $1;
      $confidences{$curr_benchmark}[3] = $3;
    } elsif ($curr_config eq "NoAssertions") {
      $means{$curr_benchmark}[4] = $1;
      $confidences{$curr_benchmark}[4] = $3;
    } elsif ($curr_config eq "Assertions") {
      $means{$curr_benchmark}[5] = $1;
      $confidences{$curr_benchmark}[5] = $3;
    } else {
      print "Error processing line $_: no current config found\n";
    }
  } elsif (/^gc time -- avg ([\d\.]+), stddev ([\d\.]+), confidence ([\d\.]+)$/) {
    if ($curr_config eq "Base") {
      $means{$curr_benchmark}[6] = $1;
      $confidences{$curr_benchmark}[6] = $3;
    } elsif ($curr_config eq "NoAssertions") {
      $means{$curr_benchmark}[7] = $1;
      $confidences{$curr_benchmark}[7] = $3;
    } elsif ($curr_config eq "Assertions") {
      $means{$curr_benchmark}[8] = $1;
      $confidences{$curr_benchmark}[8] = $3;
    } else {
      print "Error processing line $_: no current config found\n";
    }
  } elsif (/^scan -- avg ([\d\.]+), stddev ([\d\.]+), confidence ([\d\.]+)$/) {
    if ($curr_config eq "Base") {
      $means{$curr_benchmark}[9] = $1;
      $confidences{$curr_benchmark}[9] = $3;
    } elsif ($curr_config eq "NoAssertions") {
      $means{$curr_benchmark}[10] = $1;
      $confidences{$curr_benchmark}[10] = $3;
    } elsif ($curr_config eq "Assertions") {
      $means{$curr_benchmark}[11] = $1;
      $confidences{$curr_benchmark}[11] = $3;
    } else {
      print "Error processing line $_: no current config found\n";
    }
  }
}
close INFILE;

# calculate geomean of slowdowns. format:
# 0 - product of NoAssertions.total slowdown factors
# 1 - product of NoAssertions.mutator slowdown factors
# 2 - product of NoAssertions.gc slowdown factors
# 3 - count of things we multiplied
my @slowdowns = (1, 1, 1, 1);
foreach my $bm (@benchmarks) {
  $slowdowns[0] *= $means{$bm}[1]/$means{$bm}[0];
  $slowdowns[1] *= $means{$bm}[4]/$means{$bm}[3];
  $slowdowns[2] *= $means{$bm}[7]/$means{$bm}[6];
  $slowdowns[3]++;
}
my $geomean_time = $slowdowns[0]**(1/$slowdowns[3]);
my $geomean_mutator = $slowdowns[1]**(1/$slowdowns[3]);
my $geomean_gc = $slowdowns[2]**(1/$slowdowns[3]);

# generate jgraph files

open(TOTALTIMEFILE, ">", $total_time_file) or die "Cannot open file $total_time_file: $!";
open(GCTIMEFILE, ">", $gc_time_file) or die "Cannot open file $gc_time_file: $!";

# header
my $xmax = (scalar(@benchmarks) + 1) * 5 + 3;
print TOTALTIMEFILE "newgraph\n\nborder\n\n";
print TOTALTIMEFILE "xaxis\n min 0 max $xmax\n hash 0 mhash 0 no_auto_hash_labels\n size 5.6\n hash_labels fontsize 8 rotate -45 hjl vjb\n\n";
print TOTALTIMEFILE "yaxis size 2\nyaxis min 0 max 150 grid_lines grid_gray 0.6\n\n";

print GCTIMEFILE "newgraph\n\nborder\n\n";
print GCTIMEFILE "xaxis\n min 0 max $xmax\n hash 0 mhash 0 no_auto_hash_labels\n size 5.6\n hash_labels fontsize 8 rotate -45 hjl vjb\n\n";
print GCTIMEFILE "yaxis size 2\nyaxis min 0 max 200 grid_lines grid_gray 0.6\n\n";

# names of benchmarks
my $index = 3;
print TOTALTIMEFILE "xaxis hash_labels fontsize 7\n";
print GCTIMEFILE "xaxis hash_labels fontsize 7\n";
foreach my $bm (@benchmarks) {
  print TOTALTIMEFILE " hash_label at $index : $bm\n";
  print GCTIMEFILE " hash_label at $index : $bm\n";
  $index += 5;
}
$index += 2;
print TOTALTIMEFILE " hash_label at $index : geomean\n\n";
print GCTIMEFILE " hash_label at $index : geomean\n\n";

# legend
print TOTALTIMEFILE "legend defaults x 110 y 100 hjl vjb\n\nyaxis label fontsize 10 : Normalized execution time\n\n";
print GCTIMEFILE "legend defaults x 110 y 135 hjl vjb\n\nyaxis label fontsize 10 : Normalized GC time\n\n";

# Base bars
$index = 2;
print TOTALTIMEFILE "newcurve marktype xbar marksize 1.7 gray 0.4 label : Base\n";
print GCTIMEFILE "newcurve marktype xbar marksize 1.7 gray 0.4 label : Base\n";
foreach my $bm (@benchmarks) {
  print TOTALTIMEFILE " pts $index 100.0\n";
  print GCTIMEFILE " pts $index 100.0\n";
  $index += 5;
}
$index += 2;
print TOTALTIMEFILE " pts $index 100.0\n\n";    # geomean
print GCTIMEFILE " pts $index 100.0\n\n";    # geomean

# Base error bars
$index = 2;
print TOTALTIMEFILE "newcurve marktype none\n";
print GCTIMEFILE "newcurve marktype none\n";
foreach my $bm (@benchmarks) {
  my $confidence = 100*$confidences{$bm}[0]/$means{$bm}[0];
  printf TOTALTIMEFILE " y_epts %s 100.0 %0.1f %0.1f\n", $index, 100.0-$confidence, 100.0+$confidence;
  $confidence = 100*$confidences{$bm}[6]/$means{$bm}[6];
  printf GCTIMEFILE " y_epts %s 100.0 %0.1f %0.1f\n", $index, 100.0-$confidence, 100.0+$confidence;
  $index += 5;
}
print TOTALTIMEFILE "\n";
print GCTIMEFILE "\n";

# NoAssertions bars
$index = 4;
print TOTALTIMEFILE "newcurve marktype xbar marksize 1.7 gray 0.8 label : NoAssertions\n\n";
print GCTIMEFILE "newcurve marktype xbar marksize 1.7 gray 0.8 label : NoAssertions\n\n";
foreach my $bm (@benchmarks) {
  printf TOTALTIMEFILE " pts %d %0.1f\n", $index, 100*$means{$bm}[1]/$means{$bm}[0];
  printf GCTIMEFILE " pts %d %0.1f\n", $index, 100*$means{$bm}[7]/$means{$bm}[6];
  $index += 5;
}
$index += 2;
printf TOTALTIMEFILE " pts %d %0.1f\n\n", $index, 100*$geomean_time;    # geomean
printf GCTIMEFILE " pts %d %0.1f\n\n", $index, 100*$geomean_gc;    # geomean

# NoAssertions error bars
$index = 4;
print TOTALTIMEFILE "newcurve marktype none\n";
print GCTIMEFILE "newcurve marktype none\n";
foreach my $bm (@benchmarks) {
  my $mean = 100*$means{$bm}[1]/$means{$bm}[0];
  my $confidence = 100*$confidences{$bm}[1]/$means{$bm}[0];
  printf TOTALTIMEFILE " y_epts %s %0.1f %0.1f %0.1f\n", $index, $mean, $mean-$confidence, $mean+$confidence;
  $mean = 100*$means{$bm}[7]/$means{$bm}[6];
  $confidence = 100*$confidences{$bm}[7]/$means{$bm}[6];
  printf GCTIMEFILE " y_epts %s %0.1f %0.1f %0.1f\n", $index, $mean, $mean-$confidence, $mean+$confidence;
  $index += 5;
}
print TOTALTIMEFILE "\n";
print GCTIMEFILE "\n";

close TOTALTIMEFILE;
close GCTIMEFILE;

print "geomean total time: $geomean_time\n";
print "geomean mutator time: $geomean_mutator\n";
print "geomean gc time: $geomean_gc\n";
