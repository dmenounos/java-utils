#!/bin/bash

# default clean
CLN=""

# default lifecycle phase
LCP=""

# default skip tests
STS=""

# default test class
TST=""

# default profile name
PRF=""

# default module name
MDL="."

while getopts ":cl:st:p:m:h" opt; do
	case $opt in
	"c")
		CLN="clean"
		;;
	"l")
		case $OPTARG in
		"compile"|"test"|"package"|"install")
			LCP="$OPTARG"
			;;
		*)
			echo "Valid arguments for -$opt: compile | test | package | install" >&2
			exit 1
			;;
		esac
		;;
	"s")
		STS="-DskipTests"
		;;
	"t")
		TST="-Dtest=$OPTARG"
		;;
	"p")
		PRF="-P$OPTARG"
		;;
	"m")
		if [ ! -d $OPTARG ]; then
			echo "Directory $OPTARG does not exist. Operation aborted." >&2
			exit 1
		fi

		MDL="$OPTARG"
		;;
	"h")
		echo "Usage: $0 [OPTIONS]" >&2
		echo "OPTIONS" >&2
		echo "  -c clean" >&2
		echo "  -l lifecycle phase" >&2
		echo "  -s skip tests" >&2
		echo "  -t test class" >&2
		echo "  -p profile name" >&2
		echo "  -h this information" >&2
		echo "EXAMPLE" >&2
		echo "$0 -c" >&2
		echo "$0 -l compile" >&2
		echo "$0 -l test" >&2
		echo "$0 -l test -t com.example.*.*" >&2
		echo "$0 -c -l package" >&2
		echo "$0 -c -l install" >&2
		exit 1
		;;
	":")
		echo "Option -$OPTARG requires an argument" >&2
		exit 1
		;;
	"?")
		echo "Invalid option: -$OPTARG" >&2
		exit 1
		;;
	esac
done

if [ "$#" -eq "0" ]; then
	$0 -h
	exit 1
fi

if [ -z "$CLN" ] && [ -z "$LCP" ]; then
	$0 -h
	exit 1
fi

CMD="cd $MDL"
echo $CMD
eval $CMD

CMD="mvn $CLN $LCP $STS $TST $PRF"
echo $CMD
eval $CMD
