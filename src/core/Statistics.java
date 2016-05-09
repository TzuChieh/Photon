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

package core;

import java.math.BigDecimal;
import java.math.RoundingMode;

import util.LimitedList;
import util.Time;

public final class Statistics
{
	private static final int numAvgedData = 100;
	private static final String UNIT = "k samples/sec";
	
	private static Object            spsLock       = new Object();
	private static LimitedList<Long> numRaysRecord = new LimitedList<>(numAvgedData);
	private static LimitedList<Long> msTimeRecord  = new LimitedList<>(numAvgedData + 1);
	
	static
	{
		msTimeRecord.add(Time.getMilliTime());
	}
	
	public static String getCurrentKsps()
	{
		synchronized(spsLock)
		{
			long totalRays = 0L;
			for(long numRays : numRaysRecord)
			{
				totalRays += numRays;
			}
			
			BigDecimal numTotalRays = new BigDecimal(totalRays);
			BigDecimal msElapsed    = new BigDecimal(msTimeRecord.getFirst() - msTimeRecord.getLast() + 1L);
			
			return numTotalRays.divide(msElapsed, 0, RoundingMode.FLOOR).toPlainString() + UNIT;
		}
	}
	
	public static void addNumRays(long numRays)
	{
		synchronized(spsLock)
		{
			numRaysRecord.add(numRays);
			msTimeRecord.add(Time.getMilliTime());
		}
	}
}
