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

import math.Quaternion;
import math.Rand;
import math.Vector3f;

public class Camera
{
	private float      m_fov;
	private Vector3f   m_pos;
	private Vector3f   m_dir;
	
	public Camera()
	{
		setFovDeg(70.0f);
		m_pos = new Vector3f(0.0f, 0.0f, 0.0f);
		m_dir = new Vector3f(0.0f, 0.0f, -1.0f);
	}
	
	public Vector3f getPos()
	{
		return m_pos;
	}
	
	public Vector3f getDir()
	{
		return m_dir;
	}
	
	public void setPos(float x, float y, float z)
	{
		m_pos.set(x, y, z);
	}
	
	public void setDir(float x, float y, float z)
	{
		m_dir.set(x, y, z).normalizeLocal();
	}
	
	public void setFovDeg(float deg)
	{
		m_fov = deg / 180.0f * 3.14159265f;
	}
	
	public void calcRayTroughPixel(Ray ray, int xResPx, int yResPx, int xPx, int yPx)
	{
		// FIXME: this will fail when the camera is facing directly on y-axis
		
		Vector3f rightDir = new Vector3f(-m_dir.z, 0.0f, m_dir.x).normalizeLocal();
		Vector3f upDir    = rightDir.cross(m_dir).normalizeLocal();
		
		float halfWidth = (float)Math.tan(m_fov / 2.0f);
		float halfHeight = halfWidth * (float)yResPx / (float)xResPx;
		
		float pixelPosX = ((xPx + 0.5f) / (xResPx / 2.0f) - 1.0f) * halfWidth;
		float pixelPosY = ((yPx + 0.5f) / (yResPx / 2.0f) - 1.0f) * halfHeight;
		
		rightDir.mulLocal(pixelPosX);
		upDir.mulLocal(pixelPosY);
		
		ray.getDir().set(m_dir.add(rightDir.addLocal(upDir)).normalizeLocal());
		ray.getOrigin().set(m_pos);
	}
	
	public void calcRayThroughPixelDistributed(Ray ray, int xRes, int yRes, int xPx, int yPx)
	{
		// FIXME: this will fail when the camera is facing directly on y-axis
		// TODO: reuse rightDir & upDir
		
		Vector3f rightDir = new Vector3f(-m_dir.z, 0.0f, m_dir.x).normalizeLocal();
		Vector3f upDir    = rightDir.cross(m_dir).normalizeLocal();
		
		float halfWidth = (float)Math.tan(m_fov / 2.0f);
		float halfHeight = halfWidth * (float)yRes / (float)xRes;
		
		// Pixel coordinates on screen specify the lower-left corner of each pixel. To obtain
		// anti-aliasing effect, one must distribute ray directions randomly and uniformly on
		// each pixel's rectangular area.
		float pixelPosX = ((xPx + Rand.getFloat0_1()) / (xRes / 2.0f) - 1.0f) * halfWidth;
		float pixelPosY = ((yPx + Rand.getFloat0_1()) / (yRes / 2.0f) - 1.0f) * halfHeight;
		
		rightDir.mulLocal(pixelPosX);
		upDir.mulLocal(pixelPosY);
		
		ray.getDir().set(m_dir.add(rightDir.addLocal(upDir)).normalizeLocal());
		ray.getOrigin().set(m_pos);
	}
}
