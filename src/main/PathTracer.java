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

import math.Rand;
import math.Vector3f;
import math.material.BRDF;
import math.material.Material;
import scene.Scene;
import util.Func;

public class PathTracer
{
	public PathTracer()
	{
		
	}
	
	public void trace(Scene scene, Camera camera, Frame sampleResult)
	{
		int widthPx  = sampleResult.getWidthPx();
		int heightPx = sampleResult.getHeightPx();
		
		for(int x = 0; x < widthPx; x++)
		{
			for(int y = 0; y < heightPx; y++)
			{
				Ray ray = new Ray();
//				camera.calcRayFromPixel(ray, widthPx, heightPx, x, y);
				camera.calcRayFromPixelDistributed(ray, widthPx, heightPx, x, y);
				
//				Vector3f accuRadiance = pathTrace(scene, ray, 10);
				Vector3f accuRadiance = pathTrace(scene, ray, Integer.MAX_VALUE);
				
				sampleResult.setPixelRgb(x, y, accuRadiance.x, accuRadiance.y, accuRadiance.z);
			}
		}
	}
	
	private Vector3f pathTrace(Scene scene, Ray ray, int numBounces)
	{
		Vector3f accuRadiance = new Vector3f(0.0f, 0.0f, 0.0f);
		
		if(numBounces == -1)
		{
			return accuRadiance;
		}
		
		Intersection intersection = new Intersection();
		
		// FIXME: front facing normal
		// FIXME: PureEmissive
		
		if(scene.findClosestIntersection(ray, intersection))
		{
			Material material = intersection.model.getMaterial();
			
			if(material.getEmissivity().x != 0.0f)
			{
//				float weight = intersection.intersectNormal.dot(ray.getDir().mul(-1.0f));
				
				return accuRadiance.addLocal(material.getEmissivity());
			}
			
			Vector3f N = new Vector3f(intersection.intersectNormal);
			Vector3f V = ray.getDir().mul(-1.0f);
			
			
			Vector3f sampleDirResult = new Vector3f();
			
			Vector3f reflectance = material.calcReflectance(N, V, sampleDirResult);
			
			float rrSurviveProb = Func.clamp(reflectance.avg(), 0.0f, 1.0f);
//			float rrSurviveProb = 0.5f;
			float rrScale = 1.0f / (rrSurviveProb + 0.00001f);
			float rrSpin = Rand.getFloat0_1();
			
			// russian roulette >> dead
			if(rrSpin > rrSurviveProb)
			{
				return new Vector3f(0.0f, 0.0f, 0.0f);
			}
			else
			{
				reflectance.mulLocal(rrScale);
			}
			
//			if(reflectance.x > 2.0f || reflectance.y > 2.0f || reflectance.z > 2.0f)
//			{
//				System.out.println("reflectance wow");
//			}
			
			ray.setDir(sampleDirResult);
			
			// offset a little to prevent self intersection artefact
//			ray.setOrigin(N.mul(0.001f).addLocal(intersection.intersectPoint));
			
			ray.setOrigin(intersection.intersectPoint);
			
			Vector3f inRadiance = pathTrace(scene, ray, numBounces - 1);
			Vector3f outRadiance = inRadiance.mul(reflectance);
			
			accuRadiance.addLocal(outRadiance);
			return accuRadiance;
		}
		
		return accuRadiance;
	}
}
