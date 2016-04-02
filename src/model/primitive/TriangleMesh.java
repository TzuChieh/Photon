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
import model.Model;
import model.boundingVolume.AABB;
import util.Debug;

public class TriangleMesh implements Primitive
{
	private ArrayList<Triangle> m_triangles;
	
	private Model m_model;
	
	public TriangleMesh()
	{
		m_triangles = new ArrayList<>();
		
		m_model = null;
	}
	
	@Override
	public boolean isIntersect(Ray ray, Intersection intersection)
	{
		Vector3f localRayOrigin = m_model.getTransform().getInverseModelMatrix().mul(ray.getOrigin(), 1.0f);
		Vector3f localRayDir    = m_model.getTransform().getInverseModelMatrix().mul(ray.getDir(), 0.0f).normalizeLocal();
		
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
			intersection.intersectPoint = m_model.getTransform().getModelMatrix().mul(localClosestHitPoint, 1.0f);
			intersection.intersectNormal = m_model.getTransform().getModelMatrix().mul(localClosestHitNormal, 0.0f).normalizeLocal();
			
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
	public Model getModel()
	{
		return m_model;
	}

	@Override
	public void setModel(Model model)
	{
		m_model = model;
	}
	
	@Override
	public AABB calcTransformedAABB()
	{
		// min, max value of the mesh's position components
		float minX = 0, maxX = 0,
			  minY = 0, maxY = 0,
			  minZ = 0, maxZ = 0;
		
		Vector3f vA = new Vector3f();
		Vector3f vB = new Vector3f();
		Vector3f vC = new Vector3f();
		
		for(Triangle triangle : m_triangles)
		{
			triangle.getVerticesABC(vA, vB, vC);
			
			     if(vA.x > maxX) maxX = vA.x;
			else if(vA.x < minX) minX = vA.x;
			     if(vA.y > maxY) maxY = vA.y;
			else if(vA.y < minY) minY = vA.y;
			     if(vA.z > maxZ) maxZ = vA.z;
			else if(vA.z < minZ) minZ = vA.z;
			     
			     if(vB.x > maxX) maxX = vB.x;
			else if(vB.x < minX) minX = vB.x;
			     if(vB.y > maxY) maxY = vB.y;
			else if(vB.y < minY) minY = vB.y;
			     if(vB.z > maxZ) maxZ = vB.z;
			else if(vB.z < minZ) minZ = vB.z;
					     
			     if(vC.x > maxX) maxX = vC.x;
			else if(vC.x < minX) minX = vC.x;
			     if(vC.y > maxY) maxY = vC.y;
			else if(vC.y < minY) minY = vC.y;
			     if(vC.z > maxZ) maxZ = vC.z;
			else if(vC.z < minZ) minZ = vC.z;
		}
		
		return new AABB(new Vector3f(minX, minY, minZ),
				        new Vector3f(maxX, maxY, maxZ));
	}
}
