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

package core;

import main.Camera;
import main.Frame;
import main.PathTracer;
import main.Ray;
import main.SampleManager;
import main.TraceWorker;
import math.material.AbradedOpaque;
import model.RawModel;
import model.primitive.Sphere;
import scene.ClassicMaterialScene;
import scene.FiveBallsScene;
import scene.Scene;
import ui.Window;
import util.Time;
import util.Util;

public class Engine
{
//	private static final int FRAME_WIDTH_PX  = 1366;
//	private static final int FRAME_HEIGHT_PX = 768;
	
//	private static final int FRAME_WIDTH_PX  = 800;
//	private static final int FRAME_HEIGHT_PX = 500;
	
	private static final int FRAME_WIDTH_PX  = 1280;
	private static final int FRAME_HEIGHT_PX = 800;
	
	private PathTracer tracer;
	private Window window;
	private Frame frame;
	private Scene scene;
	
	private SampleManager sampleManager;
	
	public Engine()
	{
		window = new Window(FRAME_WIDTH_PX, FRAME_HEIGHT_PX);
		frame = new Frame(FRAME_WIDTH_PX, FRAME_HEIGHT_PX);
		
		tracer = new PathTracer();
		
		sampleManager = new SampleManager(FRAME_WIDTH_PX, FRAME_HEIGHT_PX);
		
		
//		scene = new FiveBallsScene();
		scene = new ClassicMaterialScene();
		
		
		Runnable tracer1 = new TraceWorker(scene, sampleManager, FRAME_WIDTH_PX, FRAME_HEIGHT_PX);
		Thread tread1 = new Thread(tracer1);
		tread1.start();
		
		Util.threadSleep(100);
		
		Runnable tracer2 = new TraceWorker(scene, sampleManager, FRAME_WIDTH_PX, FRAME_HEIGHT_PX);
		Thread tread2 = new Thread(tracer2);
		tread2.start();
		
		Util.threadSleep(100);
		
		Runnable tracer3 = new TraceWorker(scene, sampleManager, FRAME_WIDTH_PX, FRAME_HEIGHT_PX);
		Thread tread3 = new Thread(tracer3);
		tread3.start();
		
		Util.threadSleep(100);
		
		Runnable tracer4 = new TraceWorker(scene, sampleManager, FRAME_WIDTH_PX, FRAME_HEIGHT_PX);
		Thread tread4 = new Thread(tracer4);
		tread4.start();
		
		Util.threadSleep(100);
		
//		Runnable tracer5 = new TraceWorker(scene, sampleManager, FRAME_WIDTH_PX, FRAME_HEIGHT_PX);
//		Thread tread5 = new Thread(tracer5);
//		tread5.start();
//		
//		Util.threadSleep(100);
	}
	
	public void run()
	{
		System.out.println("start tracing");
		
		
		double startTimeMs = Time.getMilliTime();
		
		int numSamples = 0;
		
//		while(numSamples < 20)
		while(numSamples < Integer.MAX_VALUE)
		{
//			tracer.trace(scene, camera, frame);
//			sampleManager.addSample(frame);
			
			numSamples = sampleManager.getCombinedSample(frame);
			
			window.render(frame);
			
			
			System.out.println("number of samples: " + numSamples);
			
			Util.threadSleep(500);
		}
		
		double endTimeMs = Time.getMilliTime();
		
		
		System.out.println("time elapsed: " + (endTimeMs - startTimeMs) + " ms");
	}
}
