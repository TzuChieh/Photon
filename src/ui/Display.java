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

package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import main.Frame;
import math.Vector3f;
import util.Color;
import util.Func;

public class Display extends JPanel
{
	private BufferedImage m_bufferedImage;
	
	public Display(int xPx, int yPx, int widthPx, int heightPx)
	{
		super();
		
		super.setBounds(xPx, yPx, widthPx, heightPx);
		
		m_bufferedImage = new BufferedImage(widthPx, heightPx, BufferedImage.TYPE_INT_RGB);
	}
	
	public void render(Frame frame)
	{
		for(int x = 0; x < frame.getWidthPx(); x++)
		{
			for(int y = 0; y < frame.getHeightPx(); y++)
			{
				int inversedY = frame.getHeightPx() - y - 1;
				
//				float r = Func.clamp(frame.getPixelR(x, y), 0.0f, 1.0f);
//				float g = Func.clamp(frame.getPixelG(x, y), 0.0f, 1.0f);
//				float b = Func.clamp(frame.getPixelB(x, y), 0.0f, 1.0f);
				
				// Reinhard tone mapping
				float r = frame.getPixelR(x, y) / (1.0f + frame.getPixelR(x, y));
				float g = frame.getPixelG(x, y) / (1.0f + frame.getPixelG(x, y));
				float b = frame.getPixelB(x, y) / (1.0f + frame.getPixelB(x, y));
				
				if(r != r || g != g || b != b)
				{
					System.out.println("NaN!");
				}
				
				m_bufferedImage.setRGB(x, inversedY, Color.toARGBInt(0.0f, r, g, b));
			}
		}
		
		this.repaint();
	}
	
	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		
		graphics.drawImage(m_bufferedImage, 0, 0, null);
	}
}
