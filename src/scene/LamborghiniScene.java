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
import model.RawModel;
import model.obj.ObjModel;
import model.primitive.Sphere;

public class LamborghiniScene extends Scene
{
	public LamborghiniScene()
	{
		super();
		
		Camera camera = getCamera();
		camera.setPos(0.0f, -2.5f, 4.0f);
//		camera.setDir(0.25f, -0.35f, -1.0f);
		camera.setDir(-0.03f, -0.22f, -0.6f);
		
		float wallR = 1000.0f;
		float halfSize = 5.0f;
		
		ObjModel carModel = new ObjModel("./resource/model/lamborghini_aventador.obj");
		carModel.getTransform().setPos(-0.5f, -5.0f, -2.0f);
		carModel.getTransform().setScale(3.5f);
		carModel.getTransform().setRotDeg(new Vector3f(0, 1, 0), 120);
		addModel(carModel);
		
		AbradedOpaque sphereLightMatl = new AbradedOpaque();
		RawModel sphereLight = new RawModel(new Sphere(-4.0f, -3.3f, -3.0f, 0.5f), sphereLightMatl);
		sphereLightMatl.setAlbedo(0.8f, 1.0f, 0.8f);
		sphereLightMatl.setEmissivity(10.0f, 20.0f, 10.0f);
		addModel(sphereLight);
		
		AbradedOpaque sphereLightMatl2 = new AbradedOpaque();
		RawModel sphereLight2 = new RawModel(new Sphere(-2.5f, -4.7f, -0.5f, 0.3f), sphereLightMatl2);
		sphereLightMatl2.setAlbedo(0.8f, 1.0f, 0.8f);
		sphereLightMatl2.setEmissivity(20.0f, 20.0f, 20.0f);
		addModel(sphereLight2);
		
		// temp hack
		ObjModel cylinderModel = new ObjModel("./resource/model/cylinder.obj");
		AbradedOpaque cylinderLightMatl = new AbradedOpaque();
		RawModel cylinderLightModel = new RawModel(cylinderModel.getPrimitive(), cylinderLightMatl);
		cylinderLightModel.getTransform().setPos(3.5f, -3.5f, -3.0f);
		cylinderLightModel.getTransform().setScale(0.15f, 1.5f, 0.15f);
		cylinderLightMatl.setEmissivity(10.0f, 20.0f, 20.0f);
//		cylinderLightMatl.setIor(1.5f);
//		cylinderLightMatl.setRoughness(0.0f);
		addModel(cylinderLightModel);
		
		// temp hack
		ObjModel cubeModel = new ObjModel("./resource/model/cube.obj");
		AbradedOpaque cubeMatl = new AbradedOpaque();
		RawModel cubeRawModel = new RawModel(cubeModel.getPrimitive(), cubeMatl);
		cubeRawModel.getTransform().setPos(3.4f, -4.4f, -0.2f);
		cubeRawModel.getTransform().setScale(0.6f);
		cubeMatl.setEmissivity(20.0f, 20.0f, 10.0f);
		addModel(cubeRawModel);
		
		// temp hack
		ObjModel textModel = new ObjModel("./resource/model/lambo_text.obj");
		AbradedTranslucent textMatl = new AbradedTranslucent();
		RawModel textRawModel = new RawModel(textModel.getPrimitive(), textMatl);
		textRawModel.getTransform().setPos(-2.0f, -4.85f, 0.3f);
		textRawModel.getTransform().setScale(2.0f);
		textRawModel.getTransform().setRotDeg(new Vector3f(0, 1, 0), 20);
//		textMatl.setEmissivity(20.0f, 20.0f, 10.0f);
		textMatl.setIor(1.5f);
		textMatl.setRoughness(0.01f);
		addModel(textRawModel);
		
		
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
