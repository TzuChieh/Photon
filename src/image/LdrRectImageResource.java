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

import math.Vector3f;

public class LdrRectImageResource extends ImageResource
{
	private byte[] m_data;
	
	public LdrRectImageResource(int widthPx, int heightPx, int numComponents)
	{
		super(numComponents, widthPx, heightPx);
		
		m_data = new byte[widthPx * heightPx * numComponents];
	}

	@Override
	public void getPixel(int d1, int d2, Vector3f result)
	{
		int baseIndex = getNumComponents() * (d2 * getDimensions()[0] + d1);
		
		result.x = (float)(m_data[baseIndex + 0] & 0xFF) / 255.0f;
		result.y = (float)(m_data[baseIndex + 1] & 0xFF) / 255.0f;
		result.z = (float)(m_data[baseIndex + 2] & 0xFF) / 255.0f;
	}

	@Override
	public void setPixel(int d1, int d2, Vector3f pixel)
	{
		int baseIndex = getNumComponents() * (d2 * getDimensions()[0] + d1);
		
		m_data[baseIndex + 0] = (byte)(pixel.x * 255.0f + 0.5f);
		m_data[baseIndex + 1] = (byte)(pixel.y * 255.0f + 0.5f);
		m_data[baseIndex + 2] = (byte)(pixel.z * 255.0f + 0.5f);
	}
}
