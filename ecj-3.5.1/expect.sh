#! /bin/bash

if [ x$1 == x ]; then
    echo "Usage: $0 [-not] <text>"
    echo "   OR: $0 [-not] [-shuffle] -file <file>"
    echo "Prints OK or FAILED, with a diff, depending on wether stdin matches.  For"
    echo "plain text, containment is sufficient, but other text requires a full match."
    exit 1
fi

NOT=false
if [ x$1 == x-not ]; then
    NOT=true
    shift 1
fi

FILTER="cat -"
if [ x$1 == x-shuffle ]; then
    FILTER="sort"
    shift 1
fi

if [ x$1 == x-file ]; then
    BASEFILE=`mktemp`
    cat $2 | $FILTER > ${BASEFILE}
    if $FILTER | diff -u ${BASEFILE} -; then
	if ${NOT}
	then 
	    printf "\033[31;1mFAILED\033[0m\n"
	else
	    printf "\033[32;1mOK\033[0m\n"
	fi
    else
	if ${NOT}
	then 
	    printf "\033[32;1mOK\033[0m\n"
	else
	    printf "\033[31;1mFAILED\033[0m\n"
	fi
    fi
    rm -f ${BASEFILE}
else
    if grep -e "`echo "$@"`" -; then
	if ${NOT}
	then 
	    printf "\033[31;1mFAILED\033[0m\n"
	else
	    printf "\033[32;1mOK\033[0m\n"
	fi
    else
	if ${NOT}
	then 
	    printf "\033[32;1mOK\033[0m\n"
	else
	    printf "\033[31;1mFAILED\033[0m\n"
	fi
    fi
fi
