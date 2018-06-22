import org.vmmagic.pragma.SkipBytecodeVerifier;

public class Test
{
	
	@SkipBytecodeVerifier
	public static void main(String[] _)
	{
		C.f();
	}
}