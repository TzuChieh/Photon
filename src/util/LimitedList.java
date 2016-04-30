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

package util;

import java.util.Iterator;
import java.util.LinkedList;

public class LimitedList<T> implements Iterable<T>
{
	// NOTICE: This class is not thread-safe.
	
	private LinkedList<T> m_linkedList;
	private int           m_maxSize;
	
	public LimitedList(int maxSize)
	{
		super();
		
		m_maxSize    = maxSize;
		m_linkedList = new LinkedList<>();
	}
	
	public void add(T element)
	{
		m_linkedList.addFirst(element);
		
		if(m_linkedList.size() > m_maxSize)
		{
			m_linkedList.removeLast();
		}
	}

	@Override
	public Iterator<T> iterator()
	{
		return m_linkedList.iterator();
	}
	
	public int size()
	{
		return m_linkedList.size();
	}
	
	public T getFirst()
	{
		return m_linkedList.getFirst();
	}
	
	public T getLast()
	{
		return m_linkedList.getLast();
	}
}
