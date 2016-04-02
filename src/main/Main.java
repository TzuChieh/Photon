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

import core.Engine;
import math.Rand;
import math.Vector3f;
import math.material.AbradedOpaque;
import model.RawModel;
import model.boundingVolume.AABB;
import model.primitive.Sphere;
import model.primitive.Triangle;
import ui.Window;
import util.Debug;
import util.Time;

public class Main
{
	public static void main(String[] args)
	{
		Triangle triangle = new Triangle(new Vector3f(12, 9, 9), new Vector3f(9, 12, 9), new Vector3f(19, 19, 20));
		AABB aabb = new AABB(new Vector3f(-10, -10, -10), new Vector3f(10, 10, 10));
		
		System.out.println(triangle.isIntersect(aabb));
//		System.out.println(aabb.isIntersect(aabb));
		
//		int test = 1;
//		
//		if(test = 2)
//			Debug.print("WTF");
		
		
		Engine engine = new Engine();
		
		engine.run();
	}
}
