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

import main.Intersection;
import main.Ray;
import model.boundingVolume.AABB;
import model.primitive.Primitive;
import scene.partition.PartitionStrategy;

public class Kdtree implements PartitionStrategy
{
	private List<Primitive> m_atomicPrimitives;
	
	private KdtreeNode      m_rootKdtreeNode;
	
	public Kdtree()
	{
		m_atomicPrimitives = new ArrayList<>();
	}
	
	@Override
	public boolean findClosestIntersection(Ray ray, Intersection intersection)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addPrimitive(Primitive primitive)
	{
		primitive.getAtomicPrimitives(m_atomicPrimitives);
	}

	@Override
	public void processData()
	{
		m_rootKdtreeNode = new KdtreeNode();
		m_rootKdtreeNode.build(m_atomicPrimitives);
	}
}
