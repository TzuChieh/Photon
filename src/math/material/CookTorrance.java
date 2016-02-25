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
	
	public CookTorrance()
	{
		m_roughness = 0.5f;
		
		// around plastic
		m_f0 = new Vector3f(0.04f, 0.04f, 0.04f);
		
		m_albedo = new Vector3f(0.0f, 0.0f, 0.0f);
		m_emissivity = new Vector3f(0.0f, 0.0f, 0.0f);
	}
	
	@Override
	public Vector3f genDiffuseSampleDirIS(Vector3f N, Vector3f V)
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
	public Vector3f genSpecularSampleDirIS(Vector3f N, Vector3f V)
	{
//		Vector3f L = V.mul(-1.0f).reflectLocal(N).normalizeLocal();
		
		
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
//		Vector3f v = new Vector3f(L);
		Vector3f w = new Vector3f();
		v.calcOrthBasisAsYaxis(u, w);
		
		Vector3f sampleDirGlobal = new Vector3f(0.0f, 0.0f, 0.0f);
		
		sampleDirGlobal.addLocal(u.mulLocal(sampleDirLocal.x));
		sampleDirGlobal.addLocal(v.mulLocal(sampleDirLocal.y));
		sampleDirGlobal.addLocal(w.mulLocal(sampleDirLocal.z));
		
		Vector3f reflectionDir = V.mul(-1.0f).reflectLocal(sampleDirGlobal).normalizeLocal();
		return reflectionDir;
		
//		return sampleDirGlobal;
	}

	@Override
	public Vector3f calcReflectance(Vector3f N, Vector3f V, Vector3f sampleDirResult)
	{
		Vector3f reflectance = new Vector3f(0.0f, 0.0f, 0.0f);
		
//		if(N.dot(V) < 0.0001f)
//		{
//			System.out.println("N dot V < 0.0001");
//			return reflectance;
//		}
		
		Vector3f L;
		Vector3f H;
		
		L = genSpecularSampleDirIS(N, V);
		H = V.add(L).normalizeLocal();
		
//		do
//		{
//			L = genSpecularSampleDirIS(N, V);
//			H = V.add(L).normalizeLocal();
//		} while(L.dot(N) < 0.0001f);
		
		float NoV = Func.clamp(N.dot(V), 0.0f, 1.0f);
		float NoL = Func.clamp(N.dot(L), 0.0f, 1.0f);
		float VoH = Func.clamp(V.dot(H), 0.0f, 1.0f);
		float HoN = Func.clamp(H.dot(N), 0.0f, 1.0f);
		float HoL = Func.clamp(H.dot(L), 0.0f, 1.0f);
		
//		float NoV = N.dot(V);
//		float NoL = N.dot(L);
//		float VoH = V.dot(H);
//		float HoN = H.dot(N);
		
		// Fresnel: Schlick approximated
		Vector3f F = m_f0.complement().mulLocal((float)Math.pow(1.0f - VoH, 5)).addLocal(m_f0);
//		Vector3f F = new Vector3f(m_f0);
		float randomProb = Rand.getFloat0_1();
		float Ks = F.avg();
		
		// as specular lighting (Ks)
		if(randomProb < Ks)
		{
			sampleDirResult.set(L);
			
			// Geometry Shadowing: Cook-Torrance
			float g1 = 2.0f * HoN * NoV / (VoH);
			float g2 = 2.0f * HoN * NoL / (VoH);
			float G = Math.min(1.0f, Math.min(g1, g2));
			
			// Geometry visibility test
//			boolean v1 = VoH * NoV < 0.0001f;
//			boolean v2 = HoL * NoL < 0.0001f;
//			
//			G *= !v1 && !v2 ? 1.0f : 0.0f;
			
			// GGX Smith
//			float factor1 = VoH/NoV > 0.0f ? 1.0f : 0.0f;
//			float VoH2 = VoH*VoH;
//			float tan2_1 = (1.0f - VoH2) / VoH2;
//			float partialG1 = (factor1 * 2.0f) / (1.0f + (float)Math.sqrt(1 + m_roughness*m_roughness*tan2_1));
//			float factor2 = HoL/NoL > 0.0f ? 1.0f : 0.0f;
//			float LoH2 = HoL*HoL;
//			float tan2_2 = (1.0f - LoH2) / LoH2;
//			float partialG2 = (factor2 * 2.0f) / (1.0f + (float)Math.sqrt(1 + m_roughness*m_roughness*tan2_2));
//			float G = partialG1 * partialG2;
			
			
			// GGX Smith approximated (relative error less than 0.35%)
//			float c1 = NoV / (m_roughness * (float)Math.sqrt(1.0f - NoV*NoV));
//			float c2 = NoL / (m_roughness * (float)Math.sqrt(1.0f - NoL*NoL));
//			c1 = c1 < 1.6f ? (3.535f*c1 + 2.181f*c1*c1)/(1.0f + 2.276f*c1 + 2.577f*c1*c1) : 1.0f;
//			c2 = c2 < 1.6f ? (3.535f*c2 + 2.181f*c2*c2)/(1.0f + 2.276f*c2 + 2.577f*c2*c2) : 1.0f;
//			float sign = VoH / NoV > 0.0f ? 1.0f : 0.0f;
//			float G = sign * c1 * c2;
			
//			float denominator = (float)(4.0f * 3.14159265f * m_roughness * m_roughness * Math.pow(HoN, 4.0f)) * NoV * HoN + 0.0001f;
//			float denominator = 4.0f * NoV * HoN;
//			float denominator = 2.0f / 3.14159265f * NoV * HoN;
//			float denominator = 4.0f * NoV * HoN * (float)Math.sqrt(1.0f - HoN*HoN);
//			float denominator = HoN;
			float denominator = NoV * HoN;
			
//			if(HoN <= 0.0f)
//			{
//				System.out.println("NaN!");
//			}
			
//			System.out.println(H.length() != H.length());
			
//			reflectance.set(F.mulLocal(G / denominator)).clampLocal(0.0f, 1.0f);
			
//			if(G / denominator != G / denominator || G / denominator < 0.0001f || G / denominator > 5.0f)
			if(G / denominator != G / denominator || G / denominator == Float.POSITIVE_INFINITY)
			{
//				reflectance.set(1.0f, 1.0f, 1.0f);
				reflectance.set(0.0f, 0.0f, 0.0f);
			}
			else
			{
//				reflectance.set(F.mulLocal(G / denominator)).clampLocal(0.0f, 1.0f);
				reflectance.set(F.mul(G * VoH / denominator));
//				reflectance.set(F.mulLocal(G * (float)Math.sqrt(1.0f - HoN*HoN) / denominator));
			}
			
			
			
//			if(reflectance.x != reflectance.x || reflectance.y != reflectance.y || reflectance.z != reflectance.z)
//			{
//				System.out.println("NaN!");
//			}
		}
		// as diffuse lighting (Kd)
		else
		{
			sampleDirResult.set(genDiffuseSampleDirIS(N, V));
			
			if(sampleDirResult.dot(N) < 0.0001f)
			{
				reflectance.set(0.0f, 0.0f, 0.0f);
			}
			else
			{
				reflectance.set(m_albedo);
			}
		}
		
		return reflectance;
	}
	
	// unbiased
