package TimedSystem;
use strict;
use POSIX ":sys_wait_h";

require Exporter;
our @ISA = qw{Exporter};
our @EXPORT = qw{ timed_system };


# This function works similarly to the standard Perl system function, except
# it also enforces a timeout on the child process.  It allows you to execute
# a command line in a different process, and if that process runs longer than
# the timeout, it kills the process.  The timeout value is in seconds.
#
# Returns the exit status if the child exits within the timeout, or -2
# if the child ran longer than the timeout and was killed.
#
# NOTE: The timeout is approximate (within 1-3 seconds).  Do not rely on this 
# for exact timing.
sub timed_system {

  my ($cmdline, $timeout) = @_;

  my $pid = fork();
  if ($pid == 0) {
    # child
    exec $cmdline;
  } else {
    # parent
    my $slept = 0;
    my $died;
    do {
      sleep(1);
      $slept++;
      $died = waitpid($pid, WNOHANG);
    } while ($died != $pid && $slept <= $timeout);

    if ($died != $pid) {
      kill(9, $pid);
      return(-2);
    } else {
      return($?);
    }
  }
}
