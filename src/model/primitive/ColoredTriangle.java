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

import math.Vector3f;

public class ColoredTriangle extends Triangle
{
	protected Vector3f m_cA;
	protected Vector3f m_cB;
	protected Vector3f m_cC;
	
	// front facing: CCW vertex order
	public ColoredTriangle(Vector3f vA, Vector3f vB, Vector3f vC, Vector3f cA, Vector3f cB, Vector3f cC)
	{
		super(vA, vB, vC);
		
		m_cA = new Vector3f(cA);
		m_cB = new Vector3f(cB);
		m_cC = new Vector3f(cC);
	}
	
	public Vector3f getCa()
	{
		return m_cA;
	}
	
	public Vector3f getCb()
	{
		return m_cB;
	}
	
	public Vector3f getCc()
	{
		return m_cC;
	}

	@Override
	public Interpolator genInterpolator(Intersection intersection)
	{
		Vector3f localHitPoint = getModel().getTransform().getInverseModelMatrix().mul(intersection.getHitPoint(), 1.0f);
		
		// projected hit point
		float hitPu, hitPv;
		
		// projected side vector AB and AC
		float abPu, abPv, acPu, acPv;
					
		// find dominant axis
		if(Math.abs(m_normal.x) > Math.abs(m_normal.y))
		{
			// X dominant, projection plane is YZ
			if(Math.abs(m_normal.x) > Math.abs(m_normal.z))
			{
				hitPu = localHitPoint.y - m_vA.y;
				hitPv = localHitPoint.z - m_vA.z;
				abPu  = m_eAB.y;
				abPv  = m_eAB.z;
				acPu  = m_eAC.y;
				acPv  = m_eAC.z;
			}
			// Z dominant, projection plane is XY
			else
			{
				hitPu = localHitPoint.x - m_vA.x;
				hitPv = localHitPoint.y - m_vA.y;
				abPu  = m_eAB.x;
				abPv  = m_eAB.y;
				acPu  = m_eAC.x;
				acPv  = m_eAC.y;
			}
		}
		// Y dominant, projection plane is ZX
		else if(Math.abs(m_normal.y) > Math.abs(m_normal.z))
		{
			hitPu = localHitPoint.z - m_vA.z;
			hitPv = localHitPoint.x - m_vA.x;
			abPu  = m_eAB.z;
			abPv  = m_eAB.x;
			acPu  = m_eAC.z;
			acPv  = m_eAC.x;
		}
		// Z dominant, projection plane is XY
		else
		{
			hitPu = localHitPoint.x - m_vA.x;
			hitPv = localHitPoint.y - m_vA.y;
			abPu  = m_eAB.x;
			abPv  = m_eAB.y;
			acPu  = m_eAC.x;
			acPv  = m_eAC.y;
		}
		
		// TODO: check if this operation is possible of producing a NaN
		float multiplier = 1.0f / (abPu*acPv - abPv*acPu);
		
		// barycentric coordinate of vertex B in the projected plane
		float baryB = (hitPu*acPv - hitPv*acPu) * multiplier;
		
		// barycentric coordinate of vertex C in the projected plane
		float baryC = (hitPv*abPu - hitPu*abPv) * multiplier;
		
		return new ColoredTriangleInterpolator(this, 1.0f - baryB - baryC, baryB, baryC);
	}
}
