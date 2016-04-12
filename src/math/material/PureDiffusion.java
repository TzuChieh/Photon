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

import main.Ray;
import math.Rand;
import math.Vector3f;

public class PureDiffusion implements Material
{
	private Vector3f m_albedo;
	
	public PureDiffusion(float albedoR, float albedoG, float albedoB)
	{
		m_albedo = new Vector3f(albedoR, albedoG, albedoB);
	}
	
	public PureDiffusion(Vector3f albedo)
	{
		this(albedo.x, albedo.y, albedo.z);
	}
	
	private Vector3f genLambertDiffuseSampleDirIS(Vector3f N)
	{
		float phi       = Rand.getFloat0_1() * 2.0f * 3.14159265f;
		float elevation = (float)Math.sqrt(Rand.getFloat0_1());
		float planeR    = (float)Math.sqrt(1.0f - elevation*elevation);
		
		Vector3f u = new Vector3f();
		Vector3f v = new Vector3f(N);
		Vector3f w = new Vector3f();
		
		v.calcOrthBasisAsYaxis(u, w);
		
		return  u.mulLocal((float)Math.cos(phi) * planeR).
			add(w.mulLocal((float)Math.sin(phi) * planeR)).
			add(v.mulLocal(elevation)).normalizeLocal();
	}
	
	@Override
	public boolean sample(Vector3f N, Ray ray)
	{
		float rrSurviveProb = m_albedo.avg();
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
			Vector3f diffuseReflectivity = new Vector3f(m_albedo);
			
			// account for probability
			diffuseReflectivity.mulLocal(rrScale);
			
			ray.getWeight().mulLocal(diffuseReflectivity);
			ray.getDir().set(genLambertDiffuseSampleDirIS(N));
			
			return true;
		}
	}
	
	public Vector3f getAlbedo()
	{
		return m_albedo;
	}
	
	public void setAlbedo(float r, float g, float b)
	{
		m_albedo.set(r, g, b);
	}
}
