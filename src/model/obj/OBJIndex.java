package model.obj;

public class OBJIndex
{
	public int positionIndex;
	public int texCoordIndex;
	public int normalIndex;

	@Override
	public boolean equals(Object obj)
	{
		OBJIndex index = (OBJIndex)obj;

		return positionIndex == index.positionIndex &&
			   texCoordIndex == index.texCoordIndex &&
			   normalIndex   == index.normalIndex;
	}
	
	private static final int BASE       = 17;
	private static final int MULTIPLIER = 31;

	@Override
	public int hashCode()
	{
		int result = BASE;

		result = MULTIPLIER * result + positionIndex;
		result = MULTIPLIER * result + texCoordIndex;
		result = MULTIPLIER * result + normalIndex;

		return result;
	}
	
	@Override
	public String toString()
	{
		return "[" + 
	                 positionIndex + "," + 
				     texCoordIndex + "," + 
				     normalIndex + 
			   "]";
	}
}
