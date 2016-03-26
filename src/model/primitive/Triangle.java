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
import math.Transform;
import math.Vector3f;
import util.Debug;

public class Triangle implements Primitive
{
	private static final float EPSILON = 0.0001f;
	
	private Vector3f m_vA;
	private Vector3f m_vB;
	private Vector3f m_vC;
	
	private Vector3f m_eAB;
	private Vector3f m_eAC;
	
	private Vector3f m_normal;
	
	// front facing: CCW vertex order
	public Triangle(Vector3f vA, Vector3f vB, Vector3f vC)
	{
		m_vA = new Vector3f(vA);
		m_vB = new Vector3f(vB);
		m_vC = new Vector3f(vC);
		
		m_eAB = vB.sub(vA);
		m_eAC = vC.sub(vA);
		m_normal = m_eAB.cross(m_eAC).normalizeLocal();
	}
	
	// Reference: Ingo Wald's PhD paper "Real Time Ray Tracing and Interactive Global Illumination".
	// This implementation is twice as fast as Moeller-Trumbore's method (stated by others, haven't profiled
	// that myself).
	// FIXME: implement the 2x faster algorithm!
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
				abPu  = m_eAB.y;
				abPv  = m_eAB.z;
				acPu  = m_eAC.y;
				acPv  = m_eAC.z;
			}
			// Z dominant, projection plane is XY
			else
			{
				hitPu = dist * ray.getDir().x + ray.getOrigin().x - m_vA.x;
				hitPv = dist * ray.getDir().y + ray.getOrigin().y - m_vA.y;
				abPu  = m_eAB.x;
				abPv  = m_eAB.y;
				acPu  = m_eAC.x;
				acPv  = m_eAC.y;
			}
		}
		// Y dominant, projection plane is ZX
		else if(Math.abs(m_normal.y) > Math.abs(m_normal.z))
		{
			hitPu = dist * ray.getDir().z + ray.getOrigin().z - m_vA.z;
			hitPv = dist * ray.getDir().x + ray.getOrigin().x - m_vA.x;
			abPu  = m_eAB.z;
			abPv  = m_eAB.x;
			acPu  = m_eAC.z;
			acPv  = m_eAC.x;
		}
		// Z dominant, projection plane is XY
		else
		{
			hitPu = dist * ray.getDir().x + ray.getOrigin().x - m_vA.x;
			hitPv = dist * ray.getDir().y + ray.getOrigin().y - m_vA.y;
			abPu  = m_eAB.x;
			abPv  = m_eAB.y;
			acPu  = m_eAC.x;
			acPv  = m_eAC.y;
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

	// Reference: Tomas Akenine-Moeller's "Fast 3D Triangle-Box Overlap Testing", which
	// is based on SAT but faster.
	@Override
	public boolean isIntersect(AABB aabb)
	{
		Vector3f vA = new Vector3f(m_vA);
		Vector3f vB = new Vector3f(m_vB);
		Vector3f vC = new Vector3f(m_vC);
		
		// move the origin to the AABB's center
		vA.subLocal(aabb.getCenter());
		vB.subLocal(aabb.getCenter());
		vC.subLocal(aabb.getCenter());
		
		Vector3f aabbHalfExtents  = aabb.getMaxVertex().sub(aabb.getCenter());
		Vector3f projection       = new Vector3f();
		Vector3f sortedProjection = new Vector3f();// (min, mid, max)
		
		// test AABB face normals (x-, y- and z-axes)
		projection.set(vA.x, vB.x, vC.x);
		projection.sort(sortedProjection);
		if(sortedProjection.z < -aabbHalfExtents.x || sortedProjection.x > aabbHalfExtents.x)
			return false;
		
		projection.set(vA.y, vB.y, vC.y);
		projection.sort(sortedProjection);
		if(sortedProjection.z < -aabbHalfExtents.y || sortedProjection.x > aabbHalfExtents.y)
			return false;
		
		projection.set(vA.z, vB.z, vC.z);
		projection.sort(sortedProjection);
		if(sortedProjection.z < -aabbHalfExtents.z || sortedProjection.x > aabbHalfExtents.z)
			return false;
		
		// test triangle's face normal
		float trigOffset = vA.dot(m_normal);
		sortedProjection.z = Math.abs(aabbHalfExtents.x * m_normal.x)
				           + Math.abs(aabbHalfExtents.y * m_normal.y)
				           + Math.abs(aabbHalfExtents.z * m_normal.z);
		sortedProjection.x = -sortedProjection.z;
		if(sortedProjection.z < trigOffset || sortedProjection.x > trigOffset)
			return false;
		
		// test 9 edge cross-products (saves in projection)
		float aabbR;
		float trigE;// projected coordinate of a triangle's edge
		float trigV;// the remaining vertex's projected coordinate
		
		// TODO: precompute triangle edges
		
		// (1, 0, 0) cross (edge AB)
		projection.set(0.0f, vA.z - vB.z, vB.y - vA.y);
		aabbR = aabbHalfExtents.y * Math.abs(projection.y) + aabbHalfExtents.z * Math.abs(projection.z);
		trigE = projection.y*vA.y + projection.z*vA.z;
		trigV = projection.y*vC.y + projection.z*vC.z;
		if(trigE < trigV) { if(trigE > aabbR || trigV < -aabbR) return false; }
		else              { if(trigV > aabbR || trigE < -aabbR) return false; }
		
		// (0, 1, 0) cross (edge AB)
		projection.set(vB.z - vA.z, 0.0f, vA.x - vB.x);
		aabbR = aabbHalfExtents.x * Math.abs(projection.x) + aabbHalfExtents.z * Math.abs(projection.z);
		trigE = projection.x*vA.x + projection.z*vA.z;
		trigV = projection.x*vC.x + projection.z*vC.z;
		if(trigE < trigV) { if(trigE > aabbR || trigV < -aabbR) return false; }
		else              { if(trigV > aabbR || trigE < -aabbR) return false; }
		
		// (0, 0, 1) cross (edge AB)
		projection.set(vA.y - vB.y, vB.x - vA.x, 0.0f);
		aabbR = aabbHalfExtents.x * Math.abs(projection.x) + aabbHalfExtents.y * Math.abs(projection.y);
		trigE = projection.x*vA.x + projection.y*vA.y;
		trigV = projection.x*vC.x + projection.y*vC.y;
		if(trigE < trigV) { if(trigE > aabbR || trigV < -aabbR) return false; }
		else              { if(trigV > aabbR || trigE < -aabbR) return false; }
		
		// (1, 0, 0) cross (edge BC)
		projection.set(0.0f, vB.z - vC.z, vC.y - vB.y);
		aabbR = aabbHalfExtents.y * Math.abs(projection.y) + aabbHalfExtents.z * Math.abs(projection.z);
		trigE = projection.y*vB.y + projection.z*vB.z;
		trigV = projection.y*vA.y + projection.z*vA.z;
		if(trigE < trigV) { if(trigE > aabbR || trigV < -aabbR) return false; }
		else              { if(trigV > aabbR || trigE < -aabbR) return false; }
		
		// (0, 1, 0) cross (edge BC)
		projection.set(vC.z - vB.z, 0.0f, vB.x - vC.x);
		aabbR = aabbHalfExtents.x * Math.abs(projection.x) + aabbHalfExtents.z * Math.abs(projection.z);
		trigE = projection.x*vB.x + projection.z*vB.z;
		trigV = projection.x*vA.x + projection.z*vA.z;
		if(trigE < trigV) { if(trigE > aabbR || trigV < -aabbR) return false; }
		else              { if(trigV > aabbR || trigE < -aabbR) return false; }
		
		// (0, 0, 1) cross (edge BC)
		projection.set(vB.y - vC.y, vC.x - vB.x, 0.0f);
		aabbR = aabbHalfExtents.x * Math.abs(projection.x) + aabbHalfExtents.y * Math.abs(projection.y);
		trigE = projection.x*vB.x + projection.y*vB.y;
		trigV = projection.x*vA.x + projection.y*vA.y;
		if(trigE < trigV) { if(trigE > aabbR || trigV < -aabbR) return false; }
		else              { if(trigV > aabbR || trigE < -aabbR) return false; }
		
		// (1, 0, 0) cross (edge CA)
		projection.set(0.0f, vC.z - vA.z, vA.y - vC.y);
		aabbR = aabbHalfExtents.y * Math.abs(projection.y) + aabbHalfExtents.z * Math.abs(projection.z);
		trigE = projection.y*vC.y + projection.z*vC.z;
		trigV = projection.y*vB.y + projection.z*vB.z;
		if(trigE < trigV) { if(trigE > aabbR || trigV < -aabbR) return false; }
		else              { if(trigV > aabbR || trigE < -aabbR) return false; }
		
		// (0, 1, 0) cross (edge CA)
		projection.set(vA.z - vC.z, 0.0f, vC.x - vA.x);
		aabbR = aabbHalfExtents.x * Math.abs(projection.x) + aabbHalfExtents.z * Math.abs(projection.z);
		trigE = projection.x*vC.x + projection.z*vC.z;
		trigV = projection.x*vB.x + projection.z*vB.z;
		if(trigE < trigV) { if(trigE > aabbR || trigV < -aabbR) return false; }
		else              { if(trigV > aabbR || trigE < -aabbR) return false; }
		
		// (0, 0, 1) cross (edge CA)
		projection.set(vC.y - vA.y, vA.x - vC.x, 0.0f);
		aabbR = aabbHalfExtents.x * Math.abs(projection.x) + aabbHalfExtents.y * Math.abs(projection.y);
		trigE = projection.x*vC.x + projection.y*vC.y;
		trigV = projection.x*vB.x + projection.y*vB.y;
		if(trigE < trigV) { if(trigE > aabbR || trigV < -aabbR) return false; }
		else              { if(trigV > aabbR || trigE < -aabbR) return false; }
		
		// no separating axis found
		return true;
	}
	
	public void getVerticesABC(Vector3f vA, Vector3f vB, Vector3f vC)
	{
		vA.set(m_vA);
		vB.set(m_vB);
		vC.set(m_vC);
	}
	
	public void getNormal(Vector3f normal)
	{
		normal.set(m_normal);
	}
	
	public Vector3f getNormal()
	{
		return m_normal;
	}
	
	@Override
	public Transform getTransform()
	{
		// TODO Auto-generated method stub
		Debug.printTodoErr();
		return null;
	}
}
