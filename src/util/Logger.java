package util;

public class Logger
{
	private String m_name;
	
	public Logger(String name)
	{
		m_name = name;
		
		printMsg("logger started");
	}
	
	public void printMsg(Object object)
	{
		System.out.println("[" + m_name + "] " + object);
	}
	
	public void printWrn(Object object)
	{
		System.err.println("<WARNING> [" + m_name + "] " + object);
	}
	
	public void printErr(Object object)
	{
		System.err.println("<ERROR> [" + m_name + "] " + object);
	}
}
