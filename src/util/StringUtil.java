package util;

import java.util.ArrayList;
import java.util.List;

public final class StringUtil 
{
	// Returns the first occurence of such String sandwiched by the provided 
	// fore & back String in the content. if no such String exists, null is returned.
	public static String getStringBetween(String content, String fore, String back)
	{
		int startIndex = content.indexOf(fore) + fore.length();
		int endIndex   = content.indexOf(back, startIndex);
		
		try
		{
			if(startIndex == endIndex) return null;
			
			return content.substring(startIndex, endIndex);
		}
		catch(IndexOutOfBoundsException exception)
		{
			return null;
		}
	}
	
	// Same as getStringBetween, except start searching for maching result
	// from behind.
	public static String getLastStringBetween(String content, String fore, String back)
	{
		int endIndex   = content.lastIndexOf(back);
		int startIndex = content.lastIndexOf(fore) + fore.length();
		
		try
		{
			return content.substring(startIndex, endIndex);
		}
		catch(IndexOutOfBoundsException exception)
		{
			return null;
		}
	}
	
	// Same as getStringBetween, returns an int instead.
	// If no number found, a zero is returned.
	public static int getIntBetween(String content, String fore, String back)
	{
		int startIndex = content.indexOf(fore) + fore.length();
		if(startIndex == -1) return 0;
		
		int endIndex = content.indexOf(back, startIndex);
		if(endIndex == -1) return 0;
		
		try
		{
//			Debug.print("fore: " + fore + ", back: " + back);
			return Integer.valueOf(content.substring(startIndex, endIndex).trim());
		}
		catch(IndexOutOfBoundsException exception)
		{
			Debug.print("StringUtil.getIntBetween: IndexOutOfBoundsException");
			return 0;
		}
		catch(NumberFormatException exception)
		{
			Debug.print("StringUtil.getIntBetween: NumberFormatException");
			return 0;
		}
	}
	
	// Same as getStringBetween, returns a float instead.
	// If no number found, a zero is returned.
	public static float getFloatBetween(String content, String fore, String back)
	{
		int startIndex = content.indexOf(fore) + fore.length();
		int endIndex   = content.indexOf(back, startIndex);
		
		try
		{
			return Float.valueOf(content.substring(startIndex, endIndex).trim());
		}
		catch(IndexOutOfBoundsException exception)
		{
			return 0;
		}
		catch(NumberFormatException exception)
		{
			return 0;
		}
	}
	
	// Return the remaining contents with specified portion deleted.
	// TODO: exception handling
	public static String deletePortion(String content, int startIndex, int endIndex)
	{
		return content.substring(0, startIndex) + content.substring(endIndex, content.length());
	}
	
	public static String[] removeEmptyStrings(String[] data)
	{
		List<String> result = new ArrayList<>();
		
		for(int i = 0; i < data.length; i++)
		{
			if(!data[i].equals(""))
			{
				result.add(data[i]);
			}
		}
		
		String[] res = new String[result.size()];
		result.toArray(res);
		
		return res;
	}
	
	public static String[] removeEmptyStringsAndTrim(String[] data)
	{
		List<String> result = new ArrayList<>();
		
		for(int i = 0; i < data.length; i++)
		{
			if(!data[i].equals(""))
			{
				result.add(data[i].trim());
			}
		}
		
		String[] res = new String[result.size()];
		result.toArray(res);
		
		return res;
	}
	
	public static String[] getDataSegments(String line)
	{
		String[] dataSegmants = line.split(" ");
		
		return removeEmptyStringsAndTrim(dataSegmants);
	}
}
