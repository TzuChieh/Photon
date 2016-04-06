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

package scene.partition.kdtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.Intersection;
import main.Ray;
import math.Vector2f;
import math.Vector3f;
import model.Model;
import model.boundingVolume.AABB;
import model.primitive.Primitive;
import scene.partition.PartitionStrategy;
import util.Debug;

public class KdtreeNode extends AABB
{ 
	private static final int UNKNOWN_AXIS = -1;
	private static final int X_AXIS = 0;
	private static final int Y_AXIS = 1;
	private static final int Z_AXIS = 2;
	
	private static final double COST_TRAVERSAL    = 1.0;
	private static final double COST_INTERSECTION = 1.0;
	
	private KdtreeNode m_positiveNode;
	private KdtreeNode m_negativeNode;
	
	private List<Primitive> m_primitives;
	
	private int   m_splitAxis;
	private float m_splitPos;
	
	public KdtreeNode()
	{
		this(new AABB());
	}
	
	private KdtreeNode(AABB aabb)
	{
		super(aabb);
		
		m_positiveNode = null;
		m_negativeNode = null;
		
		m_primitives = null;
		
		m_splitPos  = 0.0f;
		m_splitAxis = UNKNOWN_AXIS;
	}
	
	public boolean findClosestIntersection(Ray ray, Intersection intersection)
	{
		Vector2f nearFarDist = new Vector2f();
		
		if(!isIntersect(ray, nearFarDist))
		{
			// ray missed root node's aabb
			return false;
		}
		
		float nearHitDist = nearFarDist.x < 0.0f ? 0.0f : nearFarDist.x;
		float farHitDist  = nearFarDist.y;
		
		return traverseAndFindClosestIntersection(ray, intersection, 0.0f, Float.MAX_VALUE);
		
//		if(isIntersect(ray))
//		{
//			if(!isLeaf())
//			{
//				boolean hit = false;
//				
//				if(m_positiveNode != null)
//				{
//					hit |= m_positiveNode.findClosestIntersection(ray, intersection);
//				}
//				
//				if(m_negativeNode != null)
//				{
//					hit |= m_negativeNode.findClosestIntersection(ray, intersection);
//				}
//				
//				return hit;
//			}
//			else
//			{
//				Model     closestModel     = intersection.model;
//				Vector3f  closestHitPos    = intersection.intersectPoint;
//				Vector3f  closestHitNormal = intersection.intersectNormal;
//				
//				Vector3f temp = new Vector3f();
//				
//				float closestHitSquaredDist = Float.MAX_VALUE;
//				
//				if(closestModel != null)
//					closestHitSquaredDist = closestHitPos.sub(ray.getOrigin()).squareLength();
//				
//				boolean closerHitFound = false;
//				
//				for(Primitive primitive : m_primitives)
//				{
//					if(primitive.isIntersect(ray, intersection))
//					{
//						intersection.intersectPoint.sub(ray.getOrigin(), temp);
//						float squaredHitDist = temp.squareLength();
//						
//						if(squaredHitDist < closestHitSquaredDist)
//						{
//							closestHitSquaredDist = squaredHitDist;
//							
//							closestModel     = primitive.getModel();
//							closestHitPos    = intersection.intersectPoint;
//							closestHitNormal = intersection.intersectNormal;
//							
//							closerHitFound = true;
//						}
//					}
//				}
//				
//				if(closerHitFound || closestModel != null)
//				{
//					intersection.model           = closestModel;
//					intersection.intersectPoint  = closestHitPos;
//					intersection.intersectNormal = closestHitNormal;
//					
//					return true;
//				}
//				else
//				{
//					return false;
//				}
//			}
//		}
//		else
//		{
//			return false;
//		}
	}
	
