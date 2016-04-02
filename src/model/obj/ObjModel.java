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

import math.Transform;
import math.Vector3f;
import math.material.AbradedOpaque;
import math.material.AbradedTranslucent;
import math.material.Material;
import model.Model;
import model.boundingVolume.AABB;
import model.primitive.Primitive;
import model.primitive.Triangle;
import model.primitive.TriangleMesh;

public class ObjModel implements Model
{
	private TriangleMesh m_triangleMesh;
	private Material     m_defaultMaterial;
	private AABB         m_aabb;
	private Transform    m_transform;
	
	public ObjModel(String fullFilename)
	{
		m_transform = new Transform();
		
		m_triangleMesh = new TriangleMesh();
		m_triangleMesh.setModel(this);
		
		
//		m_defaultMaterial = new AbradedOpaque();
		AbradedTranslucent matl = new AbradedTranslucent();
		m_defaultMaterial = matl;
		matl.setIor(1.5f);
		matl.setRoughness(0.0f);
		
		IndexedMesh indexedMesh = new ObjParser(fullFilename).toIndexedMesh();
		
		for(int i = 0; i < indexedMesh.getIndices().size(); i += 3)
		{
			Vector3f posA = indexedMesh.getPositions().get(indexedMesh.getIndices().get(i));
			Vector3f posB = indexedMesh.getPositions().get(indexedMesh.getIndices().get(i + 1));
			Vector3f posC = indexedMesh.getPositions().get(indexedMesh.getIndices().get(i + 2));
			
			m_triangleMesh.addTriangle(new Triangle(posA, posB, posC));
		}
	}
	
	@Override
	public Primitive getPrimitive()
	{
		return m_triangleMesh;
	}

	@Override
	public Material getMaterial()
	{
		return m_defaultMaterial;
	}

	@Override
	public Transform getTransform()
	{
		return m_transform;
	}
}
