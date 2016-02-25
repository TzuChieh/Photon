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

package math.material;

import math.Vector3f;

public class PureEmissive implements Material
{
	private Vector3f m_emissivity;
	
	public PureEmissive(Vector3f emissivity)
	{
		m_emissivity = emissivity;
	}
	
	@Override
	public Vector3f genSpecularSampleDirIS(Vector3f N, Vector3f V)
	{
		return new Vector3f(0.0f, 0.0f, -1.0f);
	}

	@Override
	public Vector3f calcReflectance(Vector3f N, Vector3f V, Vector3f L)
	{
		return new Vector3f(0.0f, 0.0f, 0.0f);
	}

	@Override
	public Vector3f getAlbedo()
	{
		return new Vector3f(0.0f, 0.0f, 0.0f);
	}
	
	@Override
	public Vector3f getEmissivity()
	{
		return m_emissivity;
	}
	
	public void setEmissivity(float r, float g, float b)
	{
		m_emissivity.set(r, g, b);
	}

	@Override
	public Vector3f genDiffuseSampleDirIS(Vector3f N, Vector3f V)
	{
		return new Vector3f(0.0f, 0.0f, -1.0f);
	}
	
	@Override
	public Vector3f getF0()
	{
		return new Vector3f(0.0f, 0.0f, 0.0f);
	}

	@Override
	public float getRoughness()
	{
		return 0.0f;
	}
}
