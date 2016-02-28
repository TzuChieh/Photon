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

public final class BRDF
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
	
	public static Vector3f lambertDiffuseIS(Vector3f albedo, Vector3f inRadiance)
	{
		return albedo.mul(inRadiance);
	}
	
	public static Vector3f lambertDiffuseISdir(Vector3f N)
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
	
	public static Vector3f randomHemisphereDir(Vector3f N, Vector3f V)
	{
		float phi      = Rand.getFloat0_1() * 2.0f * 3.14159265f;
		float cosTheta = Rand.getFloat0_1();
		float sinTheta = (float)Math.sqrt(1.0f - cosTheta*cosTheta);
		
		Vector3f u = new Vector3f();
		Vector3f v = new Vector3f(N);
		Vector3f w = new Vector3f();
		
		v.calcOrthBasisAsYaxis(u, w);
		
		return  u.mulLocal((float)Math.cos(phi) * sinTheta).
			add(w.mulLocal((float)Math.sin(phi) * sinTheta)).
			add(v.mulLocal(cosTheta)).normalizeLocal(); 
	}
	
//	public static Vector3f cookTorranceSpecularIS(Vector3f N, Vector3f V, Vector3f L, 
//			                                      float roughness, Vector3f F0,
//			                                      Vector3f inRadiance, Vector3f out_Ks)
//	{
//		Vector3f H = V.add(L).normalizeLocal();
//		
//		float NoV = Func.clamp(N.dot(V), 0.0f, 1.0f);
//		float NoL = Func.clamp(N.dot(L), 0.0f, 1.0f);
//	    float VoH = Func.clamp(V.dot(H), 0.0f, 1.0f);
//	    float LoH = Func.clamp(L.dot(H), 0.0f, 1.0f);
//	    float HoN = Func.clamp(H.dot(N), 0.0f, 1.0f);
//	    
//	    Vector3f F = Fresnel_SchlickApproximation(F0, VoH);
//	    float    G = GGX_Geometry(HoN, VoH, NoV, LoH, NoL, roughness);
//	    
//	    out_Ks.set(F);
//	    
//	    // why +0.05? to prevent divide by 0?
//	    float denominator = Func.clamp(4 * (NoV * Func.clamp(H.dot(N), 0.0f, 1.0f) + 0.0001f), 0.0f, 1.0f);
//	    
//	    Vector3f outRadiance = new Vector3f(F.mulLocal(G * (float)Math.sqrt(1.0f - NoL*NoL) / denominator));
//	    outRadiance.mulLocal(inRadiance);
//	    
//	    return outRadiance;
//	}
	
	public static Vector3f cookTorranceSpecularIS(Vector3f fresnelTerm,
			                                      Vector3f N, Vector3f V, Vector3f L, Vector3f H,
                                                  float roughness, Vector3f inRadiance)
	{
		Vector3f reflectance = new Vector3f(0.0f, 0.0f, 0.0f);
		
		
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
//		Vector3f F = m_f0.complement().mulLocal((float)Math.pow(1.0f - VoH, 5)).addLocal(m_f0);
		float randomProb = Rand.getFloat0_1();
//		float Ks = F.avg();
		
		// as specular lighting (reflected)
//		if(randomProb < Ks)
		{
//			sampleDirResult.set(L);
			
			// Geometry Shadowing: Cook-Torrance
			float g1 = 2.0f * HoN * NoV / (VoH + 0.0001f);
			float g2 = 2.0f * HoN * NoL / (VoH + 0.0001f);
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
			
			float denominator = NoV * HoN;
			float constTerm = G * HoL / denominator;
			
			
//			if(denominator < 0.0001f)
			if(constTerm != constTerm || constTerm == Float.POSITIVE_INFINITY || constTerm < 0.0f)
			{
				reflectance.set(0.0f, 0.0f, 0.0f);
			}
			else
			{
//				reflectance.set(F.mul(constTerm));
			}
		}
		// as diffuse lighting (Kd)
//		else
		{
			// if is transparent >> transmitted
			// else assume diffused
			
//			sampleDirResult.set(genDiffuseSampleDirIS(N, V));
			
//			reflectance.set(m_albedo);
		}
		
		return reflectance;
	}
	
	public static Vector3f cookTorranceSpecular(Vector3f fresnelTerm,
                                                Vector3f N, Vector3f V, Vector3f L, Vector3f H,
                                                float roughness, Vector3f inRadiance)
	{
		float NoV = Func.clamp(N.dot(V), 0.0f, 1.0f);
		float NoL = Func.clamp(N.dot(L), 0.0f, 1.0f);
		float VoH = Func.clamp(V.dot(H), 0.0f, 1.0f);
		float LoH = Func.clamp(L.dot(H), 0.0f, 1.0f);
		float HoN = Func.clamp(H.dot(N), 0.0f, 1.0f);
		
		Vector3f F = new Vector3f(fresnelTerm);
//		float    G = G_CookTorrance(HoN, VoH, NoV, LoH, NoL, roughness);
		float    G = G_SchlickSmithGGXapproximated(LoH, roughness);
		float    D = D_GGX(HoN, roughness);
		
		float denominator = 4;
		
		Vector3f outRadiance = new Vector3f(F.mulLocal(G * D / denominator).clampLocal(0.0f, 1.0f));
		outRadiance.mulLocal(inRadiance);
		
		return outRadiance;
	}
	
	public static Vector3f cookTorranceSpecularISdir(Vector3f N, Vector3f V, float roughness)
	{
//		Vector3f reflectionDir = V.mul(-1.0f).reflectLocal(N).normalizeLocal();
		
//		float rand1 = Rand.getFloat0_1();
//		float rand2 = Rand.getFloat0_1();
//		float phi      = rand1 * 2.0f * 3.14159265f;
//		float cosTheta = (float)Math.sqrt((1.0f - rand2) / (1.0f + (roughness*roughness - 1) * rand2 + 0.0001f));
//		float sinTheta = (float)Math.sqrt(1.0f - cosTheta*cosTheta);
		
		float rand1 = Rand.getFloat0_1();
		float rand2 = Rand.getFloat0_1();
		float phi      = rand1 * 2.0f * 3.14159265f;
//		float theta    = (float)Math.atan(-Math.pow(roughness, 2.0f) * Math.log(1.0f - rand2));
		float theta    = (float)Math.atan(Math.sqrt(-Math.pow(roughness, 2.0f) * Math.log(1.0f - rand2)));
		float cosTheta = (float)Math.cos(theta);
		float sinTheta = (float)Math.sin(theta);
		
		Vector3f sampleDirLocal = new Vector3f();
		sampleDirLocal.x = sinTheta * (float)Math.cos(phi);
		sampleDirLocal.y = cosTheta;
		sampleDirLocal.z = sinTheta * (float)Math.sin(phi);
		
		Vector3f u = new Vector3f();
//		Vector3f v = new Vector3f(reflectionDir);
		Vector3f v = new Vector3f(N);
		Vector3f w = new Vector3f();
		v.calcOrthBasisAsYaxis(u, w);
		
		Vector3f sampleDirGlobal = new Vector3f(0.0f, 0.0f, 0.0f);
		
		sampleDirGlobal.addLocal(u.mulLocal(sampleDirLocal.x));
		sampleDirGlobal.addLocal(v.mulLocal(sampleDirLocal.y));
		sampleDirGlobal.addLocal(w.mulLocal(sampleDirLocal.z));
		
//		return sampleDirGlobal.n7ormalizeLocal();
		
		Vector3f reflectionDir = V.mul(-1.0f).reflectLocal(sampleDirGlobal).normalizeLocal();
		return reflectionDir;
	}
	
	private static float G_CookTorrance(float NoH, float VoH, float VoN, float LoH, float LoN, float roughness)
	{
		// GGX Smith
//		float factor1 = VoH/VoN > 0.0f ? 1.0f : 0.0f;
//		float VoH2 = VoH*VoH + 0.0001f;
//		float tan2_1 = (1.0f - VoH2) / VoH2;
//		float partialG1 = (factor1 * 2.0f) / (1.0f + (float)Math.sqrt(1 + roughness*roughness*tan2_1));
//		
//		float factor2 = LoH/LoN > 0.0f ? 1.0f : 0.0f;
//		float LoH2 = LoH*LoH + 0.0001f;
//		float tan2_2 = (1.0f - LoH2) / LoH2;
//		float partialG2 = (factor2 * 2.0f) / (1.0f + (float)Math.sqrt(1 + roughness*roughness*tan2_2));
//		
//		return partialG1 * partialG2;
		
		float g1 = 2.0f * NoH * VoN / (VoH + 0.0001f);
		float g2 = 2.0f * NoH * LoN / (VoH + 0.0001f);
		
		float g = Math.min(1.0f, Math.min(g1, g2));
		
		return g;
	}
	
