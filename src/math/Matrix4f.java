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

public class Matrix4f
{
	// TODO: learn more about threadlocal
	
	private static ThreadLocal<Matrix4f> TEMP = new ThreadLocal<Matrix4f>() 
	{
		@Override
		public Matrix4f initialValue()
		{
            return new Matrix4f().initIdentity();
        }
	};
	
	public float[][] m;
	
	public Matrix4f(Matrix4f copyMatrix4f)
	{
		m = new float[4][4];
		
		m[0][0] = copyMatrix4f.m[0][0];
		m[0][1] = copyMatrix4f.m[0][1];
		m[0][2] = copyMatrix4f.m[0][2];
		m[0][3] = copyMatrix4f.m[0][3];
		
		m[1][0] = copyMatrix4f.m[1][0];
		m[1][1] = copyMatrix4f.m[1][1];
		m[1][2] = copyMatrix4f.m[1][2];
		m[1][3] = copyMatrix4f.m[1][3];
		
		m[2][0] = copyMatrix4f.m[2][0];
		m[2][1] = copyMatrix4f.m[2][1];
		m[2][2] = copyMatrix4f.m[2][2];
		m[2][3] = copyMatrix4f.m[2][3];
		
		m[3][0] = copyMatrix4f.m[3][0];
		m[3][1] = copyMatrix4f.m[3][1];
		m[3][2] = copyMatrix4f.m[3][2];
		m[3][3] = copyMatrix4f.m[3][3];
	}
	
	public Matrix4f()
	{
		m = new float[4][4];
	}

	public Matrix4f initIdentity()
	{
		m[0][0] = 1;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;	m[1][1] = 1;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;

		return this;
	}
	
	public Matrix4f initTranslation(float x, float y, float z)
	{
		m[0][0] = 1;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = x;
		m[1][0] = 0;	m[1][1] = 1;	m[1][2] = 0;	m[1][3] = y;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = z;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initTranslation(Vector3f pos)
	{
		initTranslation(pos.x, pos.y, pos.z);
		
		return this;
	}
	
	public Matrix4f initScale(float x, float y, float z)
	{
		m[0][0] = x;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;	m[1][1] = y;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = z;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initScale(Vector3f scale)
	{
		m[0][0] = scale.getX();	m[0][1] = 0;	        m[0][2] = 0;	        m[0][3] = 0;
		m[1][0] = 0;	        m[1][1] = scale.getY();	m[1][2] = 0;	        m[1][3] = 0;
		m[2][0] = 0;	        m[2][1] = 0;	        m[2][2] = scale.getZ();	m[2][3] = 0;
		m[3][0] = 0;	        m[3][1] = 0;	        m[3][2] = 0;	        m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initPerspective(float fov, float aspectRatio, float zNear, float zFar)
	{
		float tanHalfFOV = (float)Math.tan(fov / 2);
		float zRange = zNear - zFar;
		
		m[0][0] = 1.0f / tanHalfFOV; m[0][1] = 0;					m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;				 m[1][1] = aspectRatio / tanHalfFOV;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;				 m[2][1] = 0;					m[2][2] = (zNear +zFar)/zRange;	m[2][3] = 2 * zFar * zNear / zRange;
		m[3][0] = 0;				 m[3][1] = 0;					m[3][2] = -1.0f;	m[3][3] = 0;
		
		
		return this;
	}

	public Matrix4f initOrthographic(float left, float right, float bottom, float top, float near, float far)
	{
		float width = right - left;
		float height = top - bottom;
		float depth = far - near;

		m[0][0] = 2 / width;	m[0][1] = 0;			m[0][2] = 0;			m[0][3] = -(right + left) / width;
		m[1][0] = 0;			m[1][1] = 2 / height;	m[1][2] = 0;			m[1][3] = -(top + bottom) / height;
		m[2][0] = 0;			m[2][1] = 0;			m[2][2] = -2 / depth;	m[2][3] = -(far + near) / depth;
		m[3][0] = 0;			m[3][1] = 0;			m[3][2] = 0;			m[3][3] = 1;

		return this;
	}

	public Matrix4f initRotation(Vector3f forward, Vector3f up)
	{
		Vector3f f = forward.normalize();
		
		Vector3f r = up.normalize();
		r = f.cross(r);
		
		Vector3f u = r.cross(f);

		return initRotation(f, u, r);
	}

	public Matrix4f initRotation(Vector3f forward, Vector3f up, Vector3f right)
	{
		Vector3f f = forward;
		Vector3f r = right;
		Vector3f u = up;

		m[0][0] = r.getX();	m[0][1] = r.getY(); m[0][2] = r.getZ(); m[0][3] = 0;
		m[1][0] = u.getX();	m[1][1] = u.getY(); m[1][2] = u.getZ(); m[1][3] = 0;
		m[2][0] = -f.getX(); m[2][1] = -f.getY(); m[2][2] = -f.getZ(); m[2][3] = 0;
		m[3][0] = 0;		m[3][1] = 0;		m[3][2] = 0;		m[3][3] = 1;

		return this;
	}
	
	public Matrix4f initRotation(Quaternion var)
	{
		float x = var.getX();
		float y = var.getY();
		float z = var.getZ();
		float w = var.getW();

		m[0][0] = 1.0f - 2.0f*(y*y + z*z); m[0][1] = 2.0f*(x*y - z*w);        m[0][2] = 2.0f*(y*w + x*z);         m[0][3] = 0;
		m[1][0] = 2.0f*(x*y + z*w);	       m[1][1] = 1.0f - 2.0f*(x*x + z*z); m[1][2] = 2.0f*(y*z - x*w);         m[1][3] = 0;
		m[2][0] = 2.0f*(x*z - y*w);        m[2][1] = 2.0f*(y*z + x*w);        m[2][2] =  1.0f - 2.0f*(x*x + y*y); m[2][3] = 0;
		m[3][0] = 0;		               m[3][1] = 0;		                  m[3][2] = 0;		                  m[3][3] = 1;

		return this;
	}

	/****************************************************/
	// mul
	public Matrix4f mul(Matrix4f r)
	{
		Matrix4f res = new Matrix4f();
		
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				res.set(i, j, m[i][0] * r.get(0, j) +
							  m[i][1] * r.get(1, j) +
							  m[i][2] * r.get(2, j) +
							  m[i][3] * r.get(3, j));
			}
		}
		
		return res;
	}
	
	public Matrix4f mul(Matrix4f var, Matrix4f result)
	{
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				result.m[i][j] =   m[i][0] * var.m[0][j]
								 + m[i][1] * var.m[1][j]
								 + m[i][2] * var.m[2][j]
								 + m[i][3] * var.m[3][j];
			}
		}
		
