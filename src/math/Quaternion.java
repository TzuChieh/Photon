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

public class Quaternion
{
	public static Quaternion TEMP = new Quaternion(0, 0, 0, 1);
	
	public float x;
	public float y;
	public float z;
	public float w;
	
	public Quaternion(Quaternion copyQuaternion)
	{
		this.x = copyQuaternion.x;
		this.y = copyQuaternion.y;
		this.z = copyQuaternion.z;
		this.w = copyQuaternion.w;
	}

	public Quaternion(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Quaternion()
	{
		this(0.0f, 0.0f, 0.0f, 0.0f);
	}

	public Quaternion(Vector3f axis, float angle)
	{
		float sinHalfAngle = (float)Math.sin(angle / 2);
		float cosHalfAngle = (float)Math.cos(angle / 2);

		this.x = axis.getX() * sinHalfAngle;
		this.y = axis.getY() * sinHalfAngle;
		this.z = axis.getZ() * sinHalfAngle;
		this.w = cosHalfAngle;
	}
	
	public Quaternion setRotRad(Vector3f axis, float angle)
	{
		float sinHalfAngle = (float)Math.sin(angle / 2);
		float cosHalfAngle = (float)Math.cos(angle / 2);

		this.x = axis.getX() * sinHalfAngle;
		this.y = axis.getY() * sinHalfAngle;
		this.z = axis.getZ() * sinHalfAngle;
		this.w = cosHalfAngle;
		
		return this;
	}
	
	public Quaternion setRotDeg(Vector3f axis, float angle)
	{
		return setRotRad(axis, angle / 180.0f * 3.14159265f);
	}
	
	/****************************************************/
	// normalize
	public Quaternion normalize()
	{
		float length = length();
		
		return new Quaternion(x / length, y / length, z / length, w / length);
	}
	
	public Quaternion normalizeLocal()
	{
		float length = length();
		
		x /= length;
		y /= length;
		z /= length;
		w /= length;
		
		return this;
	}

	public float length()
	{
		return (float)Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	/****************************************************/
	// conjugate
	public Quaternion conjugate()
	{
		return new Quaternion(-x, -y, -z, w);
	}
	
	public Quaternion conjugate(Quaternion result)
	{
		result.x = -x;
		result.y = -y;
		result.z = -z;
		result.w = w;
		
		return result;
	}
	
	public Quaternion conjugateLocal()
	{
		x *= -1;
		y *= -1;
		z *= -1;
		
		return this;
	}

	/****************************************************/
	// mul
	public Quaternion mul(Quaternion r)
	{
		float _w = w * r.w - x * r.x - y * r.y - z * r.z;
		float _x = w * r.x + x * r.w + y * r.z - z * r.y;
		float _y = w * r.y - x * r.z + y * r.w + z * r.x;
		float _z = w * r.z + x * r.y - y * r.x + z * r.w;
		
		return new Quaternion(_x, _y, _z, _w);
	}
	
	public Quaternion mul(Quaternion r, Quaternion result)
	{
		result.w = w * r.w - x * r.x - y * r.y - z * r.z;
		result.x = w * r.x + x * r.w + y * r.z - z * r.y;
		result.y = w * r.y - x * r.z + y * r.w + z * r.x;
		result.z = w * r.z + x * r.y - y * r.x + z * r.w;
		
		return result;
	}
	
	public Quaternion mul(Vector3f r)
	{
		float _w = -x * r.getX() - y * r.getY() - z * r.getZ();
		float _x =  w * r.getX() + y * r.getZ() - z * r.getY();
		float _y =  w * r.getY() - x * r.getZ() + z * r.getX();
		float _z =  w * r.getZ() + x * r.getY() - y * r.getX();
		
		return new Quaternion(_x, _y, _z, _w);
	}
	
	public Quaternion mulLocal(Quaternion r)
	{
		float _w = w * r.w - x * r.x - y * r.y - z * r.z;
		float _x = w * r.x + x * r.w + y * r.z - z * r.y;
		float _y = w * r.y - x * r.z + y * r.w + z * r.x;
		float _z = w * r.z + x * r.y - y * r.x + z * r.w;
		
		x = _x;
		y = _y;
		z = _z;
		w = _w;
		
		return this;
	}
	
	public Quaternion mulLocal(Vector3f vec3f)
	{
		float _w = -x * vec3f.getX() - y * vec3f.getY() - z * vec3f.getZ();
		float _x =  w * vec3f.getX() + y * vec3f.getZ() - z * vec3f.getY();
		float _y =  w * vec3f.getY() - x * vec3f.getZ() + z * vec3f.getX();
		float _z =  w * vec3f.getZ() + x * vec3f.getY() - y * vec3f.getX();
		
		x = _x;
		y = _y;
		z = _z;
		w = _w;
		
		return this;
	}
	
	public Quaternion mul(float r)
	{
		return new Quaternion(x * r, y * r, z * r, w * r);
	}
	/****************************************************/

	public Quaternion sub(Quaternion r)
	{
		return new Quaternion(x - r.getX(), y - r.getY(), z - r.getZ(), w - r.getW());
	}

	public Quaternion add(Quaternion r)
	{
		return new Quaternion(x + r.getX(), y + r.getY(), z + r.getZ(), w + r.getW());
	}

	public Matrix4f toRotationMatrix()
	{
		Vector3f dirX = new Vector3f(1.0f - 2.0f*(y*y + z*z), 2.0f*(x*y - z*w)       , 2.0f*(y*w + x*z));
		Vector3f dirY = new Vector3f(2.0f*(x*y + z*w)       , 1.0f - 2.0f*(x*x + z*z), 2.0f*(y*z - x*w));
		Vector3f dirZ = new Vector3f(2.0f*(x*z - y*w)       , 2.0f*(y*z + x*w)       , 1.0f - 2.0f*(x*x + y*y));

		return new Matrix4f().initRotation(dirZ.negatived(), dirY, dirX);
	}
	
	public Matrix4f toRotationMatrix(Matrix4f result)
	{
		float[][] element = result.getM();
		
		element[0][0] = 1.0f - 2.0f*(y*y + z*z);
		element[0][1] = 2.0f*(x*y - z*w);
		element[0][2] = 2.0f*(y*w + x*z);
		element[0][3] = 0.0f;
		
		element[1][0] = 2.0f*(x*y + z*w);
		element[1][1] = 1.0f - 2.0f*(x*x + z*z);
		element[1][2] = 2.0f*(y*z - x*w);
		element[1][3] = 0.0f;
		
		element[2][0] = 2.0f*(x*z - y*w);
		element[2][1] = 2.0f*(y*z + x*w);
		element[2][2] = 1.0f - 2.0f*(x*x + y*y);
		element[2][3] = 0.0f;
		
		element[3][0] = 0.0f;
		element[3][1] = 0.0f;
		element[3][2] = 0.0f;
		element[3][3] = 1.0f;

		return result;
	}

	public float dot(Quaternion r)
	{
		return x * r.getX() + y * r.getY() + z * r.getZ() + w * r.getW();
	}

	public Quaternion nlerp(Quaternion dest, float lerpFactor, boolean shortest)
	{
		Quaternion correctedDest = dest;

		if(shortest && this.dot(dest) < 0)
			correctedDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), -dest.getW());

		return correctedDest.sub(this).mul(lerpFactor).add(this).normalize();
	}

