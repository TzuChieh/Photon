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

package main;

import math.Vector3f;

public class Ray
{
	private Vector3f m_origin;
	private Vector3f m_dir;
	private Vector3f m_radiance;
	private Vector3f m_weight;
	
	public Ray(Vector3f origin, Vector3f dir)
	{
		m_origin   = new Vector3f(origin);
		m_dir      = new Vector3f(dir);
		m_radiance = new Vector3f(0.0f, 0.0f, 0.0f);
		m_weight   = new Vector3f(1.0f, 1.0f, 1.0f);
	}
	
	public Ray()
	{
		this(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, -1.0f));
	}
	
	public Vector3f getOrigin()
	{
		return m_origin;
	}
	
	public Vector3f getDir()
	{
		return m_dir;
	}
	
	public Vector3f getRadiance()
	{
		return m_radiance;
	}
	
	public Vector3f getWeight()
	{
		return m_weight;
	}
	
	@Override
	public String toString()
	{
		return "ray origin: " + m_origin + ", ray direction: " + m_dir;
	}
}
