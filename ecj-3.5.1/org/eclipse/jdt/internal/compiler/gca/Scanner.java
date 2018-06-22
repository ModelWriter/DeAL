package org.eclipse.jdt.internal.compiler.gca;

import java.util.*;

/**
 * Writing this made me realise just how horrible writing scanners in Java is-- in all its
 * libraries, there is NO sensible support for that task.
 */
public class Scanner
{
	private char [] input;
	private final int offset;
	private int relative_offset;

	private LinkedList pushed_back = new LinkedList();

	public int
	offset()
	{
		return offset + relative_offset;
	}

	public
	Scanner(final String _body, final int _offset)
	{
		this.offset = _offset;
		this.input = _body.toCharArray();
		this.relative_offset = 0;
	}

	public void
	pushBack(Token t)
	{
		if (t != null)
			this.pushed_back.addFirst(t);
	}

	private final Token
	trySimpleToken(final int id, final String s)
	{
		final char[] chars = s.toCharArray(); // Yes, this is terrible.  But this is research code, so I don't care.

		if (chars.length > this.input.length - this.relative_offset)
			return null;

		for (int i = 0; i < chars.length; i++)
			if (chars[i] != this.input[i + this.relative_offset])
				return null;

		return new Token(id, s,
				 this.offset + this.relative_offset,
				 this.offset + (this.relative_offset += chars.length));
	}

	private final Token
	tryID()
	{
		int offset = this.relative_offset;

		if (!Character.isJavaIdentifierStart(this.input[offset++]))
			return null;

		while (offset < this.input.length
		       && Character.isJavaIdentifierPart(this.input[offset]))
			++offset;

		return new Token(Token.ID, new String(this.input, this.relative_offset, offset - this.relative_offset),
				 this.offset + this.relative_offset,
				 this.offset + (this.relative_offset = offset));
	}

	private final Token
	tryInt()
	{
		int offset = this.relative_offset;

		if (!Character.isDigit(this.input[offset++]))
			return null;

		while (offset < this.input.length && Character.isDigit(this.input[offset]))
			++offset;

		return new Token.IntToken(Integer.parseInt(new String(this.input, this.relative_offset, offset - this.relative_offset)),
					  this.offset + this.relative_offset,
					  this.offset + (this.relative_offset = offset));
	}

	public Token
	next()
	{
		if (!this.pushed_back.isEmpty())
			return (Token)this.pushed_back.removeFirst();

		if (this.input.length <= this.relative_offset)
			return null;

		while (this.input.length > this.relative_offset
		       && Character.isWhitespace(this.input[this.relative_offset]))
			++this.relative_offset;

		if (this.input.length <= this.relative_offset)
			return null;

		Token t;

		if (null != (t = trySimpleToken(Token.OR, "||"))) return t;
		if (null != (t = trySimpleToken(Token.AND, "&&"))) return t;
		if (null != (t = trySimpleToken(Token.IFF, "<->"))) return t;
		if (null != (t = trySimpleToken(Token.IMPLIES, "->"))) return t;
		if (null != (t = trySimpleToken(Token.PERIOD, "."))) return t;
		if (null != (t = trySimpleToken(Token.FORALL, "forall"))) return t;
		if (null != (t = trySimpleToken(Token.EXISTS, "exists"))) return t;
		if (null != (t = trySimpleToken(Token.REACH, "reach"))) return t;
		if (null != (t = trySimpleToken(Token.INSTANCEOF, "instanceof"))) return t;
		if (null != (t = trySimpleToken(Token.OPAREN, "("))) return t;
		if (null != (t = trySimpleToken(Token.CPAREN, ")"))) return t;
		if (null != (t = trySimpleToken(Token.OBRACKET, "["))) return t;
		if (null != (t = trySimpleToken(Token.CBRACKET, "]"))) return t;
		if (null != (t = trySimpleToken(Token.OBRACE, "{"))) return t;
		if (null != (t = trySimpleToken(Token.CBRACE, "}"))) return t;
		if (null != (t = trySimpleToken(Token.COMMA, ","))) return t;
		if (null != (t = trySimpleToken(Token.NULL, "null"))) return t;
		if (null != (t = trySimpleToken(Token.EQ, "=="))) return t;
		if (null != (t = trySimpleToken(Token.NE, "!="))) return t;
		if (null != (t = trySimpleToken(Token.LE, "<="))) return t;
		if (null != (t = trySimpleToken(Token.GE, ">="))) return t;
		if (null != (t = trySimpleToken(Token.LT, "<"))) return t;
		if (null != (t = trySimpleToken(Token.GT, ">"))) return t;
		if (null != (t = trySimpleToken(Token.TRUE, "true"))) return t;
		if (null != (t = trySimpleToken(Token.FALSE, "false"))) return t;
		if (null != (t = trySimpleToken(Token.NOT, "!"))) return t;
		if (null != (t = trySimpleToken(Token.COLON, ":"))) return t;
		if (null != (t = trySimpleToken(Token.SLASH, "/"))) return t;
		if (null != (t = tryID())) return t;
		if (null != (t = tryInt())) return t;

		System.err.println("Failed to process: `" + (new String(this.input, this.relative_offset, 30 + this.relative_offset > this.input.length ? this.input.length - this.relative_offset : 30)) + "'");
		throw new RuntimeException("GCAssertions: Tokeniser failed");
	}
}
