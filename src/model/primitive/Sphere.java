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

import java.util.List;

import core.Intersection;
import core.Ray;
import math.Vector3f;
import model.boundingVolume.AABB;
import util.Debug;
import util.Func;

public class Sphere extends AtomicPrimitive 
{
	// Notice that we've confirmed that radii larger than 10000 will
	// produce floating point precision artefact on rendered surface.
	// Radii around 1000 are fine, but no guarantee on higher values.
	
	// Sphere stores global coordinates.
	
	private static final float EPSILON = 0.0001f;
	
	private Vector3f m_center;
	private float    m_radius;
	
	public Sphere(Vector3f center, float radius)
	{
		this(center.x, center.y, center.z, radius);
	}
	
	public Sphere(float x, float y, float z, float radius)
	{
		super();
		
		m_center    = new Vector3f(x, y, z);
		m_radius    = radius;
	}
	
	public void getCenter(Vector3f result)
	{
		result.set(m_center);
	}
	
	public void setCenter(Vector3f center)
	{
		m_center.set(center);
	}
	
	public float getRadius()
	{
		return m_radius;
	}
	
	public void setRadius(float radius)
	{
		m_radius = radius;
	}

	@Override
	public boolean isIntersect(Ray ray, Intersection intersection)
	{
		// ray origin:         o
		// ray direction:      d
		// sphere center:      c
		// sphere radius:      r
		// intersection point: p
		// vector dot:         *
		// ray equation:       o + td (t is a scalar variable)
		// To find the intersection point, the length of vector (td - oc) must equals r.
		// equation: t^2(d*d) - 2t(d*op) + (oc*oc) - r^2 = 0
		// (d*d is in fact 1)
		
		Vector3f oc = m_center.sub(ray.getOrigin());// vector from ray origin to sphere center
		float b = ray.getDir().dot(oc);// b in the quadratic equation above (-2 can be cancelled out while solving t)
		float D = b*b - oc.dot(oc) + m_radius*m_radius;
		
		if(D < 0.0f)
		{
			return false;
		}
		else
		{
			float t;
			
			D = (float)Math.sqrt(D);
			
			// pick closest point in front of ray origin
			t = (t = b - D) > EPSILON ? t : ((t = b + D) > EPSILON ? t : 0.0f);
			
			if(t > 0.0f)
			{
				intersection.intersectPoint  = ray.getDir().mul(t).addLocal(ray.getOrigin());
				intersection.intersectNormal = intersection.intersectPoint.sub(m_center).normalizeLocal();
				
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	// Reference: Jim Arvo's algorithm in Graphics Gems 2
	@Override
	public boolean isIntersect(AABB aabb)
	{
		float rSquared = m_radius * m_radius;
		
	    if(m_center.x < aabb.getMinVertex().x) 
	    	rSquared -= Func.squared(m_center.x - aabb.getMinVertex().x);
	    else if(m_center.x > aabb.getMaxVertex().x) 
	    	rSquared -= Func.squared(m_center.x - aabb.getMaxVertex().x);
	    
	    if(m_center.y < aabb.getMinVertex().y) 
	    	rSquared -= Func.squared(m_center.y - aabb.getMinVertex().y);
	    else if(m_center.y > aabb.getMaxVertex().y) 
	    	rSquared -= Func.squared(m_center.y - aabb.getMaxVertex().y);
	    
	    if(m_center.z < aabb.getMinVertex().z) 
	    	rSquared -= Func.squared(m_center.z - aabb.getMinVertex().z);
	    else if(m_center.z > aabb.getMaxVertex().z) 
	    	rSquared -= Func.squared(m_center.z - aabb.getMaxVertex().z);
	    
	    return rSquared > 0;
	}
	
	@Override
	public AABB calcTransformedAABB()
	{
		AABB aabb = new AABB(m_center.sub(m_radius),
		                     m_center.add(m_radius));

		aabb.relax();
		
		return aabb;
	}

	@Override
	public void getAtomicPrimitives(List<AtomicPrimitive> results)
	{
		results.add(this);
	}

	@Override
	public Vector3f calcGeometricAveragePos()
	{
		return new Vector3f(m_center);
	}

	@Override
	public long calcGeometricWeight()
	{
		return 1L;
	}

	@Override
	public void getNormalInterpolated(Vector3f point)
	{
		// TODO Auto-generated method stub
		
	}
}
