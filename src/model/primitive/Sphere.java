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

public class Sphere implements Primitive
{
	// Notice that we've confirmed that radii larger than 10000 will
	// produce floating point precision artefact on rendered surface.
	// Radii around 1000 are fine, but no guarantee on higher values.
	
	private static final float EPSILON = 0.0001f;
	
	private Vector3f m_center;
	private float    m_radius;
	
	private Transform m_transform;
	
	public Sphere(float x, float y, float z, float radius)
	{
		m_center    = new Vector3f(x, y, z);
		m_radius    = radius;
		m_transform = new Transform();
		
		updateTransform();
	}
	
	public void getCenter(Vector3f result)
	{
		result.set(m_center);
	}
	
	public void setCenter(Vector3f center)
	{
		m_center.set(center);
		
		updateTransform();
	}
	
	private void updateTransform()
	{
		m_transform.setPos(m_center.x, m_center.y, m_center.z);
		m_transform.update();
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
		float t;
		float b = ray.getDir().dot(oc);// b in the quadratic equation above (-2 can be cancelled out while solving t)
		float D = b*b - oc.dot(oc) + m_radius*m_radius;
		
		if(D < 0.0f)
		{
			return false;
		}
		else
		{
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

	@Override
	public boolean isIntersect(AABB aabb)
	{
		// TODO Auto-generated method stub
		Debug.printTodoErr();
		return false;
	}
	
	@Override
	public Transform getTransform()
	{
		return m_transform;
	}
}