	private boolean traverseAndFindClosestIntersection(Ray ray, Intersection intersection, float rayDistMin, float rayDistMax)
	{
		if(!isLeaf())
		{
			float splitAxisRayOrigin = 0.0f;
			float splitAxisRayDir    = 0.0f;
			
			switch(m_splitAxis)
			{
			case X_AXIS:
				splitAxisRayOrigin = ray.getOrigin().x;
				splitAxisRayDir    = ray.getDir().x;
				break;
				
			case Y_AXIS:
				splitAxisRayOrigin = ray.getOrigin().y;
				splitAxisRayDir    = ray.getDir().y;
				break;
				
			case Z_AXIS:
				splitAxisRayOrigin = ray.getOrigin().z;
				splitAxisRayDir    = ray.getDir().z;
				break;
				
			default:
				Debug.printErr("KdtreeNode: unidentified split axis detected");
				break;
			}
			
			KdtreeNode nearHitNode;
			KdtreeNode farHitNode;
			
			if(m_splitPos > splitAxisRayOrigin)
			{
				nearHitNode = m_negativeNode;
				farHitNode  = m_positiveNode;
			}
			else
			{
				nearHitNode = m_positiveNode;
				farHitNode  = m_negativeNode;
			}
			
			float raySplitPlaneDist = (m_splitPos - splitAxisRayOrigin) / splitAxisRayDir;
			
			// case I: split plane is behind ray, only near node is hit
			if(raySplitPlaneDist <= rayDistMin)
			{
				if(nearHitNode != null)
				{
					return nearHitNode.traverseAndFindClosestIntersection(ray, intersection, rayDistMin, rayDistMax);
				}
			}
			// case II: split plane is beyond ray's range, only near node is hit
			else if(raySplitPlaneDist >= rayDistMax)
			{
				if(nearHitNode != null)
				{
					return nearHitNode.traverseAndFindClosestIntersection(ray, intersection, rayDistMin, rayDistMax);
				}
			}
			// case III: split plane is within ray's range, both near and far node are hit
			else
			{
				if(nearHitNode != null)
				{
					if(nearHitNode.traverseAndFindClosestIntersection(ray, intersection, rayDistMin, raySplitPlaneDist))
					{
						return true;
					}
				}
				
				if(farHitNode != null)
				{
					if(farHitNode.traverseAndFindClosestIntersection(ray, intersection, raySplitPlaneDist, rayDistMax))
					{
						return true;
					}
				}
				
				return false;
			}
			
			return false;
		}
		else
		{
			Model     closestModel     = intersection.model;
			Vector3f  closestHitPos    = intersection.intersectPoint;
			Vector3f  closestHitNormal = intersection.intersectNormal;
			
			Vector3f temp = new Vector3f();
			
			float closestHitSquaredDist = Float.MAX_VALUE;
			
			if(closestModel != null)
				closestHitSquaredDist = closestHitPos.sub(ray.getOrigin()).squareLength();
			
			boolean continueTraversal = closestHitSquaredDist > rayDistMax * rayDistMax;
			
			for(Primitive primitive : m_primitives)
			{
				if(primitive.isIntersect(ray, intersection))
				{
					intersection.intersectPoint.sub(ray.getOrigin(), temp);
					float squaredHitDist = temp.squareLength();
					
					if(squaredHitDist < closestHitSquaredDist)
					{
						closestHitSquaredDist = squaredHitDist;
						
						closestModel     = primitive.getModel();
						closestHitPos    = intersection.intersectPoint;
						closestHitNormal = intersection.intersectNormal;
						
						continueTraversal = squaredHitDist > rayDistMax * rayDistMax;
					}
				}
			}
			
			intersection.model           = closestModel;
			intersection.intersectPoint  = closestHitPos;
			intersection.intersectNormal = closestHitNormal;
			
			return !continueTraversal;
		}
	}
	
	public void build(List<Primitive> primitives)
	{
		calcAABB(primitives);
		buildChildren(primitives);
	}
	
