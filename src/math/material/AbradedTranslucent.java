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

public final class AbradedTranslucent implements Material
{
	// All input vectors are expected to be normalized.
	//
	// N:  surface normal
	// L:  direction to light
	// V:  direction from incident point to the viewer
	// L:  direction toward incoming light
	// H:  normalize(V + L)
	// T:  direction toward transmitted light
	// F0: reflectivity at normal incidence
	// o:  vector dot
	
	private float m_roughness;
	private Vector3f m_f0;
	private Vector3f m_albedo;
	private float    m_ior;
	
	public AbradedTranslucent()
	{
		m_roughness = 0.5f;
		
		// around plastic
		m_f0 = new Vector3f(0.04f, 0.04f, 0.04f);
		
		m_albedo = new Vector3f(0.0f, 0.0f, 0.0f);
		m_ior = 1.0f;
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

	private Vector3f genMicrofacetNormalIS(Vector3f N, Vector3f V)
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
		
		return sampleDirGlobal.normalizeLocal();
	}
	
	@Override
	public boolean sample(Vector3f N, Ray ray)
	{
		Vector3f reflectance = new Vector3f(0.0f, 0.0f, 0.0f);
		
		Vector3f V = ray.getDir().mul(-1.0f);
		
		boolean isBackFaceIncident = V.dot(N) < 0.0f;
		
		// make N and V on the same hemisphere
		if(isBackFaceIncident)
		{
			N.mulLocal(-1.0f);
		}
		
		Vector3f H = genMicrofacetNormalIS(N, V);
		Vector3f L;
		
		
		
		float VoH = Func.clamp(V.dot(H), 0.0f, 1.0f);
		
		// Fresnel: Schlick approximated
		Vector3f F = m_f0.complement().mulLocal((float)Math.pow(1.0f - VoH, 5)).addLocal(m_f0);
		float pathProb = Rand.getFloat0_1();
		float reflectionProb = F.avg() + 0.00001f;
		
		// as reflection (Ks)
		if(pathProb < reflectionProb)
		{
			L = V.mul(-1.0f).reflectLocal(H).normalizeLocal();
			
			float NoV = Func.clamp(N.dot(V), 0.0f, 1.0f);
			float NoL = Func.clamp(N.dot(L), 0.0f, 1.0f);
			float HoN = Func.clamp(H.dot(N), 0.0f, 1.0f);
			float HoL = Func.clamp(H.dot(L), 0.0f, 1.0f);
			
			// account for probability
			F.divLocal(reflectionProb);
			
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
		// as refraction (Kr)
		else
		{
			float HoV = H.dot(V);
			
			// assume the outside medium has an IOR of 1.0 (which is true in most cases)
			float iorRatio = isBackFaceIncident ? m_ior / 1.0f : 1.0f / m_ior;
			float sqrValue = 1.0f - iorRatio*iorRatio*(1.0f - HoV*HoV);
			
			Vector3f T;
			
			// TIR (total internal reflection)
			if(sqrValue < 0.0f)
			{
				T = V.mul(-1.0f).reflectLocal(H).normalizeLocal();
				
//				System.out.println("TIR");
			}
			// refraction
			else
			{
				float Hfactor =  iorRatio*HoV - (float)Math.sqrt(sqrValue);
				float Vfactor = -iorRatio;
				T = H.mul(Hfactor).addLocal(V.mul(Vfactor)).normalizeLocal();
			}
			
			L = new Vector3f(T);
			
			// account for probability
			Vector3f refractivity = F.complement().divLocal(1.0f - reflectionProb);
			
			float NoV = N.absDot(V);
			float NoL = N.absDot(L);
			float HoN = H.absDot(N);
			float HoL = H.absDot(L);
			
			// Geometry Shadowing: Cook-Torrance
			float g1 = 2.0f * HoN * NoV / (VoH);
			float g2 = 2.0f * HoN * NoL / (VoH);
			float G = Math.min(1.0f, Math.min(g1, g2));
			
			float denominator = NoV * HoN;
			float constTerm = G * HoL / denominator;
			
			// check for NaN and assume no crazy sample weight 
			if(constTerm < 10000.0f)
			{
				reflectance.set(refractivity.mul(constTerm));
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
	
	public void setIor(float ior)
	{
		m_ior = ior;
	}
}
