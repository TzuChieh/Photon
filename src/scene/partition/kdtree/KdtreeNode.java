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

import java.util.List;

import main.Ray;
import math.Vector3f;
import model.boundingVolume.AABB;
import model.primitive.Primitive;
import scene.partition.PartitionStrategy;

public class KdtreeNode extends AABB
{ 
	private KdtreeNode m_leftNode;
	private KdtreeNode m_rightNode;
	
	public KdtreeNode()
	{
		super();
		
		m_leftNode  = null;
		m_rightNode = null;
	}
	
	public void build(List<Primitive> primitives)
	{
		if(primitives.size() == 0)
			return;
		
		calcAABB(primitives);
		
		Vector3f geometricAveragePos = new Vector3f(0, 0, 0);
	}
}
