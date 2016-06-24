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

import image.sampler.Sampler;
import math.Vector2f;
import math.Vector3f;
import util.Logger;

public class Texture
{
	private static final Logger LOGGER = new Logger("Texture");
	
	private ImageResource m_imageResource;
	private Sampler       m_sampler;
	
	public Texture(String fullFilename, Sampler sampler)
	{
		m_sampler = sampler;
		
		try
		{
			m_imageResource = ImageManager.getImageLoader().load(fullFilename);
		}
		catch(ImageLoadingException e)
		{
			LOGGER.printWrn(e.getMessage());
			LOGGER.printWrn("due to some problem, an EmptyImageResource is being used");
		}
		finally
		{
			m_imageResource = new EmptyImageResource();
		}
	}
	
	public void sample(Vector2f texCoord, Vector3f result)
	{
		m_sampler.sample(this, texCoord, result);
	}
	
	public void getPixel(int x, int y, Vector3f result)
	{
		m_imageResource.getPixel(x, y, result);
	}
	
	public int getWidthPx()
	{
		return m_imageResource.getDimensions()[0];
	}
	
	public int getHeightPx()
	{
		return m_imageResource.getDimensions()[1];
	}
}
