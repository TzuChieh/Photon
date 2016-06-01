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

package ui.panel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import core.Frame;
import math.Vector3f;
import util.Color;
import util.Func;

@SuppressWarnings("serial")
public class Display extends JPanel
{
	private BufferedImage m_bufferedImage;
	
	public Display(int widthPx, int heightPx)
	{
		super();
		
		this.setPreferredSize(new Dimension(widthPx, heightPx));
		
		m_bufferedImage = new BufferedImage(widthPx, heightPx, BufferedImage.TYPE_INT_RGB);
	}
	
	// safe to call from any thread since repaint() uses EDT internally
	public void render(Frame frame)
	{
		synchronized(m_bufferedImage)
		{
			Vector3f color = new Vector3f();
			
			for(int x = 0; x < frame.getWidthPx(); x++)
			{
				for(int y = 0; y < frame.getHeightPx(); y++)
				{
					int inversedY = frame.getHeightPx() - y - 1;
					
//					float r = Func.clamp(frame.getPixelR(x, y), 0.0f, 1.0f);
//					float g = Func.clamp(frame.getPixelG(x, y), 0.0f, 1.0f);
//					float b = Func.clamp(frame.getPixelB(x, y), 0.0f, 1.0f);
					
					float r = frame.getPixelR(x, y);
					float g = frame.getPixelG(x, y);
					float b = frame.getPixelB(x, y);
					
					color.set(r, g, b);
					
					// Reinhard tone mapping
//					r = r / (1.0f + r);
//					g = g / (1.0f + g);
//					b = b / (1.0f + b);
//					
//					r = (float)Math.pow(r, 1.0 / 2.2);
//					g = (float)Math.pow(g, 1.0 / 2.2);
//					b = (float)Math.pow(b, 1.0 / 2.2);
					
					// Jim Hejl and Richard Burgess-Dawson (GDC)
					// no need of pow(1/2.2)
					color.subLocal(0.004f).clampLocal(0.0f, Float.MAX_VALUE);
					Vector3f numerator = color.mul(6.2f).addLocal(0.5f).mulLocal(color);
					Vector3f denominator = color.mul(6.2f).addLocal(1.7f).mulLocal(color).addLocal(0.06f);
					r = numerator.x / denominator.x;
					g = numerator.y / denominator.y;
					b = numerator.z / denominator.z;
					
					if(r != r || g != g || b != b)
					{
						System.out.println("NaN!");
						System.exit(1);
					}
					
					m_bufferedImage.setRGB(x, inversedY, Color.toARGBInt(0.0f, r, g, b));
				}
			}
		}
		
		this.repaint();
	}
	
	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		
		synchronized(m_bufferedImage)
		{
			graphics.drawImage(m_bufferedImage, 0, 0, null);
		}
	}
}
