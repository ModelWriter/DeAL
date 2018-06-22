#! /bin/bash

if [ x$2 == x ]; then
	echo "Usage: $0 <bench1> <bench2>"
	echo "for example, $0 ms gca"	
	exit 1
fi

AVERAGE=`dirname $0`
AVERAGE="$AVERAGE"/average.awk

printf "%-20s%-60s%-60s%-60s\n" benchmark gc\# wallclock gc-time ; for n in avrora luindex lusearch pmd sunflow xalan; do printf "%-20s" $n;  (for k in $1 $2; do for i in 1 2 3; do cat $n.$k.* | awk '/^GC/ {nextprint = 2} { if (nextprint-- == 1) {print $'$i'} }' | ${AVERAGE} ; done; done) | awk '/mean/ { m[++n] = $2} /standar/ { v[n] = $2 } END { for (i = 0; i < 3; i++) { ms = i + 1; gca = i + 4; printf "%-60s", sprintf("%.3f (%.3f [%.3f]; %.3f [%.3f])", m[gca]/m[ms], m[gca], v[gca], m[ms], v[ms])}; printf "\n" }'; done
