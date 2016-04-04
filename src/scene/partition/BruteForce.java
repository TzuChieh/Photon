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

package scene.partition;

import java.util.ArrayList;
import java.util.List;

import main.Intersection;
import main.Ray;
import math.Vector3f;
import model.Model;
import model.primitive.Primitive;

public class BruteForce implements PartitionStrategy
{
	private List<Primitive> m_primitives;
	
	public BruteForce()
	{
		m_primitives = new ArrayList<>();
	}
	
	@Override
	public void addPrimitive(Primitive primitive)
	{
		m_primitives.add(primitive);
	}

	@Override
	public boolean findClosestIntersection(Ray ray, Intersection intersection)
	{
		float    squareDist      = Float.MAX_VALUE;
		Vector3f intersectPoint  = null;
		Vector3f intersectNormal = null;
		Model    model           = null;
		
		for(Primitive currentPrimitive : m_primitives)
		{
			if(currentPrimitive.isIntersect(ray, intersection))
			{
				float currentSquareDist = intersection.intersectPoint.sub(ray.getOrigin()).squareLength();
				
				if(currentSquareDist < squareDist)
				{
					squareDist      = currentSquareDist;
					model           = currentPrimitive.getModel();
					intersectPoint  = intersection.intersectPoint;
					intersectNormal = intersection.intersectNormal;
				}
			}
		}
		
		if(squareDist != Float.MAX_VALUE)
		{
			intersection.model           = model;
			intersection.intersectPoint  = intersectPoint;
			intersection.intersectNormal = intersectNormal;
			return true;
		}
		
		return false;
	}

	@Override
	public void processData()
	{
		// do nothing
	}
}
