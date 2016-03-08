package util;

public class Debug 
{
	public static void print(Object object)
	{
		System.out.println(object);
	}
	
	public static void printWrn(Object object)
	{
		System.out.println("<WARNING> " + object);
	}
	
	public static void printErr(Object object)
	{
		System.err.println("<ERROR> " + object);
	}
	
	public static void exit()
	{
		System.exit(0);
	}
	
	public static void exitErr()
	{
		System.exit(1);
	}
}
