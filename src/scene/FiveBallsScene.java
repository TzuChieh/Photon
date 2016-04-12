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
import math.Vector3f;
import math.material.AbradedOpaque;
import math.material.AbradedTranslucent;
import math.material.PureDiffusion;
import model.RawModel;
import model.obj.ObjModel;
import model.primitive.Sphere;

public class FiveBallsScene extends Scene
{
	public FiveBallsScene()
	{
		super();
		
		Camera camera = getCamera();
//		camera.setPos(0.07f, 0.03f, 5.01f);
//		camera.setDir(0.3f, -0.0f, -1.0f);
//		camera.setPos(-1.5f, 2.43f, 4.01f);
		camera.setPos(0.07f, 0.03f, 4.95f);
//		camera.setDir(0.2f, -0.25f, -1.0f);
		
//		Camera camera = getCamera();
//		camera.setPos(-4.4f, -3.0f, 2.01f);
//		camera.setDir(0.25f, -0.35f, -1.0f);
//		
		float wallR = 1000.0f;
		float halfSize = 5.0f;
		
//		ObjModel bunnyModel = new ObjModel("./resource/model/bunny.obj");
//		bunnyModel.getTransform().setPos(-1.0f, 0.5f, 0.0f);
//		bunnyModel.getTransform().setScale(2.0f);
//		bunnyModel.getTransform().setRotDeg(new Vector3f(0, 1, 0), 180);
		
		// observe caustics
//		ObjModel bunnyModel = new ObjModel("./resource/model/bunny.obj");
//		bunnyModel.getTransform().setPos(-3.8f, -4.98f, -1.5f);
//		bunnyModel.getTransform().setScale(2.0f);
//		bunnyModel.getTransform().setRotDeg(new Vector3f(0, 1, 0), 180);
		
		// observe caustics
//		ObjModel dragonModel = new ObjModel("./resource/model/dragon.obj");
//		dragonModel.getTransform().setPos(-3.3f, -4.98f, -1.5f);
//		dragonModel.getTransform().setScale(1.8f);
//		dragonModel.getTransform().setRotDeg(new Vector3f(0, 1, 0), 180);
//		addModel(dragonModel);
		
//		AbradedOpaque sphereLightMatl = new AbradedOpaque();
//		RawModel sphereLight = new RawModel(new Sphere(-3.55f, -3.6f, -3.0f, 0.5f), sphereLightMatl);
//		sphereLightMatl.setAlbedo(0.8f, 1.0f, 0.8f);
//		sphereLightMatl.setEmissivity(5.0f, 20.0f, 5.0f);
//		addModel(sphereLight);
		
//		AbradedOpaque sphereLightMatl = new AbradedOpaque();
//		RawModel sphereLight = new RawModel(new Sphere(-3.0f, -3.3f, -3.0f, 0.5f), sphereLightMatl);
//		sphereLightMatl.setAlbedo(0.8f, 1.0f, 0.8f);
//		sphereLightMatl.setEmissivity(10.0f, 20.0f, 10.0f);
//		addModel(sphereLight);
//		
//		AbradedOpaque sphereLightMatl2 = new AbradedOpaque();
//		RawModel sphereLight2 = new RawModel(new Sphere(-2.5f, -4.7f, -0.5f, 0.3f), sphereLightMatl2);
//		sphereLightMatl2.setAlbedo(0.8f, 1.0f, 0.8f);
//		sphereLightMatl2.setEmissivity(20.0f, 20.0f, 20.0f);
//		addModel(sphereLight2);
		
		
//		ObjModel bunnyModel = new ObjModel("./resource/model/venusm_low_poly.obj");
//		ObjModel bunnyModel = new ObjModel("./resource/model/icosphere.obj");
//		bunnyModel.getPrimitive().getTransform().setPos(-5.0f, 0.0f, -7.0f);
//		ObjModel bunnyModel = new ObjModel("./resource/model/bunny_low_poly.obj");
//		ObjModel bunnyModel = new ObjModel("./resource/model/teapot.obj");
//		bunnyModel.getTransform().setPos(-1.0f, 0.5f, 0.0f);
//		bunnyModel.getTransform().setRotDeg(new Vector3f(0, 1, 0), 90);
		
		PureDiffusion sphere1Matl = new PureDiffusion(0.3f, 0.3f, 1.0f);
//		AbradedOpaque sphere1Matl = new AbradedOpaque();
		RawModel sphere1 = new RawModel(new Sphere(-halfSize + 0.25f, -halfSize + 0.25f, -10.0f, 0.25f), sphere1Matl);
//		sphere1Matl.setAlbedo(0.0f, 1.0f, 0.0f);
//		sphere1Matl.setRoughness(0.0f);
//		sphere1Matl.setF0(1.0f, 1.0f, 1.0f);
		addModel(sphere1);
		
		PureDiffusion sphere2Matl = new PureDiffusion(0.3f, 1.0f, 0.3f);
//		AbradedOpaque sphere2Matl = new AbradedOpaque();
		RawModel sphere2 = new RawModel(new Sphere(-halfSize + 1.5f, -halfSize + 0.5f, -10.0f, 0.5f), sphere2Matl);
//		sphere2Matl.setAlbedo(0.0f, 1.0f, 0.0f);
//		sphere2Matl.setRoughness(0.0f);
//		sphere2Matl.setF0(1.0f, 1.0f, 1.0f);
		addModel(sphere2);
		
		PureDiffusion sphere3Matl = new PureDiffusion(1.0f, 0.3f, 0.3f);
//		AbradedOpaque sphere3Matl = new AbradedOpaque();
		RawModel sphere3 = new RawModel(new Sphere(-halfSize + 4.0f, -halfSize + 1.0f, -10.0f, 1.0f), sphere3Matl);
//		sphere3Matl.setAlbedo(0.0f, 1.0f, 0.0f);
//		sphere3Matl.setRoughness(0.0f);
//		sphere3Matl.setF0(1.0f, 1.0f, 1.0f);
		addModel(sphere3);
		
		PureDiffusion sphere4Matl = new PureDiffusion(1.0f, 1.0f, 1.0f);
//		AbradedOpaque sphere4Matl = new AbradedOpaque();
//		AbradedTranslucent sphere4Matl = new AbradedTranslucent();
		RawModel sphere4 = new RawModel(new Sphere(halfSize - 3.0f, -halfSize + 3.0f, -halfSize - 10.0f + 3.0f, 3.0f), sphere4Matl);
//		RawModel sphere4 = new RawModel(new Sphere(halfSize - 3.0f, -halfSize + 3.0f, -halfSize - 7.0f + 5.0f, 3.0f), sphere4Matl);
//		RawModel sphere4 = new RawModel(new AnalyticalSphere(0.0f, 0.0f, -halfSize - 7.0f, 3.0f));
//		sphere4Matl.setAlbedo(0.1f, 0.1f, 0.1f);
//		sphere4Matl.setAlbedo(0.5f, 0.5f, 0.5f);
//		sphere4Matl.setAlbedo(1.0f, 1.0f, 1.0f);
//		sphere4Matl.setRoughness(0.0f);
//		sphere4Matl.setIor(1.55f);
//		sphere4Matl.setF0(0.0f, 0.0f, 0.0f);
//		sphere4Matl.setF0(1.0f, 1.0f, 1.0f);
//		sphere4Matl.setF0(0.9f, 0.9f, 0.9f);
//		sphere4Matl.setF0(0.5f, 0.5f, 0.5f);
//		sphere4Matl.setF0(0.1f, 0.1f, 0.1f);
		addModel(sphere4);
		
		PureDiffusion sphere5Matl = new PureDiffusion(1.0f, 1.0f, 1.0f);
//		AbradedOpaque sphere5Matl = new AbradedOpaque();
		RawModel sphere5 = new RawModel(new Sphere(halfSize - 2.0f, -halfSize + 0.8f, -8.5f, 0.8f), sphere5Matl);
//		sphere5Matl.setAlbedo(0.0f, 1.0f, 0.0f);
//		sphere5Matl.setRoughness(0.0f);
//		sphere5Matl.setF0(1.0f, 1.0f, 1.0f);
		addModel(sphere5);
		
//		AbradedOpaque whiteSphereLightMatl = new AbradedOpaque();
//		RawModel whiteSphereLight = new RawModel(new Sphere(1.0f, 1.0f, -10.0f, 0.5f), whiteSphereLightMatl);
//		whiteSphereLightMatl.setAlbedo(0.8f, 1.0f, 0.8f);
//		whiteSphereLightMatl.setEmissivity(50.0f, 50.0f, 50.0f);
//		addModel(whiteSphereLight);
		
		
		AbradedOpaque leftWallMatl = new AbradedOpaque();
		RawModel leftWall = new RawModel(new Sphere(-wallR - halfSize, 0.0f, 0.0f, wallR), leftWallMatl);
		leftWallMatl.setAlbedo(0.9f, 0.2f, 0.2f);
//		leftWallMatl.setEmissivity(1.5f, 1.5f, 1.5f);
//		leftWallMatl.setEmissivity(5.5f, 5.5f, 5.5f);
//		leftWallMatl.setF0(1.0f, 1.0f, 1.0f);
//		leftWallMatl.setBaseColor(0.1f, 0.1f, 0.1f);
//		leftWallMatl.setRoughness(0.1f);
		addModel(leftWall);
		
		AbradedOpaque rightWallMatl = new AbradedOpaque();
		RawModel rightWall = new RawModel(new Sphere(wallR + halfSize, 0.0f, 0.0f, wallR), rightWallMatl);
		rightWallMatl.setAlbedo(0.2f, 0.2f, 0.9f);
//		rightWallMatl.setEmissivity(1.5f, 1.5f, 1.5f);
//		rightWallMatl.setF0(1.0f, 1.0f, 1.0f);
//		rightWallMatl.setRoughness(0.1f);
		addModel(rightWall);
		
		AbradedOpaque backWallMatl = new AbradedOpaque();
		RawModel backWall = new RawModel(new Sphere(0.0f, 0.0f, -wallR - halfSize - 10.0f, wallR), backWallMatl);
		backWallMatl.setAlbedo(0.9f, 0.9f, 0.9f);
//		backWallMatl.setEmissivity(2.0f, 2.0f, 2.0f);
//		backWallMatl.setF0(1.0f, 1.0f, 1.0f);
//		backWallMatl.setRoughness(0.1f);
		addModel(backWall);
		
		AbradedOpaque groundWallMatl = new AbradedOpaque();
		RawModel groundWall = new RawModel(new Sphere(0.0f, -wallR - halfSize, 0.0f, wallR), groundWallMatl);
		groundWallMatl.setAlbedo(0.9f, 0.9f, 0.9f);
//		groundWallMatl.setAlbedo(0.1f, 0.1f, 0.1f);
//		groundWallMatl.setF0(1.0f, 1.0f, 1.0f);
//		groundWallMatl.setRoughness(0.8f);
		addModel(groundWall);
		
		AbradedOpaque topWallMatl = new AbradedOpaque();
		RawModel topWall = new RawModel(new Sphere(0.0f, wallR + halfSize, 0.0f, wallR), topWallMatl);
		topWallMatl.setAlbedo(0.9f, 0.9f, 0.9f);
//		topWallMatl.setEmissivity(0.5f, 0.5f, 0.5f);
		topWallMatl.setEmissivity(1.5f, 1.5f, 1.5f);
//		topWallMatl.setEmissivity(5.5f, 5.5f, 5.5f);
		addModel(topWall);
		
		AbradedOpaque frontWallMatl = new AbradedOpaque();
		RawModel frontWall = new RawModel(new Sphere(0.0f, 0.0f, wallR + 5.5f, wallR), frontWallMatl);
		frontWallMatl.setAlbedo(0.9f, 0.9f, 0.9f);
//		frontWallMatl.setF0(1.0f, 1.0f, 1.0f);
//		frontWallMatl.setRoughness(0.1f);
		addModel(frontWall);
	}
}
