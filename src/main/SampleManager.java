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

package main;

import math.Vector3f;

public class SampleManager
{
	private Frame m_frame;
	
	private int m_numSamples;
	
	private Object m_lock;
	
	public SampleManager(int resX, int resY)
	{
		m_frame = new Frame(resX, resY);
		
		m_numSamples = 0;
		
		m_lock = new Object();
	}
	
	public int getCombinedSample(Frame result)
	{
		synchronized(m_lock)
		{
			result.set(m_frame);
			
			return m_numSamples;
		}
	}
	
	public void addSample(Frame frame)
	{
		synchronized(m_lock)
		{
			Vector3f processedRGB = new Vector3f();
			
			float sampleFactorS = 1.0f / (float)(m_numSamples + 1);
			float sampleFactorD = (float)m_numSamples / (float)(m_numSamples + 1);
			
			for(int x = 0; x < frame.getWidthPx(); x++)
			{
				for(int y = 0; y < frame.getHeightPx(); y++)
				{
					processedRGB.x = m_frame.getPixelR(x, y) * sampleFactorD + frame.getPixelR(x, y) * sampleFactorS;
					processedRGB.y = m_frame.getPixelG(x, y) * sampleFactorD + frame.getPixelG(x, y) * sampleFactorS;
					processedRGB.z = m_frame.getPixelB(x, y) * sampleFactorD + frame.getPixelB(x, y) * sampleFactorS;
					
					m_frame.setPixelRgb(x, y, processedRGB.x, processedRGB.y, processedRGB.z);
				}
			}
			
			m_numSamples++;
		}
	}
}