//	@Override
//	public Vector3f calcReflectance(Vector3f N, Vector3f V, Vector3f sampleDirResult)
//	{
//		Vector3f reflectance = new Vector3f(0.0f, 0.0f, 0.0f);
//		
//		Vector3f L = BRDF.randomHemisphereDir(N, V);
//		Vector3f H = V.add(L).normalizeLocal();
//		
//		float NoV = Func.clamp(N.dot(V), 0.0f, 1.0f);
//		float NoL = Func.clamp(N.dot(L), 0.0f, 1.0f);
//		float VoH = Func.clamp(V.dot(H), 0.0f, 1.0f);
//		float HoN = Func.clamp(H.dot(N), 0.0f, 1.0f);
//		float HoL = Func.clamp(H.dot(L), 0.0f, 1.0f);
//		
//		// Fresnel: Schlick approximated
//		Vector3f F = m_f0.complement().mulLocal((float)Math.pow(1.0f - VoH, 5)).addLocal(m_f0);
//		
//		float randomProb = Rand.getFloat0_1();
//		float Ks = F.avg();
//		
//		// as specular lighting (Ks)
//		if(randomProb < Ks)
//		{
//			sampleDirResult.set(L);
//			
//			// Geometry Shadowing: Cook-Torrance
//			float g1 = 2.0f * HoN * NoV / (VoH);
//			float g2 = 2.0f * HoN * NoL / (VoH);
//			float G = Math.min(1.0f, Math.min(g1, g2));
//			
//			// Beckmann microfacet distribution
//			float roughness2 = m_roughness * m_roughness;
//			float tanNoH2 = (1.0f - HoN * HoN) / (HoN * HoN);
//			float normFactor = 3.14159265f * roughness2 * (float)Math.pow(HoN, 4);
//			float exponential = (float)Math.exp(-tanNoH2 / roughness2);
//			float D = exponential / normFactor;
//			
//			float denominator = 2.0f / 3.14159265f / NoV;
//			
//			if(G * D / denominator != G * D / denominator || G * D / denominator == Float.POSITIVE_INFINITY)
//			{
//				reflectance.set(0.0f, 0.0f, 0.0f);
//			}
//			else
//			{
//				reflectance.set(F.mulLocal(G * D / denominator));//.clampLocal(0.0f, 1.0f);
//			}
//		}
//		// as diffuse lighting (Kd)
//		else
//		{
//			sampleDirResult.set(genDiffuseSampleDirIS(N, V));
//			
//			if(sampleDirResult.dot(N) < 0.0001f)
//			{
//				reflectance.set(0.0f, 0.0f, 0.0f);
//			}
//			else
//			{
//				reflectance.set(m_albedo);
//			}
//		}
//		
//		return reflectance;
//	}
	
	@Override
	public Vector3f getAlbedo()
	{
		return m_albedo;
	}
	
	@Override
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

	@Override
	public Vector3f getF0()
	{
		return m_f0;
	}
	
	@Override
	public float getRoughness()
	{
		return m_roughness;
	}
}
