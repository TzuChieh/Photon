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

package model.primitive;

import math.Matrix4f;
import math.Vector3f;

public class VbTriangleInterpolator implements Interpolator
{
	private VbTriangle m_triangle;
	
	private float m_baryA;
	private float m_baryB;
	private float m_baryC;
	
	public VbTriangleInterpolator(VbTriangle triangle, float baryA, float baryB, float baryC)
	{
		m_triangle = triangle;
		
		m_baryA = baryA;
		m_baryB = baryB;
		m_baryC = baryC;
	}
	
	@Override
	public Vector3f getFlatNormal()
	{
		Matrix4f modelMatrix = m_triangle.getModel().getTransform().getModelMatrix();
		
		return modelMatrix.mul(m_triangle.getNormal(), 0.0f).normalizeLocal();
	}

	@Override
	public Vector3f getSmoothNormal()
	{
		Matrix4f modelMatrix = m_triangle.getModel().getTransform().getModelMatrix();
		
		Vector3f nA = m_triangle.getNa();
		Vector3f nB = m_triangle.getNb();
		Vector3f nC = m_triangle.getNc();
		
		Vector3f smoothN = new Vector3f(nA.x * m_baryA + nB.x * m_baryB + nC.x * m_baryC,
				                        nA.y * m_baryA + nB.y * m_baryB + nC.y * m_baryC,
				                        nA.z * m_baryA + nB.z * m_baryB + nC.z * m_baryC);
		
		return modelMatrix.mul(smoothN, 0.0f).normalizeLocal();
	}

	@Override
	public Vector3f getSmoothColor()
	{
		return new Vector3f(0, 0, 0);
	}
}
