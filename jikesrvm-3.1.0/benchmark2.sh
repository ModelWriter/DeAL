#! /bin/sh
HEAPSIZE=150m

# Apologies for the spaghetti style, but this was a command-line one-liner.

# Varying the environment, as per Todd Mytkowicz, Amer Diwan, Matthias Hauswirth, Peter F. Sweeney: "Producing wrong data without doing anything obviously wrong!" ASPLOS 2009: 265-276

mkdir bench

export PADDING=0000000
for n in `seq 0 3`
do  PADDING=${PADDING}00000000
    for B in luindex lusearch pmd sunflow xalan
    do  for K in "gca1" "gca3"
	do  G=""

	    if [ $K == gca1 ]
	    then G=-X:gc:dummyGCAssertion=true
	    fi

	    if [ $K == gca3 ]
	    then G="-X:gc:dummyGCAssertionwithinstanceof=true"
	    fi

	    VM="GCAssertions"
	    if [ $K == ms ]
	    then VM="MarkSweep"
	    fi

	    ./dist/FastAdaptive${VM}_x86_64-linux/rvm  -Xms${HEAPSIZE} -Xmx${HEAPSIZE} -X:gc:variableSizeHeap=false $G -jar dacapo-9.12-bach.jar -c MMTkCallback -n 4 $B >& bench/${B}.${K}.${n}; echo $n $B $VM $K
	done
    done
done
