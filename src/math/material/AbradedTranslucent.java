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

import core.Intersection;
import core.Ray;
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
	public boolean sample(Intersection intersection, Ray ray)
	{
//		Vector3f N = intersection.genInterpolator().getSmoothNormal();
		Vector3f N = intersection.genInterpolator().getFlatNormal();
		Vector3f V = ray.getDir().mul(-1.0f);
		Vector3f H = genMicrofacetNormalIS(N, V);
		Vector3f L = new Vector3f();
		
		float VoH = V.dot(H);
		float NoV = N.dot(V);
		float HoN = H.dot(N);
		
		// Fresnel: Schlick approximated
		Vector3f reflectivity = m_f0.complement().mulLocal((float)Math.pow(1.0f - Math.abs(VoH), 5)).addLocal(m_f0);
		float pathProb = Rand.getFloat0_1();
		float reflectionProb = reflectivity.avg() + 0.00001f;
		
		Vector3f BSDF;
		
		// as reflection (Ks)
		if(pathProb < reflectionProb)
		{
			L = V.mul(-1.0f).reflectLocal(H).normalizeLocal();
			
			// account for probability
			BSDF = reflectivity.div(reflectionProb);
		}
		// as refraction (Kr)
		else
		{
			float signNoV = NoV < 0.0f ? -1.0f : 1.0f;
			
			// assume the outside medium has an IOR of 1.0 (which is true in most cases)
			float iorRatio = signNoV < 0.0f ? m_ior / 1.0f : 1.0f / m_ior;
			float sqrValue = 1.0f - iorRatio*iorRatio*(1.0f - VoH*VoH);
			
			Vector3f T;
			
			// TIR (total internal reflection)
			if(sqrValue < 0.0f)
			{
				T = V.mul(-1.0f).reflectLocal(H).normalizeLocal();
			}
			// refraction
			else
			{
				float Hfactor =  iorRatio*VoH - signNoV * (float)Math.sqrt(sqrValue);
				float Vfactor = -iorRatio;
				T = H.mul(Hfactor).addLocal(V.mul(Vfactor)).normalizeLocal();
			}
			
			L = new Vector3f(T);
			
			// account for probability
			BSDF = reflectivity.complement().divLocal(1.0f - reflectionProb);
		}
		
		float NoL = N.dot(L);
		float HoL = H.dot(L);
		
		float absNoV = Math.abs(NoV);
		float absHoN = Math.abs(HoN);
		float absHoL = Math.abs(HoL);
		float absNoL = Math.abs(NoL);
		float absVoH = Math.abs(VoH);
		
		// Geometry Shadowing: Cook-Torrance
		float g1 = 2.0f * absHoN * absNoV / absVoH;
		float g2 = 2.0f * absHoN * absNoL / absVoH;
		float G = Math.min(1.0f, Math.min(g1, g2));
		
		// Geometry visibility test
		boolean v1 = VoH / NoV > 0.0f;
		boolean v2 = HoL / NoL > 0.0f;
		G *= v1 && v2 ? 1.0f : 0.0f;
		
		float denominator = absNoV * absHoN;
		float constTerm = G * absHoL / denominator;
		
		// check for NaN and assume no crazy sample weight 
		if(constTerm < 100.0f)
		{
			BSDF.mulLocal(constTerm);
		}
		
		float rrSurviveProb = Func.clamp(BSDF.avg(), 0.0f, 1.0f);
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
			BSDF.mulLocal(rrScale);
			ray.getWeight().mulLocal(BSDF);
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
