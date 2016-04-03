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

package model.boundingVolume;

import main.Intersection;
import main.Ray;
import math.Transform;
import math.Vector3f;
import model.Model;
import model.primitive.Primitive;
import util.Debug;

public class AABB implements BoundingVolume
{
	private Vector3f m_minVertex;
	private Vector3f m_maxVertex;
	private Vector3f m_center;
	
	public AABB(Vector3f minVertex, Vector3f maxVertex)
	{
		m_minVertex = new Vector3f(minVertex);
		m_maxVertex = new Vector3f(maxVertex);
		
		m_center = minVertex.add(maxVertex).divLocal(2.0f);
	}
	
	public AABB(AABB other)
	{
		this(other.m_minVertex, other.m_maxVertex);
	}
	
	public Vector3f getCenter()
	{
		return m_center;
	}
	
	public Vector3f getMinVertex()
	{
		return m_minVertex;
	}
	
	public Vector3f getMaxVertex()
	{
		return m_maxVertex;
	}

	// Reference: Kay and Kayjia's "slab method" from a project of the ACM SIGGRAPH Education Committee
	// named HyperGraph.
	// TODO: The inverse of ray direction can be precalculated if a single ray is tested against several AABBs.
	@Override
	public boolean isIntersect(Ray ray)
	{
		float tMin, tMax;
		
		float txMin = (m_minVertex.x - ray.getOrigin().x) / ray.getDir().x;
		float txMax = (m_maxVertex.x - ray.getOrigin().x) / ray.getDir().x;
		
		if(txMin < txMax)
		{
			tMin = txMin;
			tMax = txMax;
		}
		else
		{
			tMin = txMax;
			tMax = txMin;
		}
	 
		float tyMin = (m_minVertex.y - ray.getOrigin().y) / ray.getDir().y;
		float tyMax = (m_maxVertex.y - ray.getOrigin().y) / ray.getDir().y;
		
		if(tyMin < tyMax)
		{
			tMin = tMin > tyMin ? tMin : tyMin;
			tMax = tMax > tyMax ? tyMax : tMax;
		}
		else
		{
			tMin = tMin > tyMax ? tMin : tyMax;
			tMax = tMax > tyMin ? tyMin : tMax;
		}
	 
		float tzMin = (m_minVertex.z - ray.getOrigin().z) / ray.getDir().z;
		float tzMax = (m_maxVertex.z - ray.getOrigin().z) / ray.getDir().z;
		
		if(tzMin < tzMax)
		{
			tMin = tMin > tzMin ? tMin : tzMin;
			tMax = tMax > tzMax ? tzMax : tMax;
		}
		else
		{
			tMin = tMin > tzMax ? tMin : tzMax;
			tMax = tMax > tzMin ? tzMin : tMax;
		}
	 
	    return tMax > 0.0f && tMax > tMin;
	}
}
