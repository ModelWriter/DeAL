#! /usr/bin/awk -f

function print_tcount()
{
    printf "  Added: %d + %d root\n", tcount, trootcount;
    tcount = 0;
    trootcount = 0;
}

/collectionPhase/ {
    print_tcount();
    phase = $2 $3 "(" ++phase_counter ")";
    print;
}

/@@tracing/ {
    tcount++;
    obj = $2;
    if (traceset[$2])
	printf "!! Re-encountered object %s from %s in %s\n", obj, traceset[$2], phase;
    traceset[obj] = phase;
}

/@@root/ {
    trootcount++;
    obj = $2;
    if (traceset[$2])
	printf "!! Re-encountered root %s from %s in %s\n", obj, traceset[$2], phase;
    traceset[obj] = phase;
}

END {
    print_tcount();
}