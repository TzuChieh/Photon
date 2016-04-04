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
import java.util.List;

import main.Intersection;
import main.Ray;
import math.Matrix4f;
import math.Vector3f;
import model.Model;
import model.boundingVolume.AABB;
import util.Debug;

public class TriangleMesh extends Primitive
{
	private ArrayList<Triangle> m_triangles;
	
	public TriangleMesh()
	{
		super();
		
		m_triangles = new ArrayList<>();
	}
	
	@Override
	public boolean isIntersect(Ray ray, Intersection intersection)
	{
		Vector3f localRayOrigin = getModel().getTransform().getInverseModelMatrix().mul(ray.getOrigin(), 1.0f);
		Vector3f localRayDir    = getModel().getTransform().getInverseModelMatrix().mul(ray.getDir(), 0.0f).normalizeLocal();
		
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
			intersection.intersectPoint = getModel().getTransform().getModelMatrix().mul(localClosestHitPoint, 1.0f);
			intersection.intersectNormal = getModel().getTransform().getModelMatrix().mul(localClosestHitNormal, 0.0f).normalizeLocal();
			
			return true;
		}
		
		return false;
	}
	
	public void addTriangle(Triangle triangle)
	{
		m_triangles.add(triangle);
		triangle.setModel(getModel());
	}

	@Override
	public boolean isIntersect(AABB aabb)
	{
		// TODO Auto-generated method stub
		Debug.printTodoErr();
		return false;
	}
	
	@Override
	public AABB calcTransformedAABB()
	{
		float minX = Float.MAX_VALUE, maxX = Float.MIN_VALUE,
			  minY = Float.MAX_VALUE, maxY = Float.MIN_VALUE,
			  minZ = Float.MAX_VALUE, maxZ = Float.MIN_VALUE;
		
		Vector3f vA = new Vector3f();
		Vector3f vB = new Vector3f();
		Vector3f vC = new Vector3f();
		
		Vector3f tvA = new Vector3f();
		Vector3f tvB = new Vector3f();
		Vector3f tvC = new Vector3f();
		
		Matrix4f modelMatrix = getModel().getTransform().getModelMatrix();
		
		for(Triangle triangle : m_triangles)
		{
			triangle.getVerticesABC(vA, vB, vC);
			
			modelMatrix.mul(vA, 1.0f, tvA);
			modelMatrix.mul(vB, 1.0f, tvB);
			modelMatrix.mul(vC, 1.0f, tvC);
			
			     if(tvA.x > maxX) maxX = tvA.x;
			else if(tvA.x < minX) minX = tvA.x;
			     if(tvA.y > maxY) maxY = tvA.y;
			else if(tvA.y < minY) minY = tvA.y;
			     if(tvA.z > maxZ) maxZ = tvA.z;
			else if(tvA.z < minZ) minZ = tvA.z;
			     
			     if(tvB.x > maxX) maxX = tvB.x;
			else if(tvB.x < minX) minX = tvB.x;
			     if(tvB.y > maxY) maxY = tvB.y;
			else if(tvB.y < minY) minY = tvB.y;
			     if(tvB.z > maxZ) maxZ = tvB.z;
			else if(tvB.z < minZ) minZ = tvB.z;
					     
			     if(tvC.x > maxX) maxX = tvC.x;
			else if(tvC.x < minX) minX = tvC.x;
			     if(tvC.y > maxY) maxY = tvC.y;
			else if(tvC.y < minY) minY = tvC.y;
			     if(tvC.z > maxZ) maxZ = tvC.z;
			else if(tvC.z < minZ) minZ = tvC.z;
		}
		
		return new AABB(new Vector3f(minX, minY, minZ),
				        new Vector3f(maxX, maxY, maxZ));
	}
	
	@Override
	public void getAtomicPrimitives(List<Primitive> results)
	{
		for(Triangle triangle : m_triangles)
		{
			results.add(triangle);
		}
	}

	@Override
	public Vector3f calcGeometricAveragePos()
	{
		Vector3f result = new Vector3f(0, 0, 0);
		
		for(Triangle triangle : m_triangles)
		{
			result.addLocal(triangle.calcGeometricAveragePos().divLocal(m_triangles.size()));
		}
		
		return result;
	}

	@Override
	public long calcGeometricWeight()
	{
		return m_triangles.size() * 3L;
	}
	
	@Override
	public void setModel(Model model)
	{
		super.setModel(model);
		
		for(Triangle triangle : m_triangles)
		{
			triangle.setModel(model);
		}
	}
}
