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

package model.obj;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import math.Vector2f;
import math.Vector3f;

public class IndexedMesh
{
	private ArrayList<Vector3f> positions;
	private ArrayList<Vector2f> texCoords;
	private ArrayList<Vector3f> normals;
	private ArrayList<Vector3f> tangents;
	private ArrayList<Integer>  indices;

	public IndexedMesh()
	{
		positions = new ArrayList<>();
		texCoords = new ArrayList<>();
		normals   = new ArrayList<>();
		tangents  = new ArrayList<>();
		indices   = new ArrayList<>();
	}

	public void calcNormals()
	{
		for(int i = 0; i < indices.size(); i += 3)
		{
			int i0 = indices.get(i);
			int i1 = indices.get(i + 1);
			int i2 = indices.get(i + 2);

			Vector3f v1 = positions.get(i1).sub(positions.get(i0));
			Vector3f v2 = positions.get(i2).sub(positions.get(i0));

			Vector3f normal = v1.cross(v2).normalize();

			normals.get(i0).set(normals.get(i0).add(normal));
			normals.get(i1).set(normals.get(i1).add(normal));
			normals.get(i2).set(normals.get(i2).add(normal));
		}

		for(int i = 0; i < normals.size(); i++)
			normals.get(i).normalizeLocal();
	}

	public void calcTangents()
	{
		for(int i = 0; i < indices.size(); i += 3)
		{
			int i0 = indices.get(i);
			int i1 = indices.get(i + 1);
			int i2 = indices.get(i + 2);

			Vector3f edge1 = positions.get(i1).sub(positions.get(i0));// actual edge vectors
			Vector3f edge2 = positions.get(i2).sub(positions.get(i0));//

			float deltaU1 = texCoords.get(i1).getX() - texCoords.get(i0).getX();// the corresponding ratios
			float deltaV1 = texCoords.get(i1).getY() - texCoords.get(i0).getY();//
			float deltaU2 = texCoords.get(i2).getX() - texCoords.get(i0).getX();//
			float deltaV2 = texCoords.get(i2).getY() - texCoords.get(i0).getY();//

			float determinant = (deltaU1*deltaV2 - deltaU2*deltaV1);
			//TODO: The first 1.0f may need to be changed to 0.0f here.
			float f = determinant == 0 ? 1.0f : 1.0f/determinant;
//			if(determinant == 0)
//			Debug.print("LOL" + determinant);
			
			Vector3f tangent = new Vector3f(0,0,0);
			tangent.setX(f * (deltaV2 * edge1.getX() - deltaV1 * edge2.getX()));
			tangent.setY(f * (deltaV2 * edge1.getY() - deltaV1 * edge2.getY()));
			tangent.setZ(f * (deltaV2 * edge1.getZ() - deltaV1 * edge2.getZ()));
			
			// test code start
			
//			Vector3f normal = normals.get(i0);
//			Vector3f biTangent = new Vector3f(0, 0, 0);
//			biTangent.setX(f * (deltaU1 * edge2.getX() - deltaU2 * edge1.getX()));
//			biTangent.setY(f * (deltaU1 * edge2.getY() - deltaU2 * edge1.getY()));
//			biTangent.setZ(f * (deltaU1 * edge2.getZ() - deltaU2 * edge1.getZ()));
//			
//			if(normal.cross(tangent).dot(biTangent) < 0.0f)
//			{
//				tangent.mulLocal(-1.0f);
//				Debug.print("LOL");
//			}
//
//			tangents.get(i0).set(tangent);
//			tangents.get(i1).set(tangent);
//			tangents.get(i2).set(tangent);
			
			// test code end
//			tangent.normalizeLocal();
			
			tangents.get(i0).set(tangents.get(i0).add(tangent));
			tangents.get(i1).set(tangents.get(i1).add(tangent));
			tangents.get(i2).set(tangents.get(i2).add(tangent));
		}// end for

		for(int i = 0; i < tangents.size(); i++)
		{
//			tangents.get(i).set(tangents.get(i).normalize());
			tangents.get(i).normalizeLocal();
		}
	}

	public ArrayList<Vector3f> getPositions() { return positions; }
	public ArrayList<Vector2f> getTexCoords() { return texCoords; }
	public ArrayList<Vector3f> getNormals()   { return normals; }
	public ArrayList<Vector3f> getTangents()  { return tangents; }
	public ArrayList<Integer>  getIndices()   { return indices; }
	
	public void negativeTexCoordY()
	{
		for(Vector2f texCoord : texCoords)
		{
			texCoord.y *= -1;
		}
	}
	
//	public AABBMinMaxVertex findAABB()
//	{
//		// min, max value of the mesh's position components
//		float m_minX = 0, m_maxX = 0,
//			  m_minY = 0, m_maxY = 0,
//			  m_minZ = 0, m_maxZ = 0;
//		
//		for(Vector3f position : positions)
//		{
//			     if(position.x > m_maxX) m_maxX = position.x;
//			else if(position.x < m_minX) m_minX = position.x;
//			     
//			     if(position.y > m_maxY) m_maxY = position.y;
//			else if(position.y < m_minY) m_minY = position.y;
//			     
//			     if(position.z > m_maxZ) m_maxZ = position.z;
//			else if(position.z < m_minZ) m_minZ = position.z;
//		}
//		
//		return new AABBMinMaxVertex(new Vector3f(m_minX, m_minY, m_minZ),
//				                    new Vector3f(m_maxX, m_maxY, m_maxZ));
//	}
//	
//	public AABBMinMaxVertex findAABB(List<Vector2i> indexPairs)
//	{
//		// min, max value of the mesh's position components
//		float m_minX = 0, m_maxX = 0,
//			  m_minY = 0, m_maxY = 0,
//			  m_minZ = 0, m_maxZ = 0;
//		
//		for(Vector2i indexPair : indexPairs)
//		{
//			for(int i = indexPair.x; i <= indexPair.y; i++)
//			{
//				Vector3f position = positions.get(indices.get(i));
//				
//				     if(position.x > m_maxX) m_maxX = position.x;
//				else if(position.x < m_minX) m_minX = position.x;
//				     
//				     if(position.y > m_maxY) m_maxY = position.y;
//				else if(position.y < m_minY) m_minY = position.y;
//				     
//				     if(position.z > m_maxZ) m_maxZ = position.z;
//				else if(position.z < m_minZ) m_minZ = position.z;
//			}
//		}
//
//		return new AABBMinMaxVertex(new Vector3f(m_minX, m_minY, m_minZ),
//                                    new Vector3f(m_maxX, m_maxY, m_maxZ));
//	}
//	
//	public AABBMinMaxVertex findAABB(Vector2i indexPair)
//	{
//		// min, max value of the mesh's position components
//		float m_minX = 0, m_maxX = 0,
//			  m_minY = 0, m_maxY = 0,
//			  m_minZ = 0, m_maxZ = 0;
//		
//		for(int i = indexPair.x; i <= indexPair.y; i++)
//		{
//			Vector3f position = positions.get(indices.get(i));
//			
//			     if(position.x > m_maxX) m_maxX = position.x;
//			else if(position.x < m_minX) m_minX = position.x;
//			     
//			     if(position.y > m_maxY) m_maxY = position.y;
//			else if(position.y < m_minY) m_minY = position.y;
//			     
//			     if(position.z > m_maxZ) m_maxZ = position.z;
//			else if(position.z < m_minZ) m_minZ = position.z;
//		}
//
//		return new AABBMinMaxVertex(new Vector3f(m_minX, m_minY, m_minZ),
//                                    new Vector3f(m_maxX, m_maxY, m_maxZ));
//	}
}
