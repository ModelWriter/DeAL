package org.eclipse.jdt.internal.compiler.gca;

import java.util.*;

import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.compiler.ast.*;

// Manually implemented recursive descent parser.  Ah well.
public class Parser implements OperatorIds
{
	private Scanner scanner;
	private ProblemReporter problem_reporter;

	private void
	dumpScanner(final String s)
	{
		System.out.print(s + ":  ");
		_dump_scanner();
		System.out.println();
	}

	private int
	pos()
	{
		return scanner.offset();
	}

	private long
	combo_pos()
	{
		return ((0L | (scanner.offset())) << 32) | scanner.offset();
	}

	private void
	_dump_scanner()
	{
		final Token t = this.scanner.next();
		if (t != null) {
			System.out.print(t + "  ");
			_dump_scanner();
		}
		this.scanner.pushBack(t);
	}

	Set name_set;

	public
	Parser(final Scanner _scanner, final ProblemReporter _problem_reporter, final Set _name_collector)
	{
		this.name_set = _name_collector;
		this.scanner = _scanner;
		this.problem_reporter = _problem_reporter;
	}

	public static Expression
	parse(final int _source_start, final int _source_end, final String body, final int assertion_kind, ProblemReporter pr)
	{
		final HashSet name_set = new HashSet();
		final Parser p = new Parser(new Scanner(body, _source_start), pr, name_set);

		final Assertion a = p.parseAssertion(assertion_kind);
		a.contained_names = name_set;

		if (a == null)
			return new IntLiteral(new char[] {'0'}, _source_start, _source_end);
		else {
			a.sourceStart = _source_start;
			a.sourceEnd = _source_end;
			Debug.println("ast", a.toString());
			return a;
		}
	}

