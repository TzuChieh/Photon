//	The MIT License (MIT)
//	
//	Copyright (c) 2016 Tzu-Chieh Chang (as known as D01phiN)
//	
//	Permission is hereby granted, free of charge, to any person obtaining a copy
//	of this software and associated documentation files (the "Software"), to deal
//	in the Software without restriction, including without limitation the rights
//	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//	copies of the Software, and to permit persons to whom the Software is
//	furnished to do so, subject to the following conditions:
//	
//	The above copyright notice and this permission notice shall be included in all
//	copies or substantial portions of the Software.
//	
//	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//	SOFTWARE.

package util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public final class IOUtil 
{
	public static String loadTextFile(String fullFileName)
	{
		return loadTextFile(fullFileName, "\n");
	}
	
	public static String loadTextFile(String fullFileName, String replacementForNextLineChar)
	{
		try 
		{
			BufferedReader fileReader         = new BufferedReader(new FileReader(fullFileName));
			StringBuilder  fileContentBuilder = new StringBuilder();
			
			String currentLine;
			
			while((currentLine = fileReader.readLine()) != null)
			{
				// any '\n' or '\r' will be replaced by replacementForNextLineChar instead
				fileContentBuilder.append(currentLine + replacementForNextLineChar);
			}
			
			fileReader.close();
			
			return fileContentBuilder.toString();
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
			Debug.printErr("IOUtil Error: file " + fullFileName + " loading failed");
			System.exit(1);
		}

		return null;
	}// end loadTextFile
	
	/**
	 * Retrieve the filename extension (without the dot) from a filename (path can be included).
	 * @return Filename extension without the dot sign. If there's no filename extension found, 
	 * an empty String is returned.
	 */
	public static String getFilenameExtension(String fullFilename)
	{
		int lastDotIndex = fullFilename.lastIndexOf(".");
		
		if(lastDotIndex != -1)
		{
			return fullFilename.substring(lastDotIndex + 1, fullFilename.length());
		}
		else
		{
			return "";
		}
	}
	
	public static String getFullFilenameWithoutExtension(String fullFilename)
	{
		return fullFilename.substring(0, fullFilename.lastIndexOf("."));
	}
	
	public static String getFileDirectory(String fullFilename)
	{
		return fullFilename.substring(0, fullFilename.lastIndexOf("/") + 1);
	}
	
	public static void writeTextFile(String fullFileName, String contents)
	{
		try 
		{
			FileWriter     fileWriter   = new FileWriter(fullFileName);
			BufferedWriter outputWriter = new BufferedWriter(fileWriter);
			
			outputWriter.write(contents);
			
			// Without closing, this will be an empty file!
			outputWriter.close();
		} 
		catch(IOException e) 
		{
			e.printStackTrace();
			Debug.printErr("File writing failed: " + fullFileName);
			System.exit(1);
		}
	}// end writeTextFile
	
	public static BufferedImage loadImageFile(String fullFileName)
	{
		try 
		{
			BufferedImage image = ImageIO.read(new File(fullFileName));
			
			return image;
		} 
		catch (IOException e) 
		{
			Debug.printErr("IOUtil Error: something went wrong while loading " + fullFileName);
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static List<String> listFileNames(String directory)
	{
		List<String> fileNames = new ArrayList<>();
		File[]       files     = new File(directory).listFiles();
		
		for(File file : files)
		{
			if(file.isFile())
			{
				fileNames.add(file.getName());
			}
		}
		
		return fileNames;
	}
	
	public static List<String> listFolderNames(String directory)
	{
		List<String> folderNames = new ArrayList<>();
		File[]       files       = new File(directory).listFiles();
		
		for(File file : files)
		{
			if(file.isDirectory())
			{
				folderNames.add(file.getName());
			}
		}
		
		return folderNames;
	}
}
