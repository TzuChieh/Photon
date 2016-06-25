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

import core.Camera;
import math.Vector3f;
import math.material.AbradedOpaque;
import math.material.AbradedTranslucent;
import math.material.PureDiffusion;
import model.RawModel;
import model.obj.ObjModel;
import model.primitive.Sphere;

public class SponzaScene extends Scene
{
	public SponzaScene()
	{
		super();
		
//		Camera camera = getCamera();
////		camera.setPos(0.07f, 0.03f, 5.01f);
////		camera.setDir(0.3f, -0.0f, -1.0f);
////		camera.setPos(-1.5f, 2.43f, 4.01f);
//		camera.setPos(0.07f, 0.03f, 4.95f);
////		camera.setDir(0.2f, -0.25f, -1.0f);
		
		Camera camera = getCamera();
		camera.setPos(0.0f, 2.0f, 10.0f);
		camera.setDir(0.0f, 0.25f, -1.0f);
		
		float wallR = 1000.0f;
		
		ObjModel sponzaModel = new ObjModel("./resource/model/dabrovic_sponza.obj");
//		ObjModel sponzaModel = new ObjModel("./resource/model/sponza_partial.obj");
		sponzaModel.getTransform().setRotDeg(new Vector3f(0, 1, 0), 90);
		addModel(sponzaModel);
		
		AbradedOpaque topWallMatl = new AbradedOpaque();
		RawModel topWall = new RawModel(new Sphere(0.0f, wallR + 50.0f, 0.0f, wallR), topWallMatl);
		topWallMatl.setConstAlbedo(0.9f, 0.9f, 0.9f);
//		topWallMatl.setEmissivity(0.5f, 0.5f, 0.5f);
		topWallMatl.setEmissivity(4.0f, 4.0f, 4.0f);
//		topWallMatl.setEmissivity(5.5f, 5.5f, 5.5f);
		addModel(topWall);
	}
}
