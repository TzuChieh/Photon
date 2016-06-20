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

package image.intrinsic;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import image.EmptyImageResource;
import image.ImageLoader;
import image.ImageLoadingException;
import image.ImageResource;
import image.ImageSavingException;
import util.IOUtil;

public class IntrinsicImageLoader implements ImageLoader
{
	private static final Set<String> SUPPORTED_IMAGE_TYPES = new HashSet<>();
	
	static
	{
		SUPPORTED_IMAGE_TYPES.add("png");
	}
	
	public IntrinsicImageLoader()
	{
		
	}
	
	@Override
	public ImageResource load(String fullFilename) throws ImageLoadingException
	{
		String imageType = IOUtil.getFilenameExtension(fullFilename);
		imageType.toLowerCase(Locale.ROOT);
		
		if(SUPPORTED_IMAGE_TYPES.contains(imageType))
		{
			// TODO load image
			
			return null;
		}
		else
		{
			return new EmptyImageResource();
		}
	}

	@Override
	public void save(String fullFilename) throws ImageSavingException
	{
		// TODO Auto-generated method stub
		
	}
}
