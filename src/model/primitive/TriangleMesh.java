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
import math.Vector3f;
import model.bounding.AABB;

public class TriangleMesh implements Primitive
{
	private ArrayList<Triangle> m_triangles;
	
	public TriangleMesh()
	{
		m_triangles = new ArrayList<>();
	}
	
	@Override
	public boolean isIntersect(Ray ray, Intersection intersection)
	{
		float closestSquareDist = Float.MAX_VALUE;
		Vector3f closestHitPoint = null;
		Vector3f closestHitNormal = null;
		
		for(Triangle triangle : m_triangles)
		{
			intersection.intersectPoint = null;
			intersection.intersectNormal = null;
			
			triangle.isIntersect(ray, intersection);
			
			if(intersection.intersectPoint != null)
			{
				float squareDist = intersection.intersectPoint.sub(ray.getOrigin()).squareLength();
				
				if(squareDist < closestSquareDist)
				{
					closestSquareDist = squareDist;
					closestHitPoint = intersection.intersectPoint;
					closestHitNormal = intersection.intersectNormal;
				}
			}
		}
		
		if(closestHitPoint != null)
		{
			intersection.intersectPoint = closestHitPoint;
			intersection.intersectNormal = closestHitNormal;
			
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
		return false;
	}
}
