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
import util.Func;

public final class CookTorrance implements Material
{
	// All input vectors are expected to be normalized.
	//
	// N:  surface normal
	// L:  direction to light
	// V:  direction from incident point to the viewer
	// L:  direction to incoming light
	// H:  normalize(V + L)
	// F0: reflectivity at normal incidence
	// o:  vector dot
	
	private float m_roughness;
	private Vector3f m_f0;
	private Vector3f m_albedo;
	private Vector3f m_emissivity;
	
	private boolean m_isTranslucent;
	
	public CookTorrance()
	{
		m_roughness = 0.5f;
		
		// around plastic
		m_f0 = new Vector3f(0.04f, 0.04f, 0.04f);
		
		m_albedo = new Vector3f(0.0f, 0.0f, 0.0f);
		m_emissivity = new Vector3f(0.0f, 0.0f, 0.0f);
		
		m_isTranslucent = false;
	}
	
	private Vector3f genDiffuseSampleDirIS(Vector3f N, Vector3f V)
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

	private Vector3f genSpecularSampleDirIS(Vector3f N, Vector3f V)
	{
		float rand1 = Rand.getFloat0_1();
		float rand2 = Rand.getFloat0_1();
		float phi      = rand1 * 2.0f * 3.14159265f;
//		float theta    = (float)Math.atan(Math.sqrt(-Math.pow(m_roughness, 2.0f) * Math.log(1.0f - rand2)));
		
		// slightly widened Beckmann distribution to avoid extremely large weight (worst when retroreflection ray is at
		// (grazing angles, reflectance should be very small in such situation)
		float roughness = (1.2f - 0.2f * (float)Math.sqrt(V.absDot(N))) * m_roughness;
		float theta    = (float)Math.atan(Math.sqrt(-(roughness*roughness) * Math.log(1.0f - rand2)));
		
		
		float cosTheta = (float)Math.cos(theta);
		float sinTheta = (float)Math.sin(theta);
		
		Vector3f sampleDirLocal = new Vector3f();
		sampleDirLocal.x = sinTheta * (float)Math.cos(phi);
		sampleDirLocal.y = cosTheta;
		sampleDirLocal.z = sinTheta * (float)Math.sin(phi);
		
		Vector3f u = new Vector3f();
		Vector3f v = new Vector3f(N);
		Vector3f w = new Vector3f();
		v.calcOrthBasisAsYaxis(u, w);
		
		Vector3f sampleDirGlobal = new Vector3f(0.0f, 0.0f, 0.0f);
		
		sampleDirGlobal.addLocal(u.mulLocal(sampleDirLocal.x));
		sampleDirGlobal.addLocal(v.mulLocal(sampleDirLocal.y));
		sampleDirGlobal.addLocal(w.mulLocal(sampleDirLocal.z));
		
		Vector3f reflectionDir = V.mul(-1.0f).reflectLocal(sampleDirGlobal).normalizeLocal();
		return reflectionDir;
	}
	
	@Override
	public boolean sample(Vector3f N, Ray ray)
	{
		if(m_emissivity.squareLength() != 0.0f)
		{
			ray.getRadiance().addLocal(ray.getWeight().mul(m_emissivity));
			
			return false;
		}
		
		Vector3f reflectance = new Vector3f(0.0f, 0.0f, 0.0f);
		
		Vector3f V = ray.getDir().mul(-1.0f);
		Vector3f L = genSpecularSampleDirIS(N, V);
		Vector3f H = V.add(L).normalizeLocal();
		
		float NoV = Func.clamp(N.dot(V), 0.0f, 1.0f);
		float NoL = Func.clamp(N.dot(L), 0.0f, 1.0f);
		float VoH = Func.clamp(V.dot(H), 0.0f, 1.0f);
		float HoN = Func.clamp(H.dot(N), 0.0f, 1.0f);
		float HoL = Func.clamp(H.dot(L), 0.0f, 1.0f);
		
		// Fresnel: Schlick approximated
		Vector3f F = m_f0.complement().mulLocal((float)Math.pow(1.0f - VoH, 5)).addLocal(m_f0);
		float randomProb = Rand.getFloat0_1();
		float Ks = F.avg();
		
		// as specular lighting (reflected)
		if(randomProb < Ks)
		{
			// Geometry Shadowing: Cook-Torrance
			float g1 = 2.0f * HoN * NoV / (VoH);
			float g2 = 2.0f * HoN * NoL / (VoH);
			float G = Math.min(1.0f, Math.min(g1, g2));
			
			float denominator = NoV * HoN;
			float constTerm = G * HoL / denominator;
			
			// check for NaN and assume no crazy sample weight 
			if(constTerm < 10000.0f)
			{
				reflectance.set(F.mul(constTerm));
			}
		}
		// as diffuse lighting (Kd)
		else
		{
			// if is transparent >> transmitted
			// else assume diffused
			
			// if the object is translucent, calculate transmission effects
			if(m_isTranslucent)
			{
				
			}
			// if the object is opaque (or its transparency is low enough to be considered as opaque), we
			// assume the resting energy is diffused
			else
			{
				L = genDiffuseSampleDirIS(N, V);
				
				reflectance.set(m_albedo);
			}
		}
		
		float rrSurviveProb = Func.clamp(reflectance.avg(), 0.0f, 1.0f);
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
			reflectance.mulLocal(rrScale);
			ray.getWeight().mulLocal(reflectance);
			ray.getDir().set(L);
			
			return true;
		}
	}
	
	public Vector3f getAlbedo()
	{
		return m_albedo;
	}
	
	public Vector3f getEmissivity()
	{
		return m_emissivity;
	}
	
	public void setEmissivity(float r, float g, float b)
	{
		m_emissivity.set(r, g, b);
	}
	
	public void setAlbedo(float r, float g, float b)
	{
		m_albedo.set(r, g, b);
	}
	
	public void setF0(float r, float g, float b)
	{
		m_f0.set(r, g, b);
	}
	
	public void setRoughness(float roughness)
	{
		m_roughness = roughness;
	}

	public Vector3f getF0()
	{
		return m_f0;
	}
	
	public float getRoughness()
	{
		return m_roughness;
	}
}