//	public static float D_Beckmann()
//	{
//		
//	}
	
	private static float D_GGX(float NoH, float roughness)
	{
		float roughness2 = roughness * roughness;
		float NoH2 = NoH * NoH;
		float factor = NoH2 * (roughness2 - 1.0f) + 1.0f;
		
		return roughness2 / (3.14159265f * factor * factor + 0.0001f);
	}
	
	private static float G_SchlickSmithGGXapproximated(float LoH, float roughness)
	{
		float k            = roughness * roughness / 2.0f;
		float kSqr         = k * k;
		float oneMinusKSqr = 1.0f - kSqr;

		return 1.0f / (LoH * LoH * oneMinusKSqr + kSqr);
	}
	
	private static Vector3f Fresnel_SchlickApproximation(Vector3f F0, float VoH)
	{
		Vector3f f = F0.complement().mulLocal((float)Math.pow(1.0f - VoH, 5)).addLocal(F0);
		
		return f;
	}
	
	public static Vector3f Fresnel_SchlickApproximation(Vector3f F0, Vector3f V, Vector3f H)
	{
		return Fresnel_SchlickApproximation(F0, Func.clamp(V.dot(H), 0.0f, 1.0f));
		
//		return F0.add(new Vector3f(1.0f, 1.0f, 1.0f).sub(F0).mul((float)Math.pow(1.0f - V.dot(H), 5.0f)));
	}
}