		return result;
	}
	
	public Matrix4f mulLocal(Matrix4f var)
	{
		// FIXME: do this without if statements!
		
		Matrix4f temp = TEMP.get();
		
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				temp.m[i][j] = m[i][0] * var.m[0][j]
							 + m[i][1] * var.m[1][j]
							 + m[i][2] * var.m[2][j]
						     + m[i][3] * var.m[3][j];
			}
		}
		
		m[0][0] = temp.m[0][0];
		m[0][1] = temp.m[0][1];
		m[0][2] = temp.m[0][2];
		m[0][3] = temp.m[0][3];
		
		m[1][0] = temp.m[1][0];
		m[1][1] = temp.m[1][1];
		m[1][2] = temp.m[1][2];
		m[1][3] = temp.m[1][3];
		
		m[2][0] = temp.m[2][0];
		m[2][1] = temp.m[2][1];
		m[2][2] = temp.m[2][2];
		m[2][3] = temp.m[2][3];
		
		m[3][0] = temp.m[3][0];
		m[3][1] = temp.m[3][1];
		m[3][2] = temp.m[3][2];
		m[3][3] = temp.m[3][3];
		
		return this;
	}
	
	public Matrix4f mulLocal(float var)
	{
		m[0][0] *= var;
		m[0][1] *= var;
		m[0][2] *= var;
		m[0][3] *= var;
		
		m[1][0] *= var;
		m[1][1] *= var;
		m[1][2] *= var;
		m[1][3] *= var;
		
		m[2][0] *= var;
		m[2][1] *= var;
		m[2][2] *= var;
		m[2][3] *= var;
		
		m[3][0] *= var;
		m[3][1] *= var;
		m[3][2] *= var;
		m[3][3] *= var;
		
		return this;
	}
	
	public Vector3f mul(Vector3f r, float w)
	{
		return new Vector3f(m[0][0] * r.getX() + m[0][1] * r.getY() + m[0][2] * r.getZ() + m[0][3] * w,
		                    m[1][0] * r.getX() + m[1][1] * r.getY() + m[1][2] * r.getZ() + m[1][3] * w,
		                    m[2][0] * r.getX() + m[2][1] * r.getY() + m[2][2] * r.getZ() + m[2][3] * w);
	}
	
	public Vector3f mul(Vector3f r, float w, Vector3f result)
	{
		result.x = m[0][0] * r.getX() + m[0][1] * r.getY() + m[0][2] * r.getZ() + m[0][3] * w;
		result.y = m[1][0] * r.getX() + m[1][1] * r.getY() + m[1][2] * r.getZ() + m[1][3] * w;
		result.z = m[2][0] * r.getX() + m[2][1] * r.getY() + m[2][2] * r.getZ() + m[2][3] * w;
		
		return result;
	}
	
	public Quaternion mul(Quaternion r)
	{
		return new Quaternion(m[0][0] * r.getX() + m[0][1] * r.getY() + m[0][2] * r.getZ() + m[0][3] * r.getW(),
		                      m[1][0] * r.getX() + m[1][1] * r.getY() + m[1][2] * r.getZ() + m[1][3] * r.getW(),
		                      m[2][0] * r.getX() + m[2][1] * r.getY() + m[2][2] * r.getZ() + m[2][3] * r.getW(),
		                      m[3][0] * r.getX() + m[3][1] * r.getY() + m[3][2] * r.getZ() + m[3][3] * r.getW());
	}
	
	public Matrix4f inverse()
	{
		return inverse(new Matrix4f());
	}
	
	public Matrix4f inverse(Matrix4f result)
	{
		result.m[0][0] = m[1][2]*m[2][3]*m[3][1] - m[1][3]*m[2][2]*m[3][1] + m[1][3]*m[2][1]*m[3][2] 
				       - m[1][1]*m[2][3]*m[3][2] - m[1][2]*m[2][1]*m[3][3] + m[1][1]*m[2][2]*m[3][3];
		result.m[0][1] = m[0][3]*m[2][2]*m[3][1] - m[0][2]*m[2][3]*m[3][1] - m[0][3]*m[2][1]*m[3][2] 
				       + m[0][1]*m[2][3]*m[3][2] + m[0][2]*m[2][1]*m[3][3] - m[0][1]*m[2][2]*m[3][3];
		result.m[0][2] = m[0][2]*m[1][3]*m[3][1] - m[0][3]*m[1][2]*m[3][1] + m[0][3]*m[1][1]*m[3][2] 
				       - m[0][1]*m[1][3]*m[3][2] - m[0][2]*m[1][1]*m[3][3] + m[0][1]*m[1][2]*m[3][3];
		result.m[0][3] = m[0][3]*m[1][2]*m[2][1] - m[0][2]*m[1][3]*m[2][1] - m[0][3]*m[1][1]*m[2][2] 
				       + m[0][1]*m[1][3]*m[2][2] + m[0][2]*m[1][1]*m[2][3] - m[0][1]*m[1][2]*m[2][3];
		
		result.m[1][0] = m[1][3]*m[2][2]*m[3][0] - m[1][2]*m[2][3]*m[3][0] - m[1][3]*m[2][0]*m[3][2] 
				       + m[1][0]*m[2][3]*m[3][2] + m[1][2]*m[2][0]*m[3][3] - m[1][0]*m[2][2]*m[3][3];
		result.m[1][1] = m[0][2]*m[2][3]*m[3][0] - m[0][3]*m[2][2]*m[3][0] + m[0][3]*m[2][0]*m[3][2]
				       - m[0][0]*m[2][3]*m[3][2] - m[0][2]*m[2][0]*m[3][3] + m[0][0]*m[2][2]*m[3][3];
		result.m[1][2] = m[0][3]*m[1][2]*m[3][0] - m[0][2]*m[1][3]*m[3][0] - m[0][3]*m[1][0]*m[3][2] 
				       + m[0][0]*m[1][3]*m[3][2] + m[0][2]*m[1][0]*m[3][3] - m[0][0]*m[1][2]*m[3][3];
		result.m[1][3] = m[0][2]*m[1][3]*m[2][0] - m[0][3]*m[1][2]*m[2][0] + m[0][3]*m[1][0]*m[2][2] 
				       - m[0][0]*m[1][3]*m[2][2] - m[0][2]*m[1][0]*m[2][3] + m[0][0]*m[1][2]*m[2][3];
	    
		result.m[2][0] = m[1][1]*m[2][3]*m[3][0] - m[1][3]*m[2][1]*m[3][0] + m[1][3]*m[2][0]*m[3][1]
				       - m[1][0]*m[2][3]*m[3][1] - m[1][1]*m[2][0]*m[3][3] + m[1][0]*m[2][1]*m[3][3];
		result.m[2][1] = m[0][3]*m[2][1]*m[3][0] - m[0][1]*m[2][3]*m[3][0] - m[0][3]*m[2][0]*m[3][1]
				       + m[0][0]*m[2][3]*m[3][1] + m[0][1]*m[2][0]*m[3][3] - m[0][0]*m[2][1]*m[3][3];
		result.m[2][2] = m[0][1]*m[1][3]*m[3][0] - m[0][3]*m[1][1]*m[3][0] + m[0][3]*m[1][0]*m[3][1]
				       - m[0][0]*m[1][3]*m[3][1] - m[0][1]*m[1][0]*m[3][3] + m[0][0]*m[1][1]*m[3][3];
		result.m[2][3] = m[0][3]*m[1][1]*m[2][0] - m[0][1]*m[1][3]*m[2][0] - m[0][3]*m[1][0]*m[2][1]
				       + m[0][0]*m[1][3]*m[2][1] + m[0][1]*m[1][0]*m[2][3] - m[0][0]*m[1][1]*m[2][3];
	   
		result.m[3][0] = m[1][2]*m[2][1]*m[3][0] - m[1][1]*m[2][2]*m[3][0] - m[1][2]*m[2][0]*m[3][1]
				       + m[1][0]*m[2][2]*m[3][1] + m[1][1]*m[2][0]*m[3][2] - m[1][0]*m[2][1]*m[3][2];
		result.m[3][1] = m[0][1]*m[2][2]*m[3][0] - m[0][2]*m[2][1]*m[3][0] + m[0][2]*m[2][0]*m[3][1]
				       - m[0][0]*m[2][2]*m[3][1] - m[0][1]*m[2][0]*m[3][2] + m[0][0]*m[2][1]*m[3][2];
		result.m[3][2] = m[0][2]*m[1][1]*m[3][0] - m[0][1]*m[1][2]*m[3][0] - m[0][2]*m[1][0]*m[3][1]
				       + m[0][0]*m[1][2]*m[3][1] + m[0][1]*m[1][0]*m[3][2] - m[0][0]*m[1][1]*m[3][2];
		result.m[3][3] = m[0][1]*m[1][2]*m[2][0] - m[0][2]*m[1][1]*m[2][0] + m[0][2]*m[1][0]*m[2][1]
				       - m[0][0]*m[1][2]*m[2][1] - m[0][1]*m[1][0]*m[2][2] + m[0][0]*m[1][1]*m[2][2];
		
		return result.mulLocal(1.0f / determinant());
	}
	
	public float determinant()
	{
		float value;
		
		value = m[0][3]*m[1][2]*m[2][1]*m[3][0] - m[0][2]*m[1][3]*m[2][1]*m[3][0] 
			  - m[0][3]*m[1][1]*m[2][2]*m[3][0] + m[0][1]*m[1][3]*m[2][2]*m[3][0]
			  + m[0][2]*m[1][1]*m[2][3]*m[3][0] - m[0][1]*m[1][2]*m[2][3]*m[3][0]
			  - m[0][3]*m[1][2]*m[2][0]*m[3][1] + m[0][2]*m[1][3]*m[2][0]*m[3][1]
			  + m[0][3]*m[1][0]*m[2][2]*m[3][1] - m[0][0]*m[1][3]*m[2][2]*m[3][1] 
		      - m[0][2]*m[1][0]*m[2][3]*m[3][1] + m[0][0]*m[1][2]*m[2][3]*m[3][1]
		      + m[0][3]*m[1][1]*m[2][0]*m[3][2] - m[0][1]*m[1][3]*m[2][0]*m[3][2]
		      - m[0][3]*m[1][0]*m[2][1]*m[3][2] + m[0][0]*m[1][3]*m[2][1]*m[3][2]
		      + m[0][1]*m[1][0]*m[2][3]*m[3][2] - m[0][0]*m[1][1]*m[2][3]*m[3][2]
		      - m[0][2]*m[1][1]*m[2][0]*m[3][3] + m[0][1]*m[1][2]*m[2][0]*m[3][3]
		      + m[0][2]*m[1][0]*m[2][1]*m[3][3] - m[0][0]*m[1][2]*m[2][1]*m[3][3]
		      - m[0][1]*m[1][0]*m[2][2]*m[3][3] + m[0][0]*m[1][1]*m[2][2]*m[3][3];
		
		return value;
	}
	
	public float[][] getM()
	{
		return this.m;
	}
	
	public float get(int x, int y)
	{
		return m[x][y];
	}

	public void setM(float[][] m)
	{
		this.m = m;
	}
	
	public void set(int x, int y, float value)
	{
		m[x][y] = value;
	}
	
	public void set(Matrix4f var)
	{
		m[0][0] = var.m[0][0]; m[0][1] = var.m[0][1]; m[0][2] = var.m[0][2]; m[0][3] = var.m[0][3];
		m[1][0] = var.m[1][0]; m[1][1] = var.m[1][1]; m[1][2] = var.m[1][2]; m[1][3] = var.m[1][3];
		m[2][0] = var.m[2][0]; m[2][1] = var.m[2][1]; m[2][2] = var.m[2][2]; m[2][3] = var.m[2][3];
		m[3][0] = var.m[3][0]; m[3][1] = var.m[3][1]; m[3][2] = var.m[3][2]; m[3][3] = var.m[3][3];
	}
	
	@Override
	public String toString()
	{
		return "[" + m[0][0] + "][" + m[0][1] + "][" + m[0][2] + "][" + m[0][3] + "] \n"
			 + "[" + m[1][0] + "][" + m[1][1] + "][" + m[1][2] + "][" + m[1][3] + "] \n"
			 + "[" + m[2][0] + "][" + m[2][1] + "][" + m[2][2] + "][" + m[2][3] + "] \n"
			 + "[" + m[3][0] + "][" + m[3][1] + "][" + m[3][2] + "][" + m[3][3] + "]";
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
        
        final Matrix4f other = (Matrix4f)obj;
        
        return m[0][0] == other.m[0][0] &&
		       m[0][1] == other.m[0][1] &&
		       m[0][2] == other.m[0][2] &&
		       m[0][3] == other.m[0][3] &&
		       m[1][0] == other.m[1][0] &&
		       m[1][1] == other.m[1][1] &&
		       m[1][2] == other.m[1][2] &&
		       m[1][3] == other.m[1][3] &&
		       m[2][0] == other.m[2][0] &&
		       m[2][1] == other.m[2][1] &&
		       m[2][2] == other.m[2][2] &&
		       m[2][3] == other.m[2][3] &&
		       m[3][0] == other.m[3][0] &&
		       m[3][1] == other.m[3][1] &&
		       m[3][2] == other.m[3][2] &&
		       m[3][3] == other.m[3][3];
	}
	
	@Override
	public int hashCode()
	{
		int result = 7;

		result = 37 * result + Float.floatToIntBits(m[0][0]);
		result = 37 * result + Float.floatToIntBits(m[0][1]);
		result = 37 * result + Float.floatToIntBits(m[0][2]);
		result = 37 * result + Float.floatToIntBits(m[0][3]);
		result = 37 * result + Float.floatToIntBits(m[1][0]);
		result = 37 * result + Float.floatToIntBits(m[1][1]);
		result = 37 * result + Float.floatToIntBits(m[1][2]);
		result = 37 * result + Float.floatToIntBits(m[1][3]);
		result = 37 * result + Float.floatToIntBits(m[2][0]);
		result = 37 * result + Float.floatToIntBits(m[2][1]);
		result = 37 * result + Float.floatToIntBits(m[2][2]);
		result = 37 * result + Float.floatToIntBits(m[2][3]);
		result = 37 * result + Float.floatToIntBits(m[3][0]);
		result = 37 * result + Float.floatToIntBits(m[3][1]);
		result = 37 * result + Float.floatToIntBits(m[3][2]);
		result = 37 * result + Float.floatToIntBits(m[3][3]);
		
		return result;
	}
}
