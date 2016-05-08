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

package core;

import math.Vector3f;
import model.Model;
import model.primitive.AtomicPrimitive;
import model.primitive.Interpolator;

public class Intersection
{
	private AtomicPrimitive m_hitAtomicPrimitive;
	private Vector3f        m_hitPoint;
	private Vector3f        m_hitNormal;
	
	public Intersection()
	{
		
	}
	
	public Intersection(Intersection other)
	{
		set(other);
	}
	
	public boolean interact(Ray ray)
	{
		boolean keepSampling = m_hitAtomicPrimitive.getModel().getMaterial().sample(this, ray);
		
		return keepSampling;
	}
	
	public void clear()
	{
		m_hitPoint  = null;
		m_hitNormal = null;
		
		m_hitAtomicPrimitive = null;
	}
	
	public void setHitPoint(Vector3f point)   { m_hitPoint = point;   }
	public void setHitNormal(Vector3f normal) { m_hitNormal = normal; }
	
	public Vector3f getHitPoint()  { return m_hitPoint;  }
	public Vector3f getHitNormal() { return m_hitNormal; }
	
	public void setHitAtomicPrimitive(AtomicPrimitive atomicPrimitive)
	{
		m_hitAtomicPrimitive = atomicPrimitive;
	}
	
//	public AtomicPrimitive getHitAtomicPrimitive()
//	{
//		return m_hitAtomicPrimitive;
//	}
	
	public void set(Intersection other)
	{
		m_hitPoint           = other.m_hitPoint;
		m_hitNormal          = other.m_hitNormal;
		m_hitAtomicPrimitive = other.m_hitAtomicPrimitive;
	}
	
	public Interpolator getInterpolator()
	{
		return m_hitAtomicPrimitive.getInterpolator(this);
	}
}
