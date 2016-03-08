package model.obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import math.Vector2f;
import math.Vector3f;
import util.IOUtil;
import util.Logger;
import util.StringUtil;

public class OBJParser 
{
	private String m_fullFileName;
	
	private List<Vector3f> positions;
	private List<Vector2f> texCoords;
	private List<Vector3f> normals;
	private List<OBJIndex> objIndices;
	
//	private List<List<Vector2i>> m_mtlIndexPairs;
//	private List<Vector2i>       m_currentIndexPairs;
	
//	private MTLParser      m_mtlParser;
//	private List<Material> m_materials;
	
	private boolean hasTexCoord;
	private boolean hasNormal;
	
	private Logger m_logger;
	
	public OBJParser(String fullFileName)
	{
		m_logger = new Logger("OBJ Model Loading: " + fullFileName);
		
		m_fullFileName = fullFileName;
		
		positions  = new ArrayList<>();
		texCoords  = new ArrayList<>();
		normals    = new ArrayList<>();
		objIndices = new ArrayList<>();
		
		hasTexCoord = false;
		hasNormal   = false;
		
		String   loadedString = IOUtil.loadTextFile(fullFileName, " ");
		String[] dataSegments = StringUtil.removeEmptyStringsAndTrim(loadedString.split(" "));
		
		loadVertexData(dataSegments);
		
		m_logger.printMsg("loaded successfully");
	}
	
	private static final Map<String, Integer> tokensMap = new HashMap<>();
	
	private static final int POSITION      = 1;
	private static final int TEXCOORD      = 2;
	private static final int NORMAL        = 3;
	private static final int FACE          = 4;
	private static final int SMOOTHSHADING = 5;
	private static final int POUNDSIGN     = 6;
	private static final int USEMTL        = 7;
	private static final int MTLLIB        = 8;
	private static final int OBJECT        = 9;
	
	static
	{
		tokensMap.put("v",       POSITION);
		tokensMap.put("vt",      TEXCOORD);
		tokensMap.put("vn",      NORMAL);
		tokensMap.put("f",       FACE);
		tokensMap.put("s",       SMOOTHSHADING);
		tokensMap.put("#",       POUNDSIGN);
		tokensMap.put("usemtl",  USEMTL);
		tokensMap.put("mtllib",  MTLLIB);
		tokensMap.put("o",       OBJECT);
	}
	
	private void loadVertexData(String[] dataSegments)
	{
		final int dataSegmentsLength = dataSegments.length;
		
		int currentIndex = 0;
		
		while(currentIndex < dataSegmentsLength)
		{
			switch(tokensMap.getOrDefault(dataSegments[currentIndex], 0))
			{
				case POSITION:
					addPosition(dataSegments[currentIndex + 1],
							    dataSegments[currentIndex + 2],
							    dataSegments[currentIndex + 3]);
					currentIndex += 4;
					break;
					
				case TEXCOORD:
					addTexCoord(dataSegments[currentIndex + 1],
					            dataSegments[currentIndex + 2]);
					currentIndex += 3;
					break;
					
				case NORMAL:
					addNormal(dataSegments[currentIndex + 1],
					          dataSegments[currentIndex + 2],
					          dataSegments[currentIndex + 3]);
					currentIndex += 4;
					break;
					
				// support convex face only
				case FACE:
					int triangleFanCenterIndex = currentIndex + 1;
					currentIndex = triangleFanCenterIndex + 2;
					
					do
					{
						objIndices.add(parseOBJIndex(dataSegments[triangleFanCenterIndex]));
						objIndices.add(parseOBJIndex(dataSegments[currentIndex - 1]));
						objIndices.add(parseOBJIndex(dataSegments[currentIndex]));
						
						currentIndex++;
						
						if(currentIndex >= dataSegmentsLength) break;
					} 
					while(!isToken(dataSegments[currentIndex]));
					
					break;
					
//				case MTLLIB:
//					parseMtllib(dataSegments[currentIndex + 1]);
//					currentIndex += 2;
//					break;
//					
//				case USEMTL:
//					parseUsemtl(dataSegments[currentIndex + 1]);
//					currentIndex += 2;
//					break;
					
				default:
					// skip any unsupported features
					currentIndex++;
					break;
			}// end switch
		}// end while
		
		// don't forget to set the index bounds of the last index pairs!
//		if(hasMtl())
//		{
//			m_currentIndexPairs.get(m_currentIndexPairs.size() - 1).y = objIndices.size() - 1;
//		}
	}// end loadVertexData
	
	private void addPosition(String rawX, String rawY, String rawZ)
	{
		positions.add(new Vector3f(Float.valueOf(rawX),
				                   Float.valueOf(rawY),
				                   Float.valueOf(rawZ)));
	}
	
	private void addTexCoord(String rawU, String rawV)
	{
		texCoords.add(new Vector2f(Float.valueOf(rawU),
				                   Float.valueOf(rawV)));
	}
	
	private void addNormal(String rawX, String rawY, String rawZ)
	{
		normals.add(new Vector3f(Float.valueOf(rawX),
				                 Float.valueOf(rawY),
				                 Float.valueOf(rawZ)));
	}
	
