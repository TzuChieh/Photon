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

package scene;

import math.Rand;
import math.Vector3f;
import math.material.CookTorrance;
import model.RawModel;
import model.primitive.Sphere;
import model.primitive.Triangle;

public class ClassicMaterialScene extends Scene
{
	public ClassicMaterialScene()
	{
		super();
		
		float roughness;
		float f0;
		
		f0 = 1.0f;
		// construct spheres with different materials
		for(float x = -7.0f; x < 7.0f; x += 1.4f)
		{
			roughness = 1.0f;
			
			for(float z = -7; z < 7; z += 1.4f)
			{
				CookTorrance material = new CookTorrance();
//				material.setRoughness(roughness);
				material.setRoughness(roughness * roughness);
				material.setF0(f0, f0, f0);
				material.setAlbedo(Rand.getFloat0_1(), Rand.getFloat0_1(), Rand.getFloat0_1());
//				material.setAlbedo(0.9f, 0.9f, 0.9f);
				RawModel model = new RawModel(new Sphere(x, -6.0f, z, 0.6f), material);
				addModel(model);
				
				roughness -= 0.1f;
			}
			
			f0 -= 0.1f;
		}
		
		CookTorrance triangleMatl = new CookTorrance();
		RawModel triangleModel = new RawModel(new Triangle(new Vector3f(10.0f, -6.0f, 10.0f),
				                                           new Vector3f(10.0f, -6.0f, -10.0f),
				                                           new Vector3f(-10.0f, -6.0f, -10.0f)), triangleMatl);
//		RawModel triangleModel = new RawModel(new Triangle(new Vector3f(1.0f, -5.0f, 1.0f),
//                                                           new Vector3f(1.0f, -5.0f, -1.0f),
//                                                           new Vector3f(-1.0f, -5.0f, -1.0f)), triangleMatl);
		triangleMatl.setRoughness(0.3f);
		triangleMatl.setF0(0.3f, 0.3f, 0.3f);
		addModel(triangleModel);
		
		
		CookTorrance lightMaterial = new CookTorrance();
		RawModel lightSphere = new RawModel(new Sphere(0.0f, -2.0f, 0.0f, 1.5f), lightMaterial);
//		lightMaterial.setAlbedo(1.0f, 0.0f, 0.0f);
//		lightMaterial.setEmissivity(1.5f, 1.5f, 1.5f);
		lightMaterial.setEmissivity(15.0f, 19.0f, 13.0f);
//		lightMaterial.setF0(1.0f, 1.0f, 1.0f);
//		lightMaterial.setBaseColor(0.1f, 0.1f, 0.1f);
//		lightMaterial.setRoughness(0.2f);
		addModel(lightSphere);
		
		// construct Cornell box
		
		float wallR = 1000.0f;
		float halfSize = 10.0f;
		
		CookTorrance leftWallMatl = new CookTorrance();
		RawModel leftWall = new RawModel(new Sphere(-wallR - halfSize, 0.0f, 0.0f, wallR), leftWallMatl);
		leftWallMatl.setAlbedo(1.0f, 0.0f, 0.0f);
//		leftWallMatl.setEmissivity(1.5f, 1.5f, 1.5f);
//		leftWallMatl.setEmissivity(5.5f, 5.5f, 5.5f);
//		leftWallMatl.setF0(1.0f, 1.0f, 1.0f);
//		leftWallMatl.setBaseColor(0.1f, 0.1f, 0.1f);
		leftWallMatl.setRoughness(0.2f);
		addModel(leftWall);
		
		CookTorrance rightWallMatl = new CookTorrance();
		RawModel rightWall = new RawModel(new Sphere(wallR + halfSize, 0.0f, 0.0f, wallR), rightWallMatl);
		rightWallMatl.setAlbedo(0.0f, 0.0f, 1.0f);
//		rightWallMatl.setEmissivity(5.5f, 5.5f, 5.5f);
//		rightWallMatl.setF0(1.0f, 1.0f, 1.0f);
		rightWallMatl.setRoughness(0.2f);
		addModel(rightWall);
		
		CookTorrance backWallMatl = new CookTorrance();
		RawModel backWall = new RawModel(new Sphere(0.0f, 0.0f, -wallR - halfSize, wallR), backWallMatl);
		backWallMatl.setAlbedo(1.0f, 1.0f, 1.0f);
//		backWallMatl.setEmissivity(2.0f, 2.0f, 2.0f);
//		backWallMatl.setF0(1.0f, 1.0f, 1.0f);
//		backWallMatl.setRoughness(0.1f);
		addModel(backWall);
		
		CookTorrance groundWallMatl = new CookTorrance();
		RawModel groundWall = new RawModel(new Sphere(0.0f, -wallR - halfSize, 0.0f, wallR), groundWallMatl);
		groundWallMatl.setAlbedo(1.0f, 1.0f, 1.0f);
//		groundWallMatl.setAlbedo(0.1f, 0.1f, 0.1f);
//		groundWallMatl.setF0(1.0f, 1.0f, 1.0f);
//		groundWallMatl.setRoughness(0.2f);
		addModel(groundWall);
		
		CookTorrance topWallMatl = new CookTorrance();
		RawModel topWall = new RawModel(new Sphere(0.0f, wallR + halfSize, 0.0f, wallR), topWallMatl);
		topWallMatl.setAlbedo(1.0f, 1.0f, 1.0f);
//		topWallMatl.setEmissivity(0.5f, 0.5f, 0.5f);
//		topWallMatl.setEmissivity(1.5f, 1.5f, 1.5f);
//		topWallMatl.setEmissivity(5.5f, 5.5f, 5.5f);
		addModel(topWall);
		
		CookTorrance frontWallMatl = new CookTorrance();
		RawModel frontWall = new RawModel(new Sphere(0.0f, 0.0f, wallR + halfSize, wallR), frontWallMatl);
		frontWallMatl.setAlbedo(1.0f, 1.0f, 1.0f);
//		frontWallMatl.setF0(1.0f, 1.0f, 1.0f);
//		frontWallMatl.setRoughness(0.0f);
		addModel(frontWall);
	}
}
