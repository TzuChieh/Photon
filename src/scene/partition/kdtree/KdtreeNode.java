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

import main.Ray;
import math.Vector3f;
import model.boundingVolume.AABB;
import model.primitive.Primitive;
import scene.partition.PartitionStrategy;
import util.Debug;

public class KdtreeNode extends AABB
{ 
	private static final int UNKNOWN_AXIS = 0;
	private static final int X_AXIS = 1;
	private static final int Y_AXIS = 2;
	private static final int Z_AXIS = 3;
	
	private static final double COST_TRAVERSAL    = 1.0;
	private static final double COST_INTERSECTION = 1.0;
	
	private KdtreeNode m_positiveNode;
	private KdtreeNode m_negativeNode;
	
	private List<Primitive> m_primitives;
	
	public KdtreeNode()
	{
		super();
		
		m_positiveNode = null;
		m_negativeNode = null;
		
		m_primitives = null;
	}
	
	private KdtreeNode(AABB aabb)
	{
		super(aabb);
		
		m_positiveNode = null;
		m_negativeNode = null;
		
		m_primitives = null;
	}
	
	public void build(List<Primitive> primitives)
	{
		calcAABB(primitives);
		buildChildren(primitives);
	}
	
	private void buildChildren(List<Primitive> primitives)
	{
		if(primitives.size() == 0)
			return;
		
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
		double minSplitCost = Double.MAX_VALUE;
		
		float splitPos  = 0.0f;
		int   splitAxis = UNKNOWN_AXIS;
		
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
			double pNodeProb = pNodeSurfaceArea / noSplitSurfaceArea;
			double nNodeProb = nNodeSurfaceArea / noSplitSurfaceArea;
			
			double splitCost = COST_TRAVERSAL + COST_INTERSECTION * (pNodeProb*numPnodePrims + nNodeProb*numNnodePrims);
			
			if(splitCost < minSplitCost)
			{
				minSplitCost = splitCost;
				splitPos     = xPoints[i].testPoint;
				splitAxis    = X_AXIS;
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
			double pNodeProb = pNodeSurfaceArea / noSplitSurfaceArea;
			double nNodeProb = nNodeSurfaceArea / noSplitSurfaceArea;
			
			double splitCost = COST_TRAVERSAL + COST_INTERSECTION * (pNodeProb*numPnodePrims + nNodeProb*numNnodePrims);
			
			if(splitCost < minSplitCost)
			{
				minSplitCost = splitCost;
				splitPos     = yPoints[i].testPoint;
				splitAxis    = Y_AXIS;
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
			double pNodeProb = pNodeSurfaceArea / noSplitSurfaceArea;
			double nNodeProb = nNodeSurfaceArea / noSplitSurfaceArea;
			
			double splitCost = COST_TRAVERSAL + COST_INTERSECTION * (pNodeProb*numPnodePrims + nNodeProb*numNnodePrims);
			
			if(splitCost < minSplitCost)
			{
				minSplitCost = splitCost;
				splitPos     = zPoints[i].testPoint;
				splitAxis    = Z_AXIS;
			}
		}
		
		if(minSplitCost < noSplitCost)
		{
			AABB pChildAABB = new AABB(this);
			AABB nChildAABB = new AABB(this);
			
			switch(splitAxis)
			{
			case X_AXIS:
				pChildAABB.getMinVertex().x = splitPos;
				nChildAABB.getMaxVertex().x = splitPos;
				break;
				
			case Y_AXIS:
				pChildAABB.getMinVertex().y = splitPos;
				nChildAABB.getMaxVertex().y = splitPos;
				break;
				
			case Z_AXIS:
				pChildAABB.getMinVertex().z = splitPos;
				nChildAABB.getMaxVertex().z = splitPos;
				break;
				
			default:
				Debug.printErr("KdtreeNode: unidentified split axis detected");
				break;
			}
			
			m_positiveNode = buildChild(pChildAABB, primitives);
			m_negativeNode = buildChild(nChildAABB, primitives);
		}
		else
		{
			m_primitives = primitives;
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
		
		if(childNode.isLeaf())
		{
			childNode.m_primitives = primitives;
			Debug.print(primitives.size());
		}
		
		return childNode;
	}
	
	private boolean isLeaf()
	{
		return m_positiveNode == null && m_negativeNode == null;
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
