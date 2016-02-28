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

import main.Camera;
import math.material.CookTorrance;
import model.RawModel;
import model.primitive.Sphere;

public class FiveBallsScene extends Scene
{
	public FiveBallsScene()
	{
		super();
		
//		camera = new Camera();
////		camera.setPos(0.07f, 0.03f, 5.01f);
//		camera.setDir(0.3f, -0.0f, -1.0f);
//		camera.setPos(-4.5f, 0.03f, 5.01f);
		
		float wallR = 1000.0f;
		float halfSize = 5.0f;
		
		CookTorrance sphere1Matl = new CookTorrance();
		RawModel sphere1 = new RawModel(new Sphere(-halfSize + 0.25f, -halfSize + 0.25f, -10.0f, 0.25f), sphere1Matl);
		sphere1Matl.setAlbedo(0.0f, 1.0f, 0.0f);
		sphere1Matl.setRoughness(0.0f);
		sphere1Matl.setF0(1.0f, 1.0f, 1.0f);
		addModel(sphere1);
		
		CookTorrance sphere2Matl = new CookTorrance();
		RawModel sphere2 = new RawModel(new Sphere(-halfSize + 1.5f, -halfSize + 0.5f, -10.0f, 0.5f), sphere2Matl);
		sphere2Matl.setAlbedo(0.0f, 1.0f, 0.0f);
		sphere2Matl.setRoughness(0.0f);
		sphere2Matl.setF0(1.0f, 1.0f, 1.0f);
		addModel(sphere2);
		
		CookTorrance sphere3Matl = new CookTorrance();
		RawModel sphere3 = new RawModel(new Sphere(-halfSize + 4.0f, -halfSize + 1.0f, -10.0f, 1.0f), sphere3Matl);
		sphere3Matl.setAlbedo(0.0f, 1.0f, 0.0f);
		sphere3Matl.setRoughness(0.0f);
		sphere3Matl.setF0(1.0f, 1.0f, 1.0f);
		addModel(sphere3);
		
		CookTorrance sphere4Matl = new CookTorrance();
		RawModel sphere4 = new RawModel(new Sphere(halfSize - 3.0f, -halfSize + 3.0f, -halfSize - 7.0f, 3.0f), sphere4Matl);
//		RawModel sphere4 = new RawModel(new AnalyticalSphere(0.0f, 0.0f, -halfSize - 7.0f, 3.0f));
//		sphere4Matl.setAlbedo(0.1f, 0.1f, 0.1f);
//		sphere4Matl.setAlbedo(0.5f, 0.5f, 0.5f);
//		sphere4Matl.setAlbedo(1.0f, 1.0f, 1.0f);
		sphere4Matl.setRoughness(0.0f);
//		sphere4Matl.setF0(1.0f, 1.0f, 1.0f);
//		sphere4Matl.setF0(0.9f, 0.9f, 0.9f);
//		sphere4Matl.setF0(0.5f, 0.5f, 0.5f);
//		sphere4Matl.setF0(0.1f, 0.1f, 0.1f);
		addModel(sphere4);
		
		CookTorrance sphere5Matl = new CookTorrance();
		RawModel sphere5 = new RawModel(new Sphere(halfSize - 2.0f, -halfSize + 0.8f, -8.5f, 0.8f), sphere5Matl);
		sphere5Matl.setAlbedo(0.0f, 1.0f, 0.0f);
		sphere5Matl.setRoughness(0.0f);
		sphere5Matl.setF0(1.0f, 1.0f, 1.0f);
		addModel(sphere5);
		
//		RawModel whiteSphereLight = new RawModel(new AnalyticalSphere(1.0f, 1.0f, -10.0f, 0.5f));
//		whiteSphereLight.getMaterial().setBaseColor(1.0f, 1.0f, 1.0f);
//		whiteSphereLight.getMaterial().setEmissivity(50.0f, 50.0f, 50.0f);
//		scene.addModel(whiteSphereLight);
		
		
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
//		rightWallMatl.setF0(1.0f, 1.0f, 1.0f);
		rightWallMatl.setRoughness(0.2f);
		addModel(rightWall);
		
		CookTorrance backWallMatl = new CookTorrance();
		RawModel backWall = new RawModel(new Sphere(0.0f, 0.0f, -wallR - halfSize - 10.0f, wallR), backWallMatl);
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
		topWallMatl.setEmissivity(1.5f, 1.5f, 1.5f);
//		topWallMatl.setEmissivity(5.5f, 5.5f, 5.5f);
		addModel(topWall);
		
		CookTorrance frontWallMatl = new CookTorrance();
		RawModel frontWall = new RawModel(new Sphere(0.0f, 0.0f, wallR + 5.5f, wallR), frontWallMatl);
		frontWallMatl.setAlbedo(1.0f, 1.0f, 1.0f);
//		frontWallMatl.setF0(1.0f, 1.0f, 1.0f);
//		frontWallMatl.setRoughness(0.0f);
		addModel(frontWall);
	}
}
