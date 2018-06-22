package org.eclipse.jdt.internal.compiler.gca;

import org.eclipse.jdt.internal.compiler.ast.OperatorIds;

public class Token
{
	public static final int ID		= 1000;
	public static final int INT		= 1001;
	public static final int OR		= 1002;
	public static final int AND		= 1003;
	public static final int IMPLIES		= 1004;
	public static final int NOT		= 1005;
	public static final int PERIOD		= 1006;
	public static final int FORALL		= 1007;
	public static final int EXISTS		= 1008;
	public static final int REACH		= 1009;
	public static final int OPAREN		= 1010;
	public static final int CPAREN		= 1011;
	public static final int OBRACKET	= 1012;
	public static final int CBRACKET	= 1013;
	public static final int OBRACE		= 1014;
	public static final int CBRACE		= 1015;
	public static final int COMMA		= 1016;
	public static final int NULL		= 1017;
	public static final int EQ		= OperatorIds.EQUAL_EQUAL;
	public static final int NE		= OperatorIds.NOT_EQUAL;
	public static final int LT		= OperatorIds.LESS;
	public static final int GT		= OperatorIds.GREATER;
	public static final int LE		= OperatorIds.LESS_EQUAL;
	public static final int GE		= OperatorIds.GREATER_EQUAL;
	public static final int TRUE		= 1024;
	public static final int FALSE		= 1025;
	public static final int COLON		= 1026;
	public static final int INSTANCEOF	= OperatorIds.INSTANCEOF;
	public static final int SLASH		= 1028;
	public static final int IFF		= 1029;


	public int kind;
	public int start, end;
	public String description;

	public Token(final int _kind, final String _description, final int _start, final int _end)
	{
		this.kind = _kind;
		this.description = _description;
		this.start = _start;
		this.end = _end;
	}

	public String
	toString()
	{
		return this.description;
	}

	public static class IntToken extends Token
	{
		public int value;

		public IntToken(final int _value, final int _start, final int _end)
		{
			super(INT, "" + _value, _start, _end);
			this.value = _value;
		}
	}
}