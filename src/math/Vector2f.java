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

public class Vector2f
{
	public float x;
	public float y;
	
	public Vector2f(Vector2f copyVector2f)
	{
		this.x = copyVector2f.x;
		this.y = copyVector2f.y;
	}
	
	public Vector2f(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2f()
	{
		this(0, 0);
	}

	public float length()
	{
		return (float)Math.sqrt(x * x + y * y);
	}

	public float max()
	{
		return Math.max(x, y);
	}

	public float dot(Vector2f r)
	{
		return x * r.getX() + y * r.getY();
	}
	
	public Vector2f normalize()
	{
		float length = length();
		
		return new Vector2f(x / length, y / length);
	}
	
	public Vector2f normalizeLocal()
	{
		float length = length();
		
		x /= length;
		y /= length;
		
		return this;
	}

	public float cross(Vector2f r)
	{
		return x * r.getY() - y * r.getX();
	}

	public Vector2f lerp(Vector2f dest, float lerpFactor)
	{
		return dest.sub(this).mul(lerpFactor).add(this);
	}

	public Vector2f rotate(float angle)
	{
		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);
		
		return new Vector2f((float)(x * cos - y * sin),(float)(x * sin + y * cos));
	}
	
	public Vector2f add(Vector2f r)
	{
		return new Vector2f(x + r.getX(), y + r.getY());
	}
	
	public Vector2f add(float r)
	{
		return new Vector2f(x + r, y + r);
	}
	
	public Vector2f sub(Vector2f r)
	{
		return new Vector2f(x - r.getX(), y - r.getY());
	}
	
	public Vector2f sub(float r)
	{
		return new Vector2f(x - r, y - r);
	}
	
	public Vector2f mul(Vector2f r)
	{
		return new Vector2f(x * r.getX(), y * r.getY());
	}
	
	public Vector2f mul(float r)
	{
		return new Vector2f(x * r, y * r);
	}
	
	public Vector2f div(Vector2f r)
	{
		return new Vector2f(x / r.getX(), y / r.getY());
	}
	
	public Vector2f div(float r)
	{
		return new Vector2f(x / r, y / r);
	}
	
	public Vector2f divLocal(float value)
	{
		x /= value;
		y /= value;
		
		return this;
	}
	
	public Vector2f abs()
	{
		return new Vector2f(Math.abs(x), Math.abs(y));
	}
	
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
	
	public String toStringFormal()
	{
		return "(" + x + ", " + y + ")";
	}

	public Vector2f set(float x, float y) { this.x = x; this.y = y; return this; }
	public Vector2f set(Vector2f r) { set(r.getX(), r.getY()); return this; }

	public float getX() 
	{
		return x;
	}

	public void setX(float x) 
	{
		this.x = x;
	}

	public float getY() 
	{
		return y;
	}

	public void setY(float y)
	{
		this.y = y;
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
        
        final Vector2f other = (Vector2f)obj;
		
		return x == other.x && 
			   y == other.y;
	}
	
	@Override
	public int hashCode()
	{
		int result = 7;

		result = 37 * result + Float.floatToIntBits(x);
		result = 37 * result + Float.floatToIntBits(y);

		return result;
	}
	
	public static Vector2f getVector2f(String lineOfText)// load standard form, i.e., (x, y)
	{
		lineOfText = lineOfText.substring(lineOfText.indexOf("(") + 1, lineOfText.indexOf(")"));
		
		String[] str_Values = lineOfText.split(",");
		
		float x = Float.valueOf(str_Values[0].trim());
		float y = Float.valueOf(str_Values[1].trim());
		
		return new Vector2f(x, y);
	}
}