	public Quaternion slerp(Quaternion dest, float lerpFactor, boolean shortest)
	{
		final float EPSILON = 1e3f;

		float cos = this.dot(dest);
		Quaternion correctedDest = dest;

		if(shortest && cos < 0)
		{
			cos = -cos;
			correctedDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), -dest.getW());
		}

		if(Math.abs(cos) >= 1 - EPSILON)
			return nlerp(correctedDest, lerpFactor, false);

		float sin = (float)Math.sqrt(1.0f - cos * cos);
		float angle = (float)Math.atan2(sin, cos);
		float invSin =  1.0f/sin;

		float srcFactor = (float)Math.sin((1.0f - lerpFactor) * angle) * invSin;
		float destFactor = (float)Math.sin((lerpFactor) * angle) * invSin;

		return this.mul(srcFactor).add(correctedDest.mul(destFactor));
	}

	public Quaternion(Matrix4f rot)
	{
		float trace = rot.get(0, 0) + rot.get(1, 1) + rot.get(2, 2);

		if(trace > 0)
		{
			float s = 0.5f / (float)Math.sqrt(trace+ 1.0f);
			w = 0.25f / s;
			x = (rot.get(1, 2) - rot.get(2, 1)) * s;
			y = (rot.get(2, 0) - rot.get(0, 2)) * s;
			z = (rot.get(0, 1) - rot.get(1, 0)) * s;
		}
		else
		{
			if(rot.get(0, 0) > rot.get(1, 1) && rot.get(0, 0) > rot.get(2, 2))
			{
				float s = 2.0f * (float)Math.sqrt(1.0f + rot.get(0, 0) - rot.get(1, 1) - rot.get(2, 2));
				w = (rot.get(1, 2) - rot.get(2, 1)) / s;
				x = 0.25f * s;
				y = (rot.get(1, 0) + rot.get(0, 1)) / s;
				z = (rot.get(2, 0) + rot.get(0, 2)) / s;
			}
			else if(rot.get(1, 1) > rot.get(2, 2))
			{
				float s = 2.0f * (float)Math.sqrt(1.0f + rot.get(1, 1) - rot.get(0, 0) - rot.get(2, 2));
				w = (rot.get(2, 0) - rot.get(0, 2)) / s;
				x = (rot.get(1, 0) + rot.get(0, 1)) / s;
				y = 0.25f * s;
				z = (rot.get(2, 1) + rot.get(1, 2)) / s;
			}
			else
			{
				float s = 2.0f * (float)Math.sqrt(1.0f + rot.get(2, 2) - rot.get(0, 0) - rot.get(1, 1));
				w = (rot.get(0, 1) - rot.get(1, 0) ) / s;
				x = (rot.get(2, 0) + rot.get(0, 2) ) / s;
				y = (rot.get(1, 2) + rot.get(2, 1) ) / s;
				z = 0.25f * s;
			}
		}

		float length = (float)Math.sqrt(x*x + y*y + z*z +w*w);
		x /= length;
		y /= length;
		z /= length;
		w /= length;
	}

	public Vector3f getForward()
	{
		return Vector3f.UNIT_NEGATIVE_Z_AXIS.rotate(this);
	}

	public Vector3f getBack()
	{
		return Vector3f.UNIT_Z_AXIS.rotate(this);
	}

	public Vector3f getUp()
	{
		return Vector3f.UNIT_Y_AXIS.rotate(this);
	}

	public Vector3f getDown()
	{
		return Vector3f.UNIT_NEGATIVE_Y_AXIS.rotate(this);
	}

	public Vector3f getRight()
	{
		return Vector3f.UNIT_X_AXIS.rotate(this);
	}

	public Vector3f getLeft()
	{
		return Vector3f.UNIT_NEGATIVE_X_AXIS.rotate(this);
	}

	public Quaternion set(float x, float y, float z, float w) { this.x = x; this.y = y; this.z = z; this.w = w; return this; }
	public Quaternion set(Quaternion r) { set(r.getX(), r.getY(), r.getZ(), r.getW()); return this; }
	public float getX(){return x;}
	public void setX(float x){this.x = x;}
	public float getY(){return y;}
	public void setY(float y){this.y = y;}
	public float getZ(){return z;}
	public void setZ(float z){this.z = z;}
	public float getW(){return w;}
	public void setW(float w){this.w = w;}
	public boolean equals(Quaternion r)
	{
		return x == r.getX() && y == r.getY() && z == r.getZ() && w == r.getW();
	}
	
	@Override
	public String toString()
	{
//		DecimalFormat df = new DecimalFormat("000.00");
//		return "x: " + df.format(x) + " y: " + df.format(y) + " z: " + df.format(z) + " w: " + df.format(w);
		return "(" + x + ", " + y + ", " + z + ", " + w + ")";
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this)
            return true;
		
        if(obj == null)
            return false;
        
        if(getClass() != obj.getClass())
            return false;
        
        final Quaternion other = (Quaternion)obj;
		
		return x == other.x && 
			   y == other.y &&
			   z == other.z &&
			   w == other.w;
	}
	
	@Override
	public int hashCode()
	{
		int result = 7;

		result = 37 * result + Float.floatToIntBits(x);
		result = 37 * result + Float.floatToIntBits(y);
		result = 37 * result + Float.floatToIntBits(z);
		result = 37 * result + Float.floatToIntBits(w);
		
		return result;
	}
}
