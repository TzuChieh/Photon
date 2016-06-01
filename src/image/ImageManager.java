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

package image;

import core.Engine;
import util.Logger;

public final class ImageManager
{
	private static ImageLoader imageLoader = null;
	private static final Logger logger = new Logger(ImageManager.class.getSimpleName());
	
	public static boolean init(Engine engine)
	{
		
		
		return false;
//		if(imageLoader != null)
//		{
//			ImageManager.imageLoader = imageLoader;
//			return true;
//		}
//		else
//		{
//			logger.printErr("please specify a valid ImageLoader, the provided one is null");
//			return false;
//		}
	}
	
	public static ImageLoader getImageLoader()
	{
		return imageLoader;
	}
	
	private ImageManager() {}
}
