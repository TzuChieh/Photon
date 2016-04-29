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

import java.util.List;

import core.Intersection;
import core.Ray;
import math.Transform;
import math.Vector2f;
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
		
		m_center = new Vector3f();
		updateCenter();
	}
	
	public AABB()
	{
		this(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
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
	
	public void updateCenter()
	{
		m_center.set(m_minVertex).addLocal(m_maxVertex).divLocal(2.0f);
	}

	// Reference: Kay and Kayjia's "slab method" from a project of the ACM SIGGRAPH Education Committee
	// named HyperGraph.
	// Note: This algorithm can produce NaNs which will generate false positives in rare cases. Although 
	// it can be handled, we ignore it since the performance will degrade and bounding volumes can produce
	// false positives already (but for k-d tree, this may produce visual artifact... not sure).
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
	 
	    return tMax >= 0.0f && tMax >= tMin;
	}
	
	/**
	 * @return Returned boolean value indicates whether the ray is intersecting the AABB
	 *         or not. If there's an intersection, the near and far hit distance will be
	 *         stored in inOutDist like (near hit distance, far hit distance); if the ray
	 *         origin is inside the AABB, near hit distance will be negative.
	 */
	public boolean isIntersect(Ray ray, Vector2f inOutDist)
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
		
		if(tMax < 0.0f || tMax < tMin)
			return false;
		
		// ray is intersecting AABB, but whole AABB is behind the ray origin
//		if(tMax < 0)
//		{
//			inOutDist.x = tMax;
//			inOutDist.y = tMin;
//		    return false;
//		}
		
		// ray is intersecting AABB, and whole AABB is in front of the ray origin
		inOutDist.x = tMin;
		inOutDist.y = tMax;
	 
	    return true;
	}
	
	@Override
	public boolean isIntersect(Vector3f point)
	{
		return point.x < m_maxVertex.x && point.x > m_minVertex.x
			&& point.y < m_maxVertex.y && point.y > m_minVertex.y
			&& point.z < m_maxVertex.z && point.z > m_minVertex.z;
	}
	
	public void calcAABB(List<? extends Primitive> primitives)
	{
		if(primitives.size() == 0)
		{
			m_minVertex.set(0, 0, 0);
			m_maxVertex.set(0, 0, 0);
			updateCenter();
			return;
		}
		
		AABB initialAABB = primitives.get(0).calcTransformedAABB();
		m_minVertex.set(initialAABB.getMinVertex());
		m_maxVertex.set(initialAABB.getMaxVertex());
		
		for(int i = 1; i < primitives.size(); i++)
		{
			AABB primitiveAABB = primitives.get(i).calcTransformedAABB();
			m_minVertex.minLocal(primitiveAABB.getMinVertex());
			m_maxVertex.maxLocal(primitiveAABB.getMaxVertex());
		}
		
		updateCenter();
	}
}
