ECJ=./ecj -gcaDebug:all
ECJSILENT=./ecj
ECJAST=./ecj -gcaDebug:ast
ECJT=./ecj -gcaDebug:T
ECJHOOK=./ecj -gcaDebug:hook
EXPECT=./expect.sh
JAVAC=javac
JAVA=java
SPECSTUB=./org/mmtk/plan/marksweep/gcassertions/spec/
JIKES=../jikesrvm-3.1.0/dist/FastAdaptiveGCAssertions_x86_64-linux/rvm
#JIKES=../../../jikes/jikesrvm-3.1.0-gcassertions/dist/FastAdaptiveGCAssertions_x86_64-linux/rvm

.PHONY: build test test-disjoint default test-ast test-typecheck test-sanitycheck test-T test-magic test-stub test-jikes test-bugs

default: build test

test: test-disjoint test-ast test-typecheck test-sanitycheck test-T test-magic test-stub test-jikes
nonjikes-test: test-disjoint test-ast test-typecheck test-sanitycheck test-T test-magic test-stub

build:
	ant

test-disjoint:
	${ECJHOOK} test/Node.java test/HookPresentAssert.java | ${EXPECT} ASSERTION assert
	${ECJHOOK} test/Node.java test/HookPresentAssertDisjoint.java | ${EXPECT} ASSERTION assertDisjoint
	${ECJHOOK} test/Node.java test/HookPresentAssumeDisjoint.java | ${EXPECT} ASSERTION assumeDisjoint

test-ast:
	${ECJAST} test/Node.java test/HookPresentAssert.java 2>/dev/null | ${EXPECT} -file test/HookPresentAssert.expect
	${ECJAST} test/Node.java test/ASTOpPrecedence.java 2>/dev/null | ${EXPECT} -file test/ASTOpPrecedence.expect
	${ECJAST} test/Node.java test/ReachSort.java 2>/dev/null | ${EXPECT} -file test/ReachSort.expect

test-typecheck:
	${ECJ} test/Node.java test/TyCheck0.java 2>/dev/null 2>&1 | ${EXPECT} ERROR
	${ECJ} test/Node.java test/TyCheck1.java 2>/dev/null 2>&1 | ${EXPECT} ERROR
	${ECJ} test/Node.java test/TyCheck2.java 2>/dev/null 2>&1 | ${EXPECT} ERROR
	${ECJ} test/Node.java test/TyCheck3.java 2>/dev/null 2>&1 | ${EXPECT} ERROR
	${ECJ} test/Node.java test/TyCheck4.java 2>/dev/null 2>&1 | ${EXPECT} ERROR
	${ECJ} test/Node.java test/TyCheck5.java 2>/dev/null 2>&1 | ${EXPECT} ERROR
	${ECJ} test/Node.java test/TyCheck6.java 2>/dev/null 2>&1 | ${EXPECT} ERROR
	${ECJ} test/Node.java test/TyCheck7.java 2>/dev/null 2>&1 | ${EXPECT} ERROR
	${ECJ} test/Node.java test/TyCheck8.java 2>/dev/null 2>&1 | ${EXPECT} ERROR

test-sanitycheck:
	${ECJ} test/Node.java test/SanityCheck0.java 2>/dev/null 2>&1 | ${EXPECT} ERROR
	${ECJ} test/Node.java test/SanityCheck1.java 2>/dev/null 2>&1 | ${EXPECT} ERROR
	${ECJ} test/Node.java test/SanityCheck2.java 2>/dev/null 2>&1 | ${EXPECT} ERROR
	${ECJ} test/Node.java test/SanityCheck3.java 2>/dev/null 2>&1 | ${EXPECT} ERROR
	${ECJ} test/Node.java test/SanityCheck4.java 2>/dev/null 2>&1 | ${EXPECT} ERROR
	${ECJ} test/Node.java test/SanityCheck5.java 2>/dev/null 2>&1 | ${EXPECT} ERROR
	${ECJ} test/Node.java test/SanityCheck6.java 2>/dev/null 2>&1 | ${EXPECT} ERROR
	${ECJ} test/Node.java test/SanityCheck7.java 2>/dev/null 2>&1 | ${EXPECT} -not ERROR
	${ECJ} test/Node.java test/SanityCheck8.java 2>/dev/null 2>&1 | ${EXPECT} -not ERROR
	${ECJ} test/Node.java test/SanityCheck9.java 2>/dev/null 2>&1 | ${EXPECT} -not ERROR

test-T:
	${ECJT} test/Node.java test/TestT0.java 2>/dev/null 2>&1 | ${EXPECT} -shuffle -file test/TestT0.expect
	${ECJT} test/Node.java test/TestT1.java 2>/dev/null 2>&1 | ${EXPECT} -shuffle -file test/TestT1.expect
	${ECJT} test/Node.java test/TestT2.java 2>/dev/null 2>&1 | ${EXPECT} -shuffle -file test/TestT2.expect
	${ECJT} test/Node.java test/TestT3.java 2>/dev/null 2>&1 | ${EXPECT} -shuffle -file test/TestT3.expect

test-magic:
	${ECJSILENT} test/Node.java test/MagicTestLibrary.java
	${JAVAC} -cp . test/TestHarness.java test/MagicChecker.java 
	${JAVA} -cp . test.MagicChecker

test-stub:
	${JAVAC} test/Node.java ${SPECSTUB}/*.java
	${ECJSILENT} -1.5 -cp . test/HTIT*.java
	${JAVA} -cp . test.HTIT0 2>&1 | ${EXPECT} -not Exception
	${JAVA} -cp . test.HTIT1 2>&1 | ${EXPECT} -not Exception
	${JAVA} -cp . test.HTIT2 2>&1 | ${EXPECT} -not Exception
	${JAVA} -cp . test.HTIT3 2>&1 | ${EXPECT} -not Exception
	${JAVA} -cp . test.HTIT4 2>&1 | ${EXPECT} -not Exception
	${JAVA} -cp . test.HTIT5 2>&1 | ${EXPECT} -not Exception
	${JAVA} -cp . test.HTIT6 2>&1 | ${EXPECT} -not Exception

test-jikes:
	rm -f ${SPECSTUB}/*.class
	${ECJSILENT} -1.5 -cp . test/HTIT*.java
	${JIKES} test.HTIT0 2>&1 | ${EXPECT} all-clear
	${JIKES} test.HTIT1 2>&1 | ${EXPECT} Error
	${JIKES} test.HTIT2 2>&1 | ${EXPECT} all-clear
	${JIKES} test.HTIT3 2>&1 | ${EXPECT} all-clear
	${JIKES} test.HTIT4 2>&1 | ${EXPECT} Error
	${JIKES} test.HTIT5 2>&1 | ${EXPECT} all-clear
	${JIKES} test.HTIT6 2>&1 | ${EXPECT} all-clear
#	${JIKES} -X:gc:assertfrequency=60 -X:gc:assertonheapfull=true  -Xms2M -Xmx10M test.HTIT7
	${JIKES} -X:gc:assertfrequency=1 -X:gc:assertonheapfull=true  -Xms5M -Xmx20M test.HTIT7 2>&1 | ${EXPECT} all-clear
	${JIKES} test.HTIT8 2>&1 | ${EXPECT} all-clear

t:
	rm -f ${SPECSTUB}/*.class
	${ECJSILENT} -gcaDebug:all -1.5 -cp . test/HTIT8.java
	${JIKES} test.HTIT8

test-bugs:
	${ECJSILENT} -6 test/LLNode.java test/DLL.java 2>&1 | ${EXPECT} -file /dev/null