	private OBJIndex parseOBJIndex(String rawVertexIndices)
	{
		String[] indicesData = rawVertexIndices.split("/");
		OBJIndex result      = new OBJIndex();
		
		// OpenGL starts counting from 0 while obj is from 1, so we subtract it by 1
		result.positionIndex = Integer.parseInt(indicesData[0]) - 1;
		
		if(indicesData.length > 1)
		{
			if(!indicesData[1].equals(""))
			{
				hasTexCoord = true;
				result.texCoordIndex = Integer.parseInt(indicesData[1]) - 1;
			}
			
			if(indicesData.length > 2)
			{
				if(!indicesData[2].equals(""))
				{
					hasNormal = true;
					result.normalIndex = Integer.parseInt(indicesData[2]) - 1;
				}
			}
		}
		
		return result;
	}
	
//	private void parseUsemtl(String mtlName)
//	{
//		// check existence
//		if(!hasMtl())
//		{
//			return;
//		}
//		
//		Material material = m_mtlParser.getMaterial(mtlName);
//		
//		if(material == null)
//		{
//			m_logger.printErr("can't find material \"" + mtlName + "\" from " + m_mtlParser.getFullFileName());
//			return;
//		}
//		
//		// save previous index pair
//		if(m_currentIndexPairs != null)
//		{
//			m_currentIndexPairs.get(m_currentIndexPairs.size() - 1).y = objIndices.size() - 1;
//		}
//		
//		// before starting a new index pair, check if we have already allocated the index pairs' storage
//		// for this material
//		int index = m_materials.indexOf(material);
//		
//		if(index != -1)
//		{
//			m_currentIndexPairs = m_mtlIndexPairs.get(index);
//		}
//		else
//		{
//			m_materials.add(material);
//			
//			m_currentIndexPairs = new ArrayList<>();
//			m_mtlIndexPairs.add(m_currentIndexPairs);
//		}
//		
//		int startIndex = objIndices.isEmpty() ? 0 : objIndices.size();
//		m_currentIndexPairs.add(new Vector2i(startIndex, startIndex));
//	}
	
//	private void parseMtllib(String mtlFile)
//	{
//		String fileDirectory = m_fullFileName.substring(0, m_fullFileName.lastIndexOf("/") + 1);
//		
//		m_mtlParser = new MTLParser(fileDirectory + mtlFile);
//		m_materials = new ArrayList<>();
//		
//		m_mtlIndexPairs = new ArrayList<>();
//	}
	
	private boolean isToken(String data)
	{
		return tokensMap.get(data) != null;
	}
	
	public IndexedMesh toIndexedMesh()
	{
		IndexedMesh indexedMesh = new IndexedMesh();
		
		Map<OBJIndex, Integer> objIndexMap      = new HashMap<>();
		Map<Vector3f, Integer> positionIndexMap = new HashMap<>();
		
		int objIndicesSize = objIndices.size();
		
		for(int i = 0; i < objIndicesSize; i++)
		{
			OBJIndex currentOBJIndex = objIndices.get(i);
			
			Vector3f currentPosition = positions.get(currentOBJIndex.positionIndex);
			Vector2f currentTexCoord;
			
			currentTexCoord = hasTexCoord ? texCoords.get(currentOBJIndex.texCoordIndex)
                                          : new Vector2f(0, 0);
			
			Integer meshIndex;
			
			if(hasNormal)
			{
				meshIndex = objIndexMap.get(currentOBJIndex);
				
				if(meshIndex == null)
				{
					meshIndex = indexedMesh.getPositions().size();
					objIndexMap.put(currentOBJIndex, meshIndex);
					
					indexedMesh.getPositions().add(currentPosition);
					indexedMesh.getTexCoords().add(currentTexCoord);
					indexedMesh.getNormals().add(normals.get(currentOBJIndex.normalIndex));
				}
			}
			else
			{
				meshIndex = positionIndexMap.get(currentPosition);
				
				if(meshIndex == null)
				{
					meshIndex = indexedMesh.getPositions().size();
					positionIndexMap.put(currentPosition, meshIndex);
					
					indexedMesh.getPositions().add(currentPosition);
					indexedMesh.getTexCoords().add(currentTexCoord);
					indexedMesh.getNormals().add(new Vector3f(0, 0, 0));
				}
			}
			
			indexedMesh.getIndices().add(meshIndex);
			indexedMesh.getTangents().add(new Vector3f(0, 0, 0));
		}// end for each OBJIndex
		
//		Debug.print(m_fullFileName + "  after processing: " + indexedMesh.getPositions().size() + " positions " + indexedMesh.getIndices().size() + "   "+ hasNormal + "NEW!!!!!!!!!");
		
		if(!hasNormal)
		{
			indexedMesh.calcNormals();
		}
		
		indexedMesh.calcTangents();
		
		return indexedMesh;
	}
	
//	public boolean hasMtl()
//	{
//		return m_mtlParser != null;
//	}
	
	public String getFullFileName()
	{
		return m_fullFileName;
	}
	
//	public List<Material> getMaterials()
//	{
//		return m_materials;
//	}
//	
//	public List<List<Vector2i>> getMtlIndexPairs()
//	{
//		return m_mtlIndexPairs;
//	}
}
