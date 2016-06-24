package radiosity;

import java.io.BufferedReader;
import java.io.FileReader;

import math.Vector3f;
import math.material.AbradedOpaque;
import math.material.AbradedTranslucent;
import math.material.Merl;
import math.material.NoBounceVertexColor;
import math.material.PureDiffusion;
import model.Model;
import model.RawModel;
import model.obj.ObjModel;
import model.primitive.ColoredTriangle;
import model.primitive.Sphere;
import model.primitive.Triangle;
import model.primitive.TriangleMesh;
import scene.Scene;
import util.Debug;
import util.StringUtil;

public class RadiosityDatReader
{
	private String m_fullFilename;
	
	public RadiosityDatReader(String fullFilename)
	{
		m_fullFilename = fullFilename;
	}
	
	public Scene parse()
	{
		Scene scene = new Scene();
		
		try 
		{
			BufferedReader fileReader = new BufferedReader(new FileReader(m_fullFilename));
			
			String currentLine;
			
			Vector3f posA = new Vector3f();
			Vector3f posB = new Vector3f();
			Vector3f posC = new Vector3f();
			Vector3f colorA = new Vector3f();
			Vector3f colorB = new Vector3f();
			Vector3f colorC = new Vector3f();
			
			NoBounceVertexColor matl = new NoBounceVertexColor();
//			PureDiffusion matl = new PureDiffusion(1, 1, 1);
			TriangleMesh triangleMesh = new TriangleMesh();
			RawModel model = new RawModel(triangleMesh, matl);
			
			if(m_fullFilename.equals("./resource/radiosity/church.tri.9300"))
			{
				model.getTransform().setRotDeg(new Vector3f(1, 0, 0), -90);
				model.getTransform().setPos(0.0f, -5.0f, 0.0f);
				
				ObjModel carModel = new ObjModel("./resource/model/lamborghini_aventador.obj");
				carModel.getTransform().setPos(2.5f, -3.5f, 1.0f);
				carModel.getTransform().setScale(1.0f);
				scene.addModel(carModel);
				
				Sphere sphereWall = new Sphere(0, -1003.75f, 0.0f, 1000.0f);
				AbradedOpaque sphereWallMatl = new AbradedOpaque();
				sphereWallMatl.setConstAlbedo(0.9f, 0.9f, 0.9f);
				sphereWallMatl.setRoughness(0.5f);
//				sphereWallMatl.setEmissivity(1, 0, 0);
				RawModel sphereWallModel = new RawModel(sphereWall, sphereWallMatl);
				scene.addModel(sphereWallModel);
				
				scene.getCamera().setDir(-0.7916469f, -0.41420883f, -0.4491308f);
				scene.getCamera().setPos(4.319907f, -2.0722241f, 2.7602394f);
			}
			else if(m_fullFilename.equals("./resource/radiosity/hall.tri.4500"))
			{
				model.getTransform().setRotDeg(new Vector3f(1, 0, 0), -90);
//				model.getTransform().setPos(0.0f, -5.0f, 0.0f);
				
				Sphere sphereWall = new Sphere(0, -1002.0f, 0.0f, 1000.0f);
				AbradedOpaque sphereWallMatl = new AbradedOpaque();
				sphereWallMatl.setConstAlbedo(0.95f, 0.95f, 0.95f);
				sphereWallMatl.setRoughness(0.5f);
//				sphereWallMatl.setEmissivity(1, 0, 0);
				RawModel sphereWallModel = new RawModel(sphereWall, sphereWallMatl);
				scene.addModel(sphereWallModel);
				
				ObjModel teapotModel = new ObjModel("./resource/model/teapot_hires.obj");
				teapotModel.getTransform().setPos(0, -2, 1.0f);
				teapotModel.getTransform().setScale(0.8f);
				teapotModel.getTransform().setRotDeg(new Vector3f(0, 1, 0), -30);
				scene.addModel(teapotModel);
				
				ObjModel monkeyModel = new ObjModel("./resource/model/monkey.obj");
				monkeyModel.getTransform().setPos(0, -1.1f, -5.0f);
				monkeyModel.getTransform().setScale(1.0f);
				scene.addModel(monkeyModel);
				
				ObjModel playerModel = new ObjModel("./resource/model/player.obj");
				playerModel.getTransform().setPos(0, -1.5f, -10.0f);
				playerModel.getTransform().setScale(1.0f);
				scene.addModel(playerModel);
				
				scene.getCamera().setDir(-0.3243841f, -0.07832131f, -0.94266045f);
				scene.getCamera().setPos(1.499391f, -0.5754583f, 4.013812f);
			}
			else if(m_fullFilename.equals("./resource/radiosity/room.tri.12100"))
			{
				model.getTransform().setRotDeg(new Vector3f(1, 0, 0), -180);
			}
			else if(m_fullFilename.equals("./resource/radiosity/blocks.tri.7000"))
			{
				Sphere sphereWall = new Sphere(0, 0, 1000.0f, 1000.0f);
				AbradedOpaque sphereWallMatl = new AbradedOpaque();
				sphereWallMatl.setConstAlbedo(1.0f, 1.0f, 1.0f);
				sphereWallMatl.setF0(1.0f, 1.0f, 1.0f);
				sphereWallMatl.setRoughness(0.0f);
				sphereWallMatl.setMetalness(1.0f);
//				sphereLightMatl.setEmissivity(5.0f, 5.0f, 5.0f);
				RawModel sphereWallModel = new RawModel(sphereWall, sphereWallMatl);
				scene.addModel(sphereWallModel);
				
				Sphere sphere = new Sphere(1, 0, -3, 1.2f);
	//			AbradedOpaque sphereMatl kk= new AbradedOpaque();
				AbradedTranslucent sphereMatl = new AbradedTranslucent();
				sphereMatl.setAlbedo(1.0f, 1.0f, 1.0f);
//				sphereMatl.setF0(0.1f, 0.1f, 0.1f);
				sphereMatl.setRoughness(0.0f);
				sphereMatl.setIor(1.5f);
//				sphereMatl.setMetalness(1.0f);
//				sphereLightMatl.setEmissivity(5.0f, 5.0f, 5.0f);
//				Merl sphereMatl = new Merl("chrome");
				RawModel sphereLightModel = new RawModel(sphere, sphereMatl);
				scene.addModel(sphereLightModel);
			}
			
			
			while((currentLine = fileReader.readLine()) != null)
			{
				if(currentLine.trim().equals("Triangle"))
				{
					currentLine = fileReader.readLine();
					parsePosAndColor(currentLine, posA, colorA);
					currentLine = fileReader.readLine();
					parsePosAndColor(currentLine, posB, colorB);
					currentLine = fileReader.readLine();
					parsePosAndColor(currentLine, posC, colorC);
					
					Debug.print(posA);
					Debug.print(posB);
					Debug.print(posC);
					
					ColoredTriangle triangle = new ColoredTriangle(posA, posB, posC,
						                      	                   colorA, colorB, colorC);
					triangleMesh.addTriangle(triangle);
				}
			}
			
			model.getTransform().setScale(0.01f);
			scene.addModel(model);
			
//			Sphere sphere = new Sphere(0, 0, -3, 1.7f);
////			AbradedOpaque sphereMatl = new AbradedOpaque();
//			AbradedTranslucent sphereMatl = new AbradedTranslucent();
//			sphereMatl.setAlbedo(1.0f, 1.0f, 1.0f);
//			sphereMatl.setF0(0.1f, 0.1f, 0.1f);
//			sphereMatl.setRoughness(0.0f);
//			sphereMatl.setIor(1.5f);
////			sphereMatl.setMetalness(1.0f);
////			sphereLightMatl.setEmissivity(5.0f, 5.0f, 5.0f);
//			RawModel sphereLightModel = new RawModel(sphere, sphereMatl);
//			scene.addModel(sphereLightModel);
			
//			Sphere sphereWall = new Sphere(0, 0, 1000.0f, 1000.0f);
//			AbradedOpaque sphereWallMatl = new AbradedOpaque();
//			sphereWallMatl.setAlbedo(1.0f, 1.0f, 1.0f);
//			sphereWallMatl.setF0(1.0f, 1.0f, 1.0f);
//			sphereWallMatl.setRoughness(0.0f);
//			sphereWallMatl.setMetalness(1.0f);
////			sphereLightMatl.setEmissivity(5.0f, 5.0f, 5.0f);
//			RawModel sphereWallModel = new RawModel(sphereWall, sphereWallMatl);
//			scene.addModel(sphereWallModel);
			
//			Model dragonModel = new ObjModel("./resource/model/dragon.obj");
//			dragonModel.getTransform().setScale(3.0f);
//			scene.addModel(dragonModel);
			
//			scene.getCamera().setPos(3.3163042f, 1.4020933f, -4.2036543f);
//			scene.getCamera().setDir(-0.85528207f, -0.31938317f, 0.40798423f);
			
//			scene.getCamera().setPos(0, 0, 12);
//			scene.getCamera().setDir(0, 0, -1);
			
			fileReader.close();
		} 
		catch(Exception e)
		{
			e.printStackTrace();
			Debug.printErr("error: file " + m_fullFilename + " loading failed");
			System.exit(1);
		}
		
		return scene;
	}
	
	private static void parsePosAndColor(String line, Vector3f posResult, Vector3f colorResult)
	{
		String[] dataSegments = StringUtil.getDataSegments(line);
		
		posResult.x = Float.valueOf(dataSegments[0]);
		posResult.y = -Float.valueOf(dataSegments[1]);// flip y
		posResult.z = Float.valueOf(dataSegments[2]);
		
		colorResult.x = Float.valueOf(dataSegments[3]) / 255.0f;
		colorResult.y = Float.valueOf(dataSegments[4]) / 255.0f;
		colorResult.z = Float.valueOf(dataSegments[5]) / 255.0f;
	}
}
