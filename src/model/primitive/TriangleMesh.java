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

import java.util.ArrayList;

import main.Intersection;
import main.Ray;
import math.Transform;
import math.Vector3f;
import util.Debug;

public class TriangleMesh implements Primitive
{
	private ArrayList<Triangle> m_triangles;
	
	private Transform m_transform;
	
	public TriangleMesh()
	{
		m_triangles = new ArrayList<>();
		m_transform = new Transform();
	}
	
	@Override
	public boolean isIntersect(Ray ray, Intersection intersection)
	{
		Vector3f localRayOrigin = m_transform.getInverseModelMatrix().mul(ray.getOrigin(), 1.0f);
		Vector3f localRayDir    = m_transform.getInverseModelMatrix().mul(ray.getDir(), 0.0f);
		
		Ray localRay = new Ray(localRayOrigin, localRayDir);
		
		float closestSquareDist = Float.MAX_VALUE;
		Vector3f localClosestHitPoint = null;
		Vector3f localClosestHitNormal = null;
		
		for(Triangle triangle : m_triangles)
		{
			intersection.intersectPoint = null;
			intersection.intersectNormal = null;
			
			triangle.isIntersect(localRay, intersection);
			
			if(intersection.intersectPoint != null)
			{
				float squareDist = intersection.intersectPoint.sub(localRay.getOrigin()).squareLength();
				
				if(squareDist < closestSquareDist)
				{
					closestSquareDist = squareDist;
					localClosestHitPoint = intersection.intersectPoint;
					localClosestHitNormal = intersection.intersectNormal;
				}
			}
		}
		
		if(localClosestHitPoint != null)
		{
			intersection.intersectPoint = m_transform.getModelMatrix().mul(localClosestHitPoint, 1.0f);
			intersection.intersectNormal = m_transform.getModelMatrix().mul(localClosestHitNormal, 0.0f);
			
			return true;
		}
		
		return false;
	}
	
	public void addTriangle(Triangle triangle)
	{
		m_triangles.add(triangle);
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
