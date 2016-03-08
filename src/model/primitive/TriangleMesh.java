package model.primitive;

import java.util.ArrayList;

import main.Intersection;
import main.Ray;

public class TriangleMesh implements Primitive
{
	private ArrayList<Triangle> m_triangles;
	
	public TriangleMesh()
	{
		m_triangles = new ArrayList<>();
	}
	
	@Override
	public boolean isIntersect(Ray ray, Intersection intersection)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public void addTriangle(Triangle triangle)
	{
		m_triangles.add(triangle);
	}
}
