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

package math;

public class Transform
{
	private Matrix4f m_modelMatrix;
	private Matrix4f m_inverseModelMatrix;
	
	private Matrix4f m_translationMatrix;
	private Matrix4f m_rotationMatrix;
	private Matrix4f m_scaleMatrix;
	
	public Transform()
	{
		m_modelMatrix        = new Matrix4f().initIdentity();
		m_inverseModelMatrix = new Matrix4f().initIdentity();
		
		m_translationMatrix = new Matrix4f().initIdentity();
		m_rotationMatrix    = new Matrix4f().initIdentity();
		m_scaleMatrix       = new Matrix4f().initIdentity();
	}
	
	public void update()
	{
		m_translationMatrix.mul(m_rotationMatrix.mul(m_scaleMatrix), m_modelMatrix);
		m_modelMatrix.inverse(m_inverseModelMatrix);
		
//		Debug.print(m_inverseModelMatrix);
	}
	
	public void setPos(float x, float y, float z)
	{
		m_translationMatrix.initTranslation(x, y, z);
		update();
	}
	
	public void setRot(Quaternion rot)
	{
		m_rotationMatrix.initRotation(rot);
		update();
	}
	
	public void setRotRad(Vector3f axis, float angle)
	{
		m_rotationMatrix.initRotation(new Quaternion().setRotRad(axis, angle));
		update();
	}
	
	public void setRotDeg(Vector3f axis, float angle)
	{
		m_rotationMatrix.initRotation(new Quaternion().setRotDeg(axis, angle));
		update();
	}
	
	public void setScale(float x, float y, float z)
	{
		m_scaleMatrix.initScale(x, y, z);
		update();
	}
	
	public void setScale(float uniformScale)
	{
		m_scaleMatrix.initScale(uniformScale, uniformScale, uniformScale);
		update();
	}
	
	public Matrix4f getModelMatrix()
	{
		return m_modelMatrix;
	}
	
	public Matrix4f getInverseModelMatrix()
	{
		return m_inverseModelMatrix;
	}
}
