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

package model.boundingVolume;

import main.Intersection;
import main.Ray;
import math.Transform;
import math.Vector3f;
import model.Model;
import model.primitive.Primitive;
import util.Debug;

public class AABB implements BoundingVolume
{
	private Vector3f m_minVertex;
	private Vector3f m_maxVertex;
	private Vector3f m_center;
	
	public AABB(Vector3f minVertex, Vector3f maxVertex)
	{
		m_minVertex = new Vector3f(minVertex);
		m_maxVertex = new Vector3f(maxVertex);
		
		m_center = minVertex.add(maxVertex).divLocal(2.0f);
	}
	
	public AABB(AABB other)
	{
		this(other.m_minVertex, other.m_maxVertex);
	}
	
	public Vector3f getCenter()
	{
		return m_center;
	}
	
	public Vector3f getMinVertex()
	{
		return m_minVertex;
	}
	
	public Vector3f getMaxVertex()
	{
		return m_maxVertex;
	}

	@Override
	public boolean isIntersect(Ray ray)
	{
		// TODO Auto-generated method stub
		Debug.printTodoErr();
		return false;
	}
}
