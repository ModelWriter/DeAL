Eddie Aftandilian
eaftan@cs.tufts.edu

This directory contains scripts to automate benchmarking Jikes RVM.  These 
scripts support running and analyzing 4 different benchmark suites:
- SPEC JVM 98
- Pseudo JBB 2000
- Dacapo 2006 MR2
- Dacapo 9.12 (2009 release)
We may want to consider supporting SPEC JBB 2005 and SPEC JVM 2008 in 
the future.

find_min_heap_sizes.pl finds the minimum heap size for each benchmark for
a given set of configurations of Jikes RVM.  It has a 20-minute timeout
per invocation to catch JVM nontermination.  However, it a benchmark fails
repeatedly (i.e. Jikes RVM just won't run that benchmark), the script will 
not terminate.
Usage: ./find_min_heap_sizes.pl > min_heap_sizes.txt

run_benchmarks.pl runs the benchmarks.  It runs each benchmark through 4 
iterations, timing only the last one, and repeats 20 times to reduce 
variation from the adaptive optimizer.  Methodology was suggested by Steve 
Blackburn via email.  Results include the full output from each benchmark run 
and are stored in the current directory under "results."

compile_results.pl takes the output from the run scripts and compiles them 
into summaries, computing the mean, standard deviation, and 90% confidence 
intervals for all the different time values we have.

Order of use:
1) Find minimum heap sizes, if not done already:
   find_min_heap_sizes.pl > min_heap_sizes.txt
2) Update run_benchmarks.pl with minimum heap sizes.
3) run_benchmarks.pl
4) compile_results.pl [results dir] > output_all.txt

TODO: Currently we have scripts that normalize data against the Base 
configuration and output jgraph files that we can render to EPS or PDF.  In
these scripts, the names of the configurations are hardcoded.  We may want
to make them configurable via the command line or a configuration file.
