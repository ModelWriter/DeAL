package org.eclipse.jdt.internal.compiler.ast;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;

/**
 * Reachability spec.  Captures e.g. `{a, b} / {c}, d
 */
public class ReachSpec
{
	public Traversal[] traversals;

	public
	ReachSpec(final Traversal[] _traversals)
	{
		this.traversals = _traversals;
	}

	public boolean
	isDominator()
	{
		return (traversals.length > 0
			&& traversals[0].sources.length == 0);
	}

	public void
	resolveTypes(final BlockScope scope)
	{
		final TypeBinding tb = scope.getJavaLangObject();
		for (int i = 0; i < traversals.length; i++)
			traversals[i].resolveTypes(scope, tb);
	}

	/**
	 * asserts that the parameter binding does not occur in this spec
	 */
	public void
	assertNotVariable(final VariableBinding vb, final ProblemReporter problem_reporter, final ASTNode node)
	{
		for (int i = 0; i < traversals.length; i++)
			traversals[i].assertNotVariable(vb, problem_reporter, node);
	}

	public int
	hashCode()
	{
		int v = 0;
		for (int i = 0; i < this.traversals.length; i++)
			v = (v << 1) + this.traversals[i].hashCode();
		return v;
	}

	public boolean
	equals(final Object o)
	{
		if (!(o instanceof ReachSpec))
			return false;
		final ReachSpec other = (ReachSpec) o;

		if (other.traversals.length != this.traversals.length)
			return false;

		for (int i = 0; i < this.traversals.length; i++)
			if (!this.traversals[i].equals(other.traversals[i]))
				return false;

		return true;
	}

	public String
	toString()
	{
		String s = "";
		for (int i = 0; i < this.traversals.length; i++) {
			s += this.traversals[i].toString();
			if (i + 1 < this.traversals.length)
				s += ", ";
		}
		return s;
	}

	public static class
	Traversal
	{
		public NameReference[] sources;
		public NameReference[] ignores;

		static NameReference[]
		make_sorted_array(final NameReference [] _src)
		{
			if (_src == null)
				return new NameReference[0];

			java.util.Arrays.sort(_src);
			return _src;
		}

		static boolean
		compare_arrays(final NameReference [] a,
			       final NameReference [] b)
		{
			if (a.length != b.length)
				return false;

			for (int i = 0; i < a.length; i++)
				if (a[i].compareTo(b[i]) != 0)
					return false;
			return true;
		}

		static String
		array_to_string(final NameReference [] a)
		{
			if (a.length == 0)
				return "";
			if (a.length == 1)
				return a[0].toString();

			String s = "{";
			for (int i = 0; i < a.length; i++) {
				s += a[i].toString();
				if (i + 1 < a.length)
					s += ", ";
			}

			return s + "}";
		}

		public
		Traversal(final NameReference[] _sources, final NameReference[] _ignores)
		{
			this.sources = make_sorted_array(_sources);
			this.ignores = make_sorted_array(_ignores);
		}

		public int
		hashCode()
		{
			int v = 0;
			for (int i = 0; i < this.sources.length; i++)
				v += (this.sources[i].binding.hashCode() * 7);
			for (int i = 0; i < this.ignores.length; i++)
				v += (this.ignores[i].binding.hashCode() * 2);
			return v;
		}

		public boolean
		equals(Object o)
		{
			if (!(o instanceof Traversal))
				return false;

			final Traversal other = (Traversal) o;

			if (!compare_arrays(this.sources, other.sources))
				return false;
			if (!compare_arrays(this.ignores, other.ignores))
				return false;

			return true;
		}

		public String
		toString()
		{
			String sep = "";

			if (sources.length == 0 || ignores.length != 0)
				sep = "/";

			return array_to_string(sources) + sep + array_to_string(ignores);
		}

		public void
		resolveTypes(final BlockScope scope, final TypeBinding tb)
		{
			for (int i = 0; i < this.sources.length; i++)
				this.sources[i].resolveTypeExpecting(scope, tb);
			for (int i = 0; i < this.ignores.length; i++)
				this.ignores[i].resolveTypeExpecting(scope, tb);
		}

		public void
		assertNotVariable(final VariableBinding vb, final ProblemReporter problem_reporter, final ASTNode node)
		{
			for (int i = 0; i < this.sources.length; i++)
				if (this.sources[i].binding == vb)
					problem_reporter.abortDueToInternalError("Quantified variable `"+this.sources[i]+"' in reachability test source set", node);
			for (int i = 0; i < this.ignores.length; i++)
				if (this.ignores[i].binding == vb)
					problem_reporter.abortDueToInternalError("Quantified variable `"+this.ignores[i]+"' in reachability test ignore set", node);
		}
	}
}
