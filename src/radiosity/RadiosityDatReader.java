package radiosity;

import java.io.BufferedReader;
import java.io.FileReader;

import math.Vector3f;
import math.material.AbradedOpaque;
import math.material.NoBounceVertexColor;
import model.RawModel;
import model.obj.ObjModel;
import model.primitive.ColoredTriangle;
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
			
			NoBounceVertexColor material = new NoBounceVertexColor();
			TriangleMesh triangleMesh = new TriangleMesh();
			RawModel model = new RawModel(triangleMesh, material);
			
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
			
			scene.getCamera().setPos(3.3163042f, 1.4020933f, -4.2036543f);
			scene.getCamera().setDir(-0.85528207f, -0.31938317f, 0.40798423f);
			
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
