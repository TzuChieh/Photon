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
import math.Vector2f;
import math.Vector3f;

public class ColoredTriangleInterpolator implements Interpolator
{
	private ColoredTriangle m_coloredTriangle;
	
	private float m_baryA;
	private float m_baryB;
	private float m_baryC;
	
	public ColoredTriangleInterpolator(ColoredTriangle coloredTriangle, float baryA, float baryB, float baryC)
	{
		m_coloredTriangle = coloredTriangle;
		
		m_baryA = baryA;
		m_baryB = baryB;
		m_baryC = baryC;
	}
	
	@Override
	public Vector3f getFlatNormal()
	{
		Matrix4f modelMatrix = m_coloredTriangle.getModel().getTransform().getModelMatrix();
		
		return modelMatrix.mul(m_coloredTriangle.getNormal(), 0.0f).normalizeLocal();
	}

	@Override
	public Vector3f getSmoothNormal()
	{
		Matrix4f modelMatrix = m_coloredTriangle.getModel().getTransform().getModelMatrix();
		
		Vector3f nA = m_coloredTriangle.getNa();
		Vector3f nB = m_coloredTriangle.getNb();
		Vector3f nC = m_coloredTriangle.getNc();
		
		Vector3f smoothN = new Vector3f(nA.x * m_baryA + nB.x * m_baryB + nC.x * m_baryC,
				                        nA.y * m_baryA + nB.y * m_baryB + nC.y * m_baryC,
				                        nA.z * m_baryA + nB.z * m_baryB + nC.z * m_baryC);
		
		return modelMatrix.mul(smoothN, 0.0f).normalizeLocal();
	}

	@Override
	public Vector3f getSmoothColor()
	{
		Vector3f cA = m_coloredTriangle.getCa();
		Vector3f cB = m_coloredTriangle.getCb();
		Vector3f cC = m_coloredTriangle.getCc();
		
		Vector3f smoothC = new Vector3f(cA.x * m_baryA + cB.x * m_baryB + cC.x * m_baryC,
				                        cA.y * m_baryA + cB.y * m_baryB + cC.y * m_baryC,
				                        cA.z * m_baryA + cB.z * m_baryB + cC.z * m_baryC);
		
		return smoothC;
	}

	@Override
	public Vector2f getSmoothTexCoord()
	{
		Vector2f txA = m_coloredTriangle.getTxA();
		Vector2f txB = m_coloredTriangle.getTxB();
		Vector2f txC = m_coloredTriangle.getTxC();
		
		Vector2f smoothTx = new Vector2f(txA.x * m_baryA + txB.x * m_baryB + txC.x * m_baryC,
				                         txA.y * m_baryA + txB.y * m_baryB + txC.y * m_baryC);
		
		return smoothTx;
	}
}
