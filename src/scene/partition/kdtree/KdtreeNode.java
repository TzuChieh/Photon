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
import java.util.List;

import main.Ray;
import math.Vector3f;
import model.boundingVolume.AABB;
import model.primitive.Primitive;
import scene.partition.PartitionStrategy;
import util.Debug;

public class KdtreeNode extends AABB
{ 
	private static final int X = 0;
	private static final int Y = 1;
	private static final int Z = 2;
	
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
		if(primitives.size() == 0)
			return;
		
		calcAABB(primitives);
		
		buildChildren(primitives);
	}
	
	private void buildChildren(List<Primitive> primitives)
	{
		// calculate total geometric average position
		Vector3f totalAveragePos      = new Vector3f(0, 0, 0);
		long     totalGeometricWeight = 0L;
		
		for(Primitive primitive : primitives)
			totalGeometricWeight += primitive.calcGeometricWeight();
		
		for(Primitive primitive : primitives)
		{
			Vector3f geometricAveragePos = primitive.calcGeometricAveragePos();
			float    geometricFactor     = (float)primitive.calcGeometricWeight() / (float)totalGeometricWeight;
			
			totalAveragePos.addLocal(geometricAveragePos.mulLocal(geometricFactor));
		}
		
		// split primitives based on their average vertex position's sidedness against 
		// the total average vertex position, the cutting plane cuts the longest extent 
		// of the AABB
		List<Primitive> positivePrimitives = new ArrayList<>();
		List<Primitive> negativePrimitives = new ArrayList<>();
		
		float extentX = getMaxVertex().x - getMinVertex().x;
		float extentY = getMaxVertex().y - getMinVertex().y;
		float extentZ = getMaxVertex().z - getMinVertex().z;
		
		int maxExtent;
		
		if(extentX > extentY)
		{
			if(extentX > extentZ)
				maxExtent = X;
			else
				maxExtent = Z;
		}
		else
		{
			if(extentY > extentZ)
				maxExtent = Y;
			else
				maxExtent = Z;
		}
		
		m_positiveNode = new KdtreeNode(genChildAABB(maxExtent, totalAveragePos,  1));
		m_negativeNode = new KdtreeNode(genChildAABB(maxExtent, totalAveragePos, -1));
		
		for(Primitive primitive : primitives)
		{
			if(primitive.isIntersect(m_positiveNode))
				positivePrimitives.add(primitive);
			
			if(primitive.isIntersect(m_negativeNode))
				negativePrimitives.add(primitive);
		}
		
		int numDuplicates = numDuplicates(positivePrimitives, negativePrimitives);
		
		if(positivePrimitives.size() != 0)
		{
			float duplicateFraction = (float)numDuplicates / (float)positivePrimitives.size();
			
//			Debug.print("" + m_positiveNode.getMinVertex() + m_positiveNode.getMaxVertex());
//			Debug.print(positivePrimitives.get(0));
//			Debug.print(positivePrimitives.size());
			Debug.print(numDuplicates);
			
			if(duplicateFraction < 0.5f && m_positiveNode.isIntersect(totalAveragePos))
				m_positiveNode.buildChildren(positivePrimitives);
			else
				m_positiveNode.m_primitives = positivePrimitives;
		}
		else
		{
			m_positiveNode = null;
		}
		
		if(negativePrimitives.size() != 0)
		{
			float duplicateFraction = (float)numDuplicates / (float)negativePrimitives.size();
			
			if(duplicateFraction < 0.5f && m_positiveNode.isIntersect(totalAveragePos))
				m_negativeNode.buildChildren(negativePrimitives);
			else
				m_negativeNode.m_primitives = negativePrimitives;
		}
		else
		{
			m_negativeNode = null;
		}
	}
	
	private int numDuplicates(List<Primitive> positivePrims, List<Primitive> negativePrims)
	{
		int duplicatedNum = 0;
		
		for(int i = 0; i < positivePrims.size(); i++)
		{
			for(int j = 0; j < negativePrims.size(); j++)
			{
				if(positivePrims.get(i) == negativePrims.get(j))
				{
					duplicatedNum++;
					break;
				}
			}
		}
		
		return duplicatedNum;
	}
	
	private AABB genChildAABB(int maxExtent, Vector3f totalAveragePos, int sidedness)
	{
		AABB result = new AABB(this);
		
		switch(maxExtent)
		{
		case X:
			if(sidedness == 1)
				result.getMinVertex().x = totalAveragePos.x;
			else
				result.getMaxVertex().x = totalAveragePos.x;
			break;
			
		case Y:
			if(sidedness == 1)
				result.getMinVertex().y = totalAveragePos.y;
			else
				result.getMaxVertex().y = totalAveragePos.y;
			break;
			
		case Z:
			if(sidedness == 1)
				result.getMinVertex().z = totalAveragePos.z;
			else
				result.getMaxVertex().z = totalAveragePos.z;
			break;
		}
		
		result.updateCenter();
		
		return result;
	}
}
