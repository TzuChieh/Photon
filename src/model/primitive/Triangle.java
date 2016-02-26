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

package model.primitive;

import main.Intersection;
import main.Ray;
import math.Vector3f;

public class Triangle implements Primitive
{
	private static final float EPSILON = 0.0001f;
	
	private Vector3f m_vA;
	private Vector3f m_vB;
	private Vector3f m_vC;
	
	private Vector3f m_AB;
	private Vector3f m_AC;
	
	private Vector3f m_normal;
	
	// front facing: CCW vertex order
	public Triangle(Vector3f vA, Vector3f vB, Vector3f vC)
	{
		m_vA = new Vector3f(vA);
		m_vB = new Vector3f(vB);
		m_vC = new Vector3f(vC);
		
		m_AB = vB.sub(vA);
		m_AC = vC.sub(vA);
		m_normal = m_AB.cross(m_AC).normalizeLocal();
	}
	
	// Reference: Ingo Wald's PhD paper "Real Time Ray Tracing and Interactive Global Illumination".
	// This implementation is twice as fast as Moeller-Trumbore's method (stated by others, haven't profiled
	// that myself).
	@Override
	public boolean isIntersect(Ray ray, Intersection intersection)
	{
		float dist = ray.getOrigin().sub(m_vA).dot(m_normal) / (-ray.getDir().dot(m_normal));
		
		// reject by distance
		if(dist < EPSILON || dist > Float.MAX_VALUE || dist != dist) 
			return false;
		
		// projected hit point
		float hitPu, hitPv;
		
		// projected side vector AB and AC
		float abPu, abPv, acPu, acPv;
					
		// find dominant axis
		if(Math.abs(m_normal.x) > Math.abs(m_normal.y))
		{
			// X dominant, projection plane is YZ
			if(Math.abs(m_normal.x) > Math.abs(m_normal.z))
			{
				hitPu = dist * ray.getDir().y + ray.getOrigin().y - m_vA.y;
				hitPv = dist * ray.getDir().z + ray.getOrigin().z - m_vA.z;
				abPu  = m_AB.y;
				abPv  = m_AB.z;
				acPu  = m_AC.y;
				acPv  = m_AC.z;
			}
			// Z dominant, projection plane is XY
			else
			{
				hitPu = dist * ray.getDir().x + ray.getOrigin().x - m_vA.x;
				hitPv = dist * ray.getDir().y + ray.getOrigin().y - m_vA.y;
				abPu  = m_AB.x;
				abPv  = m_AB.y;
				acPu  = m_AC.x;
				acPv  = m_AC.y;
			}
		}
		// Y dominant, projection plane is ZX
		else if(Math.abs(m_normal.y) > Math.abs(m_normal.z))
		{
			hitPu = dist * ray.getDir().z + ray.getOrigin().z - m_vA.z;
			hitPv = dist * ray.getDir().x + ray.getOrigin().x - m_vA.x;
			abPu  = m_AB.z;
			abPv  = m_AB.x;
			acPu  = m_AC.z;
			acPv  = m_AC.x;
		}
		// Z dominant, projection plane is XY
		else
		{
			hitPu = dist * ray.getDir().x + ray.getOrigin().x - m_vA.x;
			hitPv = dist * ray.getDir().y + ray.getOrigin().y - m_vA.y;
			abPu  = m_AB.x;
			abPv  = m_AB.y;
			acPu  = m_AC.x;
			acPv  = m_AC.y;
		}
		
		// TODO: check if these operations are possible of producing NaNs
		
		// barycentric coordinate of vertex B in the projected plane
		float baryB = (hitPu*acPv - hitPv*acPu) / (abPu*acPv - abPv*acPu);
		if(baryB < 0.0f) return false;
		
		// barycentric coordinate of vertex C in the projected plane
		float baryC = (hitPu*abPv - hitPv*abPu) / (acPu*abPv - abPu*acPv);
		if(baryC < 0.0f) return false;
		
		if(baryB + baryC > 1.0f) return false;
		
		// so the ray intersects the triangle (TODO: reuse calculated results!)
		intersection.intersectPoint  = ray.getDir().mul(dist).addLocal(ray.getOrigin());
		intersection.intersectNormal = new Vector3f(m_normal);
		
		return true;
	}
}
