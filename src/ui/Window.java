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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import core.Frame;
import ui.panel.CorePanel;
import ui.panel.Display;

public class Window
{
	public static final int COREPANEL_WIDTH  = 270;
	public static final int COREPANEL_HEIGHT = 600;
	
	private JFrame m_jframe;
	
	private Display m_display;
	
	public Window(int widthPx, int heightPx)
	{
		m_jframe = new JFrame("Photon ver. 0.1");
		m_jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_jframe.setBounds(0, 0, widthPx, heightPx);
		m_jframe.setLocationRelativeTo(null);
		m_jframe.setLayout(new BorderLayout());
		m_jframe.setResizable(false);
		m_jframe.setVisible(true);
		
		m_display = new Display(widthPx, heightPx);
		m_jframe.add(m_display, BorderLayout.CENTER);
		
		m_jframe.pack();
		m_jframe.revalidate();
		
//		CorePanel corePanel = new CorePanel();
//		m_jframe.add(corePanel, BorderLayout.WEST);
	}
	
	public void render(Frame frame)
	{
		m_display.render(frame);
	}
}
