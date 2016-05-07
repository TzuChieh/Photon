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

package core;

import math.Vector3f;
import model.Model;

public class Intersection
{
	private Model    m_model;
	private Vector3f m_point;
	private Vector3f m_normal;
	
	public Intersection()
	{
		
	}
	
	public boolean interact(Ray ray)
	{
		boolean keepSampling = m_model.getMaterial().sample(m_normal, ray);
		
		return keepSampling;
	}
	
	public void clear()
	{
		m_model  = null;
		m_point  = null;
		m_normal = null;
	}
	
	public void setModel(Model model)
	{
		m_model = model;
	}
	
	public void setPoint(Vector3f point)
	{
		m_point = point;
	}
	
	public void setNormal(Vector3f normal)
	{
		m_normal = normal;
	}
	
	public Model getModel()
	{
		return m_model;
	}
	
	public Vector3f getPoint()
	{
		return m_point;
	}
	
	public Vector3f getNormal()
	{
		return m_normal;
	}
}
