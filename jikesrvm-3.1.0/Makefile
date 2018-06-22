# Default to basebase configuration
#CP=${CLASSPATH}:target/BaseBaseGCAssertions_x86_64-linux/jksvm.jar:.
#JAVAC=javac -classpath ${CP}
#JAVA=./dist/BaseBaseGCAssertions_x86_64-linux/rvm -classpath ${CP}
JAVA=JAVA
JAVAC=JAVAC

.PHONY: default basebase fastadaptive bytecode-verifier

default: basebase

basebase: CP="${CLASSPATH}:target/BaseBaseGCAssertions_x86_64-linux/jksvm.jar:."
basebase: JAVAC=javac -classpath ${CP}
basebase: JAVA="./dist/BaseBaseGCAssertions_x86_64-linux/rvm"
basebase: all-tests

fastadaptive: CP = "${CLASSPATH}:target/FastAdaptiveGCAssertions_x86_64-linux/jksvm.jar:."
fastadaptive: JAVAC = javac -classpath ${CP}
fastadaptive: JAVA = "./dist/FastAdaptiveGCAssertions_x86_64-linux/rvm"
fastadaptive: all-tests

all-tests: gtest/Tests.class gtest/TestHarness.class
	${JAVA} gtest.TestHarness gtest.Tests gtest.Tests

bytecode-verifier: CP="${CLASSPATH}:../../../target/BaseBaseGCAssertions_x86_64-linux/jksvm.jar:."
bytecode-verifier: JAVAC=javac -classpath ${CP}
bytecode-verifier: JAVA=../../../dist/BaseBaseGCAssertions_x86_64-linux/rvm -classpath ${CP}
bytecode-verifier:
	(cd gtest/bytecode-hack/sound; ${JAVAC} Test.java)
	(cd gtest/bytecode-hack/unsound; ${JAVAC} C.java; cp ../sound/Test.class .; ${JAVA} Test)

avrora-annot: CP="${CLASSPATH}:target/BaseBaseGCAssertions_x86_64-linux/jksvm.jar:."
avrora-annot: JAVAC="javac -classpath ${CP}"
avrora-annot: JAVA="./dist/BaseBaseGCAssertions_x86_64-linux/rvm"
avrora-annot:
	${JAVA} -jar dacapo-9.12-bach.jar -c MMTkCallback avrora

avrora-ms: CP="${CLASSPATH}:target/BaseBaseMarkSweep_x86_64-linux/jksvm.jar:."
avrora-ms: JAVAC="javac -classpath ${CP}"
avrora-ms: JAVA="./dist/BaseBaseMarkSweep_x86_64-linux/rvm"
avrora-ms:
	${JAVA} -jar dacapo-9.12-bach.jar -c MMTkCallback avrora
#	${JAVA} -jar dacapo-9.12-bach.jar avrora

avrora-fa-annot: CP="${CLASSPATH}:target/FastAdaptiveGCAssertions_x86_64-linux/jksvm.jar:."
avrora-fa-annot: JAVAC="javac -classpath ${CP}"
avrora-fa-annot: JAVA="./dist/FastAdaptiveGCAssertions_x86_64-linux/rvm"
avrora-fa-annot:
	${JAVA} -jar dacapo-9.12-bach.jar -c MMTkCallback avrora

avrora-fa-ms: CP="${CLASSPATH}:target/FastAdaptiveMarkSweep_x86_64-linux/jksvm.jar:."
avrora-fa-ms: JAVAC="javac -classpath ${CP}"
avrora-fa-ms: JAVA="./dist/FastAdaptiveMarkSweep_x86_64-linux/rvm"
avrora-fa-ms:
	${JAVA} -jar dacapo-9.12-bach.jar -c MMTkCallback avrora
#	${JAVA} -jar dacapo-9.12-bach.jar avrora

avrora-gen: CP="${CLASSPATH}:target/BaseBaseGenMS_x86_64-linux/jksvm.jar:."
avrora-gen: JAVAC="javac -classpath ${CP}"
avrora-gen: JAVA="./dist/BaseBaseGenMS_x86_64-linux/rvm"
avrora-gen:
	${JAVA} -jar dacapo-9.12-bach.jar -c MMTkCallback avrora
#	${JAVA} -jar dacapo-9.12-bach.jar avrora

%.class: %.java
	${JAVAC} $<