	private void buildChildren(List<Primitive> primitives)
	{
		// be aware of array sizes that are around Integer.MAX_VALUE
		TestPoint[] xPoints = new TestPoint[primitives.size() * 2];
		TestPoint[] yPoints = new TestPoint[primitives.size() * 2];
		TestPoint[] zPoints = new TestPoint[primitives.size() * 2];
		
		for(int i = 0; i < primitives.size(); i++)
		{
			AABB primAABB = primitives.get(i).calcTransformedAABB();
			
			xPoints[2 * i]     = new TestPoint(primAABB.getMinVertex().x, TestPoint.PRIMITIVE_MIN);
			xPoints[2 * i + 1] = new TestPoint(primAABB.getMaxVertex().x, TestPoint.PRIMITIVE_MAX);
			yPoints[2 * i]     = new TestPoint(primAABB.getMinVertex().y, TestPoint.PRIMITIVE_MIN);
			yPoints[2 * i + 1] = new TestPoint(primAABB.getMaxVertex().y, TestPoint.PRIMITIVE_MAX);
			zPoints[2 * i]     = new TestPoint(primAABB.getMinVertex().z, TestPoint.PRIMITIVE_MIN);
			zPoints[2 * i + 1] = new TestPoint(primAABB.getMaxVertex().z, TestPoint.PRIMITIVE_MAX);
		}
		
		Arrays.sort(xPoints);
		Arrays.sort(yPoints);
		Arrays.sort(zPoints);
		
		double xExtent = getMaxVertex().x - getMinVertex().x;
		double yExtent = getMaxVertex().y - getMinVertex().y;
		double zExtent = getMaxVertex().z - getMinVertex().z;
		double noSplitSurfaceArea = 2.0 * (xExtent * yExtent + yExtent * zExtent + zExtent * xExtent);
		double noSplitCost  = COST_INTERSECTION * (double)primitives.size();
		
//		noSplitCost = 0.0;
		
		double minSplitCost = Double.MAX_VALUE;
		
		int numNnodePrims, numPnodePrims;
		boolean boundaryPrimPassed;
		
		// analyze x-axis
		numNnodePrims = 0;
		numPnodePrims = primitives.size();
		boundaryPrimPassed = false;
		
		for(int i = 0; i < xPoints.length; i++)
		{
			if(xPoints[i].pointType == TestPoint.PRIMITIVE_MIN)
			{
				if(boundaryPrimPassed)
				{
					boundaryPrimPassed = false;
					numPnodePrims--;
				}
				
				numNnodePrims++;
			}
			
			if(xPoints[i].pointType == TestPoint.PRIMITIVE_MAX)
			{
				if(boundaryPrimPassed)
				{
					numPnodePrims--;
				}
				
				boundaryPrimPassed = true;
			}
			
			// check if the test point is a reasonable one
			if(xPoints[i].testPoint <= getMinVertex().x || xPoints[i].testPoint >= getMaxVertex().x)
				continue;
			
			double pNodeXextent = getMaxVertex().x - xPoints[i].testPoint;
			double nNodeXextent = xPoints[i].testPoint - getMinVertex().x;
			double pNodeSurfaceArea = 2.0 * (pNodeXextent * yExtent + yExtent * zExtent + zExtent * pNodeXextent);
			double nNodeSurfaceArea = 2.0 * (nNodeXextent * yExtent + yExtent * zExtent + zExtent * nNodeXextent);
			double pNodeFrac = pNodeSurfaceArea / noSplitSurfaceArea;
			double nNodeFrac = nNodeSurfaceArea / noSplitSurfaceArea;
			
			double splitCost = COST_TRAVERSAL + COST_INTERSECTION * (pNodeFrac*numPnodePrims + nNodeFrac*numNnodePrims);
			
			if(splitCost < minSplitCost)
			{
				minSplitCost = splitCost;
				m_splitPos   = xPoints[i].testPoint;
				m_splitAxis  = X_AXIS;
			}
		}
		
		// analyze y-axis
		numNnodePrims = 0;
		numPnodePrims = primitives.size();
		boundaryPrimPassed = false;
		
		for(int i = 0; i < yPoints.length; i++)
		{
			if(yPoints[i].pointType == TestPoint.PRIMITIVE_MIN)
			{
				if(boundaryPrimPassed)
				{
					boundaryPrimPassed = false;
					numPnodePrims--;
				}
				
				numNnodePrims++;
			}
			
			if(yPoints[i].pointType == TestPoint.PRIMITIVE_MAX)
			{
				if(boundaryPrimPassed)
				{
					numPnodePrims--;
				}
				
				boundaryPrimPassed = true;
			}
			
			// check if the test point is a reasonable one
			if(yPoints[i].testPoint <= getMinVertex().y || yPoints[i].testPoint >= getMaxVertex().y)
				continue;
			
			double pNodeYextent = getMaxVertex().y - yPoints[i].testPoint;
			double nNodeYextent = yPoints[i].testPoint - getMinVertex().y;
			double pNodeSurfaceArea = 2.0 * (xExtent * pNodeYextent + pNodeYextent * zExtent + zExtent * xExtent);
			double nNodeSurfaceArea = 2.0 * (xExtent * nNodeYextent + nNodeYextent * zExtent + zExtent * xExtent);
			double pNodeFrac = pNodeSurfaceArea / noSplitSurfaceArea;
			double nNodeFrac = nNodeSurfaceArea / noSplitSurfaceArea;
			
			double splitCost = COST_TRAVERSAL + COST_INTERSECTION * (pNodeFrac*numPnodePrims + nNodeFrac*numNnodePrims);
			
			if(splitCost < minSplitCost)
			{
				minSplitCost = splitCost;
				m_splitPos   = yPoints[i].testPoint;
				m_splitAxis  = Y_AXIS;
			}
		}
		
		// analyze z-axis
		numNnodePrims = 0;
		numPnodePrims = primitives.size();
		boundaryPrimPassed = false;
		
		for(int i = 0; i < zPoints.length; i++)
		{
			if(zPoints[i].pointType == TestPoint.PRIMITIVE_MIN)
			{
				if(boundaryPrimPassed)
				{
					boundaryPrimPassed = false;
					numPnodePrims--;
				}
				
				numNnodePrims++;
			}
			
			if(zPoints[i].pointType == TestPoint.PRIMITIVE_MAX)
			{
				if(boundaryPrimPassed)
				{
					numPnodePrims--;
				}
				
				boundaryPrimPassed = true;
			}
			
			// check if the test point is a reasonable one
			if(zPoints[i].testPoint <= getMinVertex().z || zPoints[i].testPoint >= getMaxVertex().z)
				continue;
			
			double pNodeZextent = getMaxVertex().z - zPoints[i].testPoint;
			double nNodeZextent = zPoints[i].testPoint - getMinVertex().z;
			double pNodeSurfaceArea = 2.0 * (xExtent * yExtent + yExtent * pNodeZextent + pNodeZextent * xExtent);
			double nNodeSurfaceArea = 2.0 * (xExtent * yExtent + yExtent * nNodeZextent + nNodeZextent * xExtent);
			double pNodeFrac = pNodeSurfaceArea / noSplitSurfaceArea;
			double nNodeFrac = nNodeSurfaceArea / noSplitSurfaceArea;
			
			double splitCost = COST_TRAVERSAL + COST_INTERSECTION * (pNodeFrac*numPnodePrims + nNodeFrac*numNnodePrims);
			
			if(splitCost < minSplitCost)
			{
				minSplitCost = splitCost;
				m_splitPos   = zPoints[i].testPoint;
				m_splitAxis  = Z_AXIS;
			}
		}
		
		if(minSplitCost < noSplitCost)
		{
			AABB pChildAABB = new AABB(this);
			AABB nChildAABB = new AABB(this);
			
			switch(m_splitAxis)
			{
			case X_AXIS:
				pChildAABB.getMinVertex().x = m_splitPos;
				nChildAABB.getMaxVertex().x = m_splitPos;
				break;
				
			case Y_AXIS:
				pChildAABB.getMinVertex().y = m_splitPos;
				nChildAABB.getMaxVertex().y = m_splitPos;
				break;
				
			case Z_AXIS:
				pChildAABB.getMinVertex().z = m_splitPos;
				nChildAABB.getMaxVertex().z = m_splitPos;
				break;
				
			default:
				Debug.printErr("KdtreeNode: unidentified split axis detected");
				break;
			}
			
			pChildAABB.updateCenter();
			nChildAABB.updateCenter();
			
			m_positiveNode = buildChild(pChildAABB, primitives);
			m_negativeNode = buildChild(nChildAABB, primitives);
		}
		else
		{
			m_primitives = primitives;
			Debug.print(primitives.size());
			Debug.print("????????");
		}
	}
	
	private KdtreeNode buildChild(AABB childAABB, List<Primitive> parentPrimitives)
	{
		List<Primitive> primitives = new ArrayList<>();
		
		for(int i = 0; i < parentPrimitives.size(); i++)
		{
			if(parentPrimitives.get(i).isIntersect(childAABB))
				primitives.add(parentPrimitives.get(i));
		}
		
		if(primitives.size() == 0)
			return null;
		
		KdtreeNode childNode = new KdtreeNode(childAABB);
		childNode.buildChildren(primitives);
		
		return childNode;
	}
	
	private boolean isLeaf()
	{
		return m_primitives != null;
	}
	
	private static class TestPoint implements Comparable<TestPoint>
	{
		public static final int PRIMITIVE_MIN = 1;
		public static final int PRIMITIVE_MAX = 2;
		
		public float testPoint;
		public int   pointType;
		
		public TestPoint(float testPoint, int pointType)
		{
			this.testPoint = testPoint;
			this.pointType = pointType;
		}

		@Override
		public int compareTo(TestPoint other)
		{
			return Float.compare(testPoint, other.testPoint);
		}
	}
}
