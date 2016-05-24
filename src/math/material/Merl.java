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

package math.material;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import core.Ray;
import math.Rand;
import math.Vector3f;
import model.primitive.Interpolator;
import model.primitive.Intersection;
import util.Debug;
import util.Func;
import util.Logger;

// Experimental data of isotropic BRDF.

public class Merl implements Material
{
	public static final String DATA_DIRECTORY = "./resource/material/merl/";
	public static final String DATA_EXTENSION = ".binary";
	
	private float[] m_merlBrdf;
	
	private Logger m_logger;
	
	public Merl(String materialName)
	{
		m_logger = new Logger("MERL Data Loading: " + materialName);
		
		try
		{
			InputStream inputStream = new FileInputStream(DATA_DIRECTORY + materialName + DATA_EXTENSION);
			
			byte[] rawData = new byte[(180 * 90 * 90 * 3 * 8) + (3 * 4)];
			inputStream.read(rawData);
			
			ByteBuffer byteBuffer = ByteBuffer.wrap(rawData);
			byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
			
			m_logger.printMsg("dimension check (thetaHalf): " + byteBuffer.getInt());
			m_logger.printMsg("dimension check (thetaDiff): " + byteBuffer.getInt());
			m_logger.printMsg("dimension check (phiDiff):   " + byteBuffer.getInt());
			
			int numFloats = 180 * 90 * 90 * 3;
			m_merlBrdf = new float[numFloats];
			for(int i = 0; i < numFloats; i++)
			{
//				double sRgbValue = byteBuffer.getDouble();
//				float linearRgbValue;
//				
//				if(sRgbValue <= 0.03928)
//					linearRgbValue = (float)(sRgbValue / 12.92);
//				else
//					linearRgbValue = (float)Math.pow((sRgbValue + 0.055) / 1.055, 2.4);
//				
//				m_merlBrdf[i] = linearRgbValue;
				
				m_merlBrdf[i] = (float)byteBuffer.getDouble();
			}
			
			inputStream.close();
		}
		catch(IOException e)
		{
			m_logger.printErr("loading failed");
			e.printStackTrace();
			Debug.exitErr();// FIXME: replace by some fallback implementation
		}

		m_logger.printMsg("loaded successfully");
	}
	
	@Override
	public boolean sample(Intersection intersection, Ray ray)
	{
		Interpolator interpolator = intersection.genInterpolator();
		
		Vector3f N = interpolator.getSmoothNormal();
		Vector3f V = ray.getDir().mul(-1.0f);
		
		if(N.dot(V) < 0.0f)
			N.mulLocal(-1.0f);
		
		Vector3f L = genRandomUniformHemisphereDir(N);
		Vector3f H = V.add(L).normalizeLocal();
		
		Vector3f Hv = N.cross(H).normalizeLocal();
		Vector3f Hu = Hv.cross(H);
		float lengthU = Hu.dot(V);
		float lengthV = Hv.dot(V);

		float thetaHalf = (float)Math.acos(N.dot(H));          // 0 ~ pi/2
		float thetaDiff = (float)Math.acos(V.dot(H));          // 0 ~ pi/2
		float phiDiff   = (float)Math.atan2(lengthV, lengthU); // -pi ~ pi
		
		int index = getPhiDiffIndex(phiDiff) +
				    getThetaDiffIndex(thetaDiff) * 180 +
				    getThetaHalfIndex(thetaHalf) * 180 * 90;
		
		Vector3f brdf = new Vector3f();

		brdf.x = m_merlBrdf[index] * (1.0f / 1500.0f);
		brdf.y = m_merlBrdf[index + 90 * 90 * 180] * (1.15f / 1500.0f);
		brdf.z = m_merlBrdf[index + 90 * 90 * 180 * 2] * (1.66f / 1500.0f);
		
		brdf.mulLocal(N.dot(L));
		brdf.mulLocal(2.0f * 3.1415926536f);

		if(brdf.x < 0.0f || brdf.y < 0.0f || brdf.z < 0.0f)
			Debug.print("negative");
		
		float rrSurviveProb = Func.clamp(brdf.avg(), 0.0f, 1.0f);
		float rrScale = 1.0f / (rrSurviveProb + 0.00001f);
		float rrSpin = Rand.getFloat0_1();
		
		// russian roulette >> dead
		if(rrSpin > rrSurviveProb)
		{
			return false;
		}
		// russian roulette >> alive
		else
		{
			brdf.mulLocal(rrScale);
			ray.getWeight().mulLocal(brdf);
			ray.getDir().set(L);
			
			return true;
		}
	}
	
	// Notice that this is a non-linear mapping!
	// input:  0 ~ pi/2
	// output: 0 ~ 89
	private int getThetaHalfIndex(float thetaHalf)
	{
		float thetaHalfDeg = thetaHalf / 3.1415926536f * 180.0f;
		int index = (int)Math.sqrt(thetaHalfDeg * 90.0f);
		
		// this should prevent any value out of [0, 89] including NaN and infinity
		index = Func.clamp(index, 0, 89);
		
		return index;
	}
	
	// input:  0 ~ pi/2
	// output: 0 ~ 89
	private int getThetaDiffIndex(float thetaDiff)
	{
		int index = (int)(thetaDiff / 3.1415926536f * 180.0f);
		
		// this should prevent any value out of [0, 89] including NaN and infinity
		index = Func.clamp(index, 0, 89);
		
		return index;
	}
	
	// input:  -pi ~ pi
	// output: 0 ~ 179
	private int getPhiDiffIndex(float phiDiff)
	{
		// because of the reciprocity property of BRDF, the output of BRDF should be the
		// same under this transformation
		if(phiDiff < 0.0f)
			phiDiff += 3.1415926536f;
		
		int index = (int)(phiDiff / 3.1415926536f * 180.0f);
		
		// this should prevent any value out of [0, 179] including NaN and infinity
		index = Func.clamp(index, 0, 179);
		
		return index;
	}
	
	private Vector3f genRandomUniformHemisphereDir(Vector3f N)
	{
		float phi       = Rand.getFloat0_1() * 2.0f * 3.14159265f;
		float elevation = Rand.getFloat0_1();
		float planeR    = (float)Math.sqrt(1.0f - elevation*elevation);
		
		Vector3f u = new Vector3f();
		Vector3f v = new Vector3f(N);
		Vector3f w = new Vector3f();
		
		v.calcOrthBasisAsYaxis(u, w);
		
		return  u.mulLocal((float)Math.cos(phi) * planeR).
			add(w.mulLocal((float)Math.sin(phi) * planeR)).
			add(v.mulLocal(elevation)).normalizeLocal(); 
	}
}