	public void
	fail(String message)
	{
		final String msg = "GCA Parse failed: " + message + "; next token: " + scanner.next();
		try {
			throw new RuntimeException(msg);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		this.problem_reporter.abortDueToInternalError(msg);
	}

	public boolean
	accept(int kind)
	{
		final Token t = this.scanner.next();
		if (t == null
		    || t.kind != kind) {
			this.scanner.pushBack(t);
			return false;
		}
		return true;
	}

	public Token
	peek()
	{
		final Token t = this.scanner.next();
		this.scanner.pushBack(t);
		return t;
	}

	public Expression
	expectExpression(final Expression e)
	{
		if (e == null)
			fail("Missing expression");
		return e;
	}

	public String
	acceptID()
	{
		final Token t = this.scanner.next();
		if (t == null
		    || t.kind != Token.ID) {
			this.scanner.pushBack(t);
			return null;
		}
		return t.toString();
	}

	public Expression
	acceptDoubleWithPrefix(final String pfx)
	{
		final Token t = this.scanner.next();

		if (t == null || t.kind != Token.PERIOD) {
			this.scanner.pushBack(t);
			return null;
		}

		final Token t2 = this.scanner.next();
		if (t2 == null) {
			this.scanner.pushBack(t);
			return null;
		}

		if (t2.kind != Token.INT) {
			this.scanner.pushBack(t2);
			this.scanner.pushBack(t);
			return null;
		}

		return new DoubleLiteral((pfx + t + t2).toCharArray(), pos(), pos());
	}

	public Expression
	acceptNumber()
	{
		final Token t = this.scanner.next();
		if (t == null)
			return null;

		if (t.kind != Token.INT) {
			// ".2" format double?
			final Expression retval = acceptDoubleWithPrefix("");
			if (retval == null)
				this.scanner.pushBack(t);
			return retval;
		}

		final String integer = t.toString();
		final Expression maybe_double = acceptDoubleWithPrefix(integer);

		if (maybe_double != null)
			return maybe_double;
		else
			return new IntLiteral(integer.toCharArray(), pos(), pos());
	}

	public void
	expect(int kind, String token)
	{
		if (accept(kind))
			return;
		fail("Expected " + token);
	}

	public String
	expectID()
	{
		String s;
		if (null != (s = acceptID()))
			return s;
		fail("Expected identifier");
		return null;
	}

	public SingleNameReference
	useName(SingleNameReference snr)
	{
		this.name_set.add(snr);
		return snr;
	}

	public SingleNameReference
	acceptSingleNameReference()
	{
		final String n = acceptID();
		if (n == null)
			return null;
		else
			return useName(new SingleNameReference(n.toCharArray(), combo_pos()));
	}

	public SingleNameReference
	expectSingleNameReference()
	{
		final String n = expectID();
		return useName(new SingleNameReference(n.toCharArray(), combo_pos()));
	}

	public Assertion
	parseAssertion(final int kind)
	{
		final Expression e = parseAssertionExpr(null);
		final Token t = scanner.next();

		if (t != null)
			fail("expected Assertion, but found trailing token " + t);

		return new Assertion(kind, e);
	}

	// fixme: take care of sourceStart, sourceEnd

	// Assertion ::= <Assertion> '<->' <AssertionImplies>
	// 	       | <AssertionImplies>
	public Expression
	parseAssertionExpr(final Expression lhs)
	{
		Expression e = parseAssertionImplies(null);

		if (lhs != null) {
			if (e == null)
				fail("<-> without rhs");

			e = new EqualExpression(lhs, e, Token.EQ);
		}

		if (e == null)
			return null;

		if (accept(Token.IFF))
			return parseAssertionExpr(e);
		else
			return e;
	}

	// AssertionImplies ::= <AssertionImplies> '->' <AssertionOr>
	// 	              | <AssertionOr>
	public Expression
	parseAssertionImplies(final Expression lhs)
	{
		Expression e = parseAssertionOr(null);

		if (lhs != null) {
			if (e == null)
				fail("-> without rhs");

			e = new OR_OR_Expression(new UnaryExpression(lhs, NOT), e, OR_OR);
		}

		if (e == null)
			return null;

		if (accept(Token.IMPLIES))
			return parseAssertionImplies(e);
		else
			return e;
	}

	// AssertionOr ::= <AssertionOr> '||' <AssertionAnd>
	// 	         | <AssertionAnd>
	public Expression
	parseAssertionOr(final Expression lhs)
	{
		Expression e = parseAssertionAnd(null);

		if (lhs != null) {
			if (e == null)
				fail("|| without rhs");

			e = new OR_OR_Expression(lhs, e, OR_OR);
		}

		if (e == null)
			return null;

		if (accept(Token.OR))
			return parseAssertionOr(e);
		else
			return e;
	}

	// AssertionAnd ::= <AssertionAnd> '||' <AssertionAtom>
	// 	          | <AssertionAtom>
	public Expression
	parseAssertionAnd(final Expression lhs)
	{
		Expression e = parseAssertionAtom();

		if (lhs != null) {
			if (e == null)
				fail("&& without rhs");

			e = new AND_AND_Expression(lhs, e, AND_AND);
		}

		if (e == null)
			return null;

		if (accept(Token.AND))
			return parseAssertionAnd(e);
		else
			return e;
	}

	// AssertionAtom ::= '(' <Assertion> ')'
	// 	        | <QExpr>
	// 		| '!' <AssertionAtom>
	// 	    	| <BoolEq>
	public Expression
	parseAssertionAtom()
	{
		Expression e = null;

		if (accept(Token.OPAREN)) {
			e = parseAssertionExpr(null);
			expect(Token.CPAREN, ")");
			return e;
		}

		if (accept(Token.NOT))
			return new UnaryExpression(expectExpression(parseAssertionAtom()), NOT);

		if (null != (e = parseQExpr()))
			return e;

		if (null != (e = parseBoolEq(null, -1)))
			return e;
		return null;
	}

	public TypeReference
	parseType()
	{
		String s = acceptID();

		if (accept(Token.PERIOD)) {
			LinkedList components = new LinkedList();

			components.addLast(s);

			do {
				components.addLast(expectID());
			} while (accept(Token.PERIOD));

			Object[] onames = components.toArray();
			char[][] names = new char[onames.length][];
			long[] pos = new long[onames.length];

			for (int i = 0; i < onames.length; i++) {
				names[i] = ((String)onames[i]).toCharArray();
				pos[i] = combo_pos();
			}

			return new QualifiedTypeReference(names, pos);

		} else
			return new SingleTypeReference(s.toCharArray(), pos());
	}

	public Argument
	parseArgument(final TypeReference tr)
	{
		String s = expectID();

		return new Argument(s.toCharArray(), combo_pos(), tr, 0 /* modifiers */);
	}

	// Quant ::= 'forall' | 'exists'
	// QExpr ::= <Quant> <Type> <QName> ':' <BoolExpr>
	public Expression
	parseQExpr()
	{
		boolean existential;

		if (accept(Token.EXISTS))
			existential = true;
		else if (accept(Token.FORALL))
			existential = false;
		else return null;

		final TypeReference ty = parseType();
		if (ty == null)
			fail("Quantifier not followed by type");

		final Argument var = parseArgument(ty);
		if (var == null)
			fail("Quantifier and type not followed by identifier");

		expect(Token.COLON, ":");

		final Expression body = parseBoolExpr(null);
		if (body == null)
			fail("Quantification without body");

		return new QuantifiedExpression(existential, var, body);
	}


	public int
	acceptOneToken(final int[] tokens)
	{
		for (int i = 0; i < tokens.length; i++)
			if (accept(tokens[i]))
				return tokens[i];
		return -1;
	}

	// BinEqRel ::= '=='
	//            | '!='
	final int[] eq_ops = new int[] { Token.EQ, Token.NE };

        // BinOpRel ::= '<='
	// 	      | '<'
	// 	      | '>'
	// 	      | '>='
	final int[] linear_order_binops = new int[] { Token.LE, Token.LT, Token.GT, Token.GE };

	// BoolExpr ::= <BoolExpr> '<->' <BoolExprImplies>
	//            | <BoolExprImplies>
	public Expression
	parseBoolExpr(final Expression lhs)
	{
		Expression e = parseBoolExprImplies(null);

		if (lhs != null) {
			if (e == null)
				fail("<-> without rhs");

			e = new EqualExpression(lhs, e, Token.EQ);
		}

		if (e == null)
			return null;

		if (accept(Token.IFF))
			return parseBoolExpr(e);
		else
			return e;
	}


	// BoolExprImplies ::= <BoolExprImplies> '->' <BoolExprOr>
	//                   | <BoolExprOr>
	public Expression
	parseBoolExprImplies(final Expression lhs)
	{
		Expression e = parseBoolExprOr(null);

		if (lhs != null) {
			if (e == null)
				fail("-> without rhs");

			e = new OR_OR_Expression(new UnaryExpression(lhs, NOT), e, OR_OR);
		}

		if (e == null)
			return null;

		if (accept(Token.IMPLIES))
			return parseBoolExprImplies(e);
		else
			return e;
	}


	// BoolExprOr ::= <BoolExprOr> '||' <BoolExprAnd>
	// 	        | <BoolExprAnd>
	public Expression
	parseBoolExprOr(final Expression lhs)
	{
		Expression e = parseBoolExprAnd(null);

		if (lhs != null) {
			if (e == null)
				fail("|| without rhs");

			e = new OR_OR_Expression(lhs, e, OR_OR);
		}

		if (e == null)
			return null;

		if (accept(Token.OR))
			return parseBoolExprOr(e);
		else
			return e;
	}

	// BoolExprAnd ::= <BoolExprAnd> '&&' <BoolEq>
	// 	         | <BoolEq>
	public Expression
	parseBoolExprAnd(final Expression lhs)
	{
		Expression e = parseBoolEq(null, -1);

		if (lhs != null) {
			if (e == null)
				fail("&& without rhs");

			e = new AND_AND_Expression(lhs, e, AND_AND);
		}

		if (e == null)
			return null;

		if (accept(Token.AND))
			return parseBoolExprAnd(e);
		else
			return e;
	}

	// BoolEq ::= <BoolEq> <BinEqRel> <BoolRel>
	//          | <BoolRel>           
	public Expression
	parseBoolEq(Expression lhs, int op)
	{
		Expression e = parseBoolRel(null, -1);

		if (lhs != null) {
			if (e == null)
				fail("equality operator without rhs");

			e = new EqualExpression(lhs, e, op);
		}

		if (e == null)
			return null;

		int new_operator;
		if (-1 != (new_operator = acceptOneToken(eq_ops)))
			return parseBoolEq(e, new_operator);
		else
			return e;
	}


	// BoolRel ::= <BoolRel> <BinOpRel> <BoolGround>
	//           | <BoolRel> <instanceof> <Type>
	//           | <BoolGround>
	public Expression
	parseBoolRel(Expression lhs, int op)
	{
		Expression e = parseBoolGround();

		if (lhs != null) {
			if (e == null)
				fail("comparison operator without rhs");

			e = new BinaryExpression(lhs, e, op);
		}

		if (e == null)
			return null;

		int new_operator;
		if (-1 != (new_operator = acceptOneToken(linear_order_binops)))
			return parseBoolRel(e, new_operator);
		else if (accept(Token.INSTANCEOF)) {
			final TypeReference ty = parseType();
			return new InstanceOfExpression(e, ty);
		} else
			return e;
	}


	// BoolGround ::= '(' <BoolExpr> ')'
	//            | '!' <BoolGround>
	//            | 'reach' '(' <ReachSpecSeq> ')' '[' <QName> ']'
	//            | <Expr>
	public Expression
	parseBoolGround()
	{
		if (accept(Token.OPAREN)) {
			Expression e = parseBoolExpr(null);
			expect(Token.CPAREN, ")");
			return e;
		}

		if (accept(Token.NOT))
			return new UnaryExpression(expectExpression(parseBoolGround()), NOT);

		if (accept(Token.REACH)) {
			expect(Token.OBRACKET, "[");
			ReachSpec rs = parseReachSpecSeq();
			if (rs == null)
				fail("Expected `reach' path specification");
			expect(Token.CBRACKET, "]");
			expect(Token.OPAREN, "(");
			SingleNameReference name = expectSingleNameReference();
			expect(Token.CPAREN, ")");
			return new ReachExpression(name, rs);
		}

		return parseExpr();
	}


	// ReachSpecSeq ::= <ReachSpec>
	// 	          | <ReachSpecSeq> ',' <ReachSpec>
	public ReachSpec
	parseReachSpecSeq()
	{
		final LinkedList l = parseReachSpecSequence(new LinkedList());
		if (l == null)
			return null;

		ReachSpec.Traversal[] traversals = new ReachSpec.Traversal[l.size()];
		for (int i = 0; i < l.size(); i++)
			traversals[i] = (ReachSpec.Traversal)l.get(i); // yes, slow

		return new ReachSpec(traversals);
	}

	public LinkedList
	parseReachSpecSequence(LinkedList entries)
	{
		final ReachSpec.Traversal t = parseReachSpec();
		if (t == null)
			return null;

		entries.addLast(t);

		// Yes, I'm a functional programmer.  Yes, this is slow in Java.  But this is research code, so deal with it.
		// If I wanted fast code, I wouldn't be writing a recursive descent parser by hand, now would I?
		if (accept(Token.COMMA))
			return parseReachSpecSequence(entries);
		else
			return entries;
	}

	// ReachSpec ::= <ReachSource>
	// 	    | <ReachSource> '/' <NodeSet>
	public ReachSpec.Traversal
	parseReachSpec()
	{
		NameReference sources[] = parseReachSource();
		NameReference ignores[] = null;

		if (accept(Token.SLASH))
			ignores = parseNodeSet();

		return new ReachSpec.Traversal(sources, ignores);
	}

	// ReachSource ::= (* empty *)
	// 	      | <NodeSet>
	public NameReference[]
	parseReachSource()
	{
		if (peek() != null && peek().kind == Token.SLASH)
			return new NameReference[0];
		return parseNodeSet();
	}

	// NodeSet ::= '{' <NameSet> '}'
	//           | <Name>
	public NameReference[]
	parseNodeSet()
	{
		final NameReference singleton = parseQName();
		if (singleton == null) {
			expect(Token.OBRACE, "{");
			final LinkedList ll = parseNameSet(new LinkedList());
			final NameReference[] retval = new NameReference[ll.size()];
			for (int i = 0; i < ll.size(); i++)
				retval[i] = (NameReference)ll.get(i);
			expect(Token.CBRACE, "}");
			return retval;
		}
		else return new NameReference[] { singleton };
	}


	// NameSet ::= <QName>
	// 	  | <NameSet> ',' <QName>
	public LinkedList
	parseNameSet(LinkedList elements)
	{
		elements.addLast(parseQName());
		if (accept(Token.COMMA))
			return parseNameSet(elements);
		else
			return elements;
	}

	public NameReference
	parseQName()
	{
		String s = acceptID();

		if (s == null)
			return null;

		if (accept(Token.PERIOD)) {
			LinkedList components = new LinkedList();

			components.addLast(s);

			do {
				components.addLast(expectID());
			} while (accept(Token.PERIOD));

			Object[] onames = components.toArray();
			char[][] names = new char[onames.length][];
			long[] pos = new long[onames.length];

			for (int i = 0; i < onames.length; i++) {
				names[i] = ((String)onames[i]).toCharArray();
				pos[i] = combo_pos();
			}

			return new QualifiedNameReference(names, pos, pos(), pos());

		} else
			return new SingleNameReference(s.toCharArray(), pos());
	}

	// Expr ::= <Name>
	//        | <Expr> '.' <Name>
	//        | <int>
	//        | 'null'
	//        | 'true'
	//        | 'false'
	//        | '(' <Expr> ')'
	public Expression
	parseExpr()
	{
		if (accept(Token.OPAREN)) {
			final Expression e = parseExpr();
			expect(Token.CPAREN, ")");
			return e;
		}

		if (accept(Token.NULL))
			return new NullLiteral(pos(), pos());
		if (accept(Token.TRUE))
			return new TrueLiteral(pos(), pos());
		if (accept(Token.FALSE))
			return new FalseLiteral(pos(), pos());
		final Expression num_expr = acceptNumber();
		if (num_expr != null)
			return num_expr;
		return parseQName();
	}
}