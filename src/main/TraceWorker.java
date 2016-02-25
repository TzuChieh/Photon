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

import scene.Scene;

public class TraceWorker implements Runnable
{
	private Scene m_scene;
	private Camera m_camera;
	private SampleManager m_sampleManager;
	
	private PathTracer m_pathTracer;
	private Frame m_sampleResult;
	
	public TraceWorker(Scene scene, Camera camera, SampleManager sampleManager, int xRes, int yRes)
	{
		m_scene = scene;
		m_camera = camera;
		m_sampleManager = sampleManager;
		
		m_pathTracer = new PathTracer();
		m_sampleResult = new Frame(xRes, yRes);
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			m_pathTracer.trace(m_scene, m_camera, m_sampleResult);
			m_sampleManager.addSample(m_sampleResult);
		}
	}
}
