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

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import javax.swing.SwingUtilities;

import math.Quaternion;
import math.Vector3f;
import math.material.AbradedOpaque;
import model.RawModel;
import model.primitive.Sphere;
import radiosity.RadiosityDatReader;
import scene.ClassicMaterialScene;
import scene.FiveBallsScene;
import scene.LamborghiniScene;
import scene.Scene;
import scene.SponzaScene;
import ui.Window;
import util.Debug;
import util.Logger;
import util.Time;
import util.Util;

public class Engine
{
	public static enum OsType {UNKNOWN, WINDOWS, LINUX};
	
	private OsType m_osType;
	
//	private static final int FRAME_WIDTH_PX  = 1366;
//	private static final int FRAME_HEIGHT_PX = 768;
	
//	private static final int FRAME_WIDTH_PX  = 800;
//	private static final int FRAME_HEIGHT_PX = 500;
	
	private static final int FRAME_WIDTH_PX  = 400;
	private static final int FRAME_HEIGHT_PX = 250;
	
//	private static final int FRAME_WIDTH_PX  = 1280;
//	private static final int FRAME_HEIGHT_PX = 800;
	
	private PathTracer tracer;
	private Window m_window;
	private Scene scene;
	
	private SampleManager sampleManager;
	
	private Logger m_logger;
	
	public Engine()
	{
		m_logger = new Logger("Photon Engine");
		
		initGui();
		init();
		
		tracer = new PathTracer();
		
		sampleManager = new SampleManager(FRAME_WIDTH_PX, FRAME_HEIGHT_PX);
		
//		scene = new FiveBallsScene();
//		scene = new ClassicMaterialScene();
//		scene = new LamborghiniScene();
//		scene = new SponzaScene();
		
		String filename = "./resource/radiosity/blocks.tri.7000.dat";
//		String filename = "./resource/radiosity/church.tri.2000.dat";
		RadiosityDatReader radReader = new RadiosityDatReader(filename);
		scene = radReader.parse();
		
		
		scene.cookScene();
		
		for(int i = 0; i < 4; i++)
		{
			Runnable tracer = new TraceWorker(scene, sampleManager, FRAME_WIDTH_PX, FRAME_HEIGHT_PX);
			Thread thread = new Thread(tracer);
			thread.start();
			
			Util.threadSleep(100);
		}
	}
	
	private void init()
	{
		m_logger.printMsg("initializing engine...");
		
		{
			m_osType = retrieveOsType();
			
			Input.init();
		}
		
		m_logger.printMsg("engine initialized successfully");
	}
	
	public void run()
	{
		final HdrFrame frameResult = new HdrFrame(FRAME_WIDTH_PX, FRAME_HEIGHT_PX);
		
		while(true)
		{
			synchronized(scene.getCamera())
			{
				Camera camera = scene.getCamera();
				boolean moved = false;
				
				if(Input.keyHold(Input.KEY_W))
				{
					camera.getPos().addLocal(camera.getDir().mul(0.1f));
					moved = true;
				}
				
				if(Input.keyHold(Input.KEY_S))
				{
					camera.getPos().addLocal(camera.getDir().mul(-0.1f));
					moved = true;
				}
				
				if(Input.keyHold(Input.KEY_A))
				{
					Vector3f cameraDir = camera.getDir();
					Vector3f rightDir = new Vector3f(-cameraDir.z, 0.0f, cameraDir.x).normalizeLocal();
					camera.getPos().addLocal(rightDir.mul(-0.1f));
					moved = true;
				}
				
				if(Input.keyHold(Input.KEY_D))
				{
					Vector3f cameraDir = camera.getDir();
					Vector3f rightDir = new Vector3f(-cameraDir.z, 0.0f, cameraDir.x).normalizeLocal();
					camera.getPos().addLocal(rightDir.mul(0.1f));
					moved = true;
				}
				
				if(Input.keyHold(Input.KEY_UP))
				{
					Vector3f cameraDir = camera.getDir();
					Vector3f rightDir = new Vector3f(-cameraDir.z, 0.0f, cameraDir.x).normalizeLocal();
					Vector3f upDir    = rightDir.cross(cameraDir).normalizeLocal();
					Quaternion rot = new Quaternion(rightDir, 0.05f);
					camera.getDir().set(cameraDir.rotate(rot));
					moved = true;
				}
				
				if(Input.keyHold(Input.KEY_DOWN))
				{
					Vector3f cameraDir = camera.getDir();
					Vector3f rightDir = new Vector3f(-cameraDir.z, 0.0f, cameraDir.x).normalizeLocal();
					Vector3f upDir    = rightDir.cross(cameraDir).normalizeLocal();
					Quaternion rot = new Quaternion(rightDir, -0.05f);
					camera.getDir().set(cameraDir.rotate(rot));
					moved = true;
				}
				
				if(Input.keyHold(Input.KEY_LEFT))
				{
					Vector3f cameraDir = camera.getDir();
					Vector3f rightDir = new Vector3f(-cameraDir.z, 0.0f, cameraDir.x).normalizeLocal();
					Vector3f upDir    = rightDir.cross(cameraDir).normalizeLocal();
					Quaternion rot = new Quaternion(upDir, 0.05f);
					camera.getDir().set(cameraDir.rotate(rot));
					moved = true;
				}
				
				if(Input.keyHold(Input.KEY_RIGHT))
				{
					Vector3f cameraDir = camera.getDir();
					Vector3f rightDir = new Vector3f(-cameraDir.z, 0.0f, cameraDir.x).normalizeLocal();
					Vector3f upDir    = rightDir.cross(cameraDir).normalizeLocal();
					Quaternion rot = new Quaternion(upDir, -0.05f);
					camera.getDir().set(cameraDir.rotate(rot));
					moved = true;
				}
				
				if(moved)
				{
					sampleManager.refresh();
				}
			}
			
			int numSamples = sampleManager.getCombinedSample(frameResult);
			
			m_window.render(frameResult);
			
			Debug.print("=============================================");
			Debug.print("number of samples: " + numSamples);
			Debug.print(Statistics.getCurrentKsps());
			Debug.print("pos = " + scene.getCamera().getPos());
			Debug.print("dir = " + scene.getCamera().getDir());
			
			Util.threadSleep(20);
			
			Input.update();
		}
	}
	
	private void initGui()
	{
		m_logger.printMsg("initializing UI...");
		
		try
		{
			SwingUtilities.invokeAndWait(new Runnable()
			{
				@Override
				public void run()
				{
					m_window = new Window(FRAME_WIDTH_PX, FRAME_HEIGHT_PX);
				}
			});
		}
		catch(Exception e)
		{
			m_logger.printErr("UI initialization failed");
			e.printStackTrace();
			Debug.exit();
		}
		
		m_logger.printMsg("UI initialized successfully");
	}
	
	private OsType retrieveOsType()
	{
		String osName = System.getProperty("os.name");
		
		if(osName.toLowerCase(Locale.ROOT).contains("windows"))
		{
			m_logger.printMsg("OS is Windows (" + osName + ")");
			return OsType.WINDOWS;
		}
		else
		{
			m_logger.printWrn("OS is unknown/unsupported (" + osName + ")");
			return OsType.UNKNOWN;
		}
	}
	
	public OsType getOsType()
	{
		return m_osType;
	}
}
