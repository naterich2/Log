package org.pokepal101.log.pokepal101.log;

import java.io.*;

public class BufferedDataFileWriter
{
	private File dataFile;
	private String[] cache;
	private int index;
	private final int cacheSize = 15;

	public BufferedDataFileWriter (File dF)
	{
		dataFile = dF;
		cache = new String[cacheSize];
		index = 0;
	}

	public void write (String s)
	{
		if (index >= cache.length)
			flush();
		cache[index] = s;
		index++;
//		System.out.println("Wrote: " + s);
	}

	public void flush ()
	{
		if (index > 0)
		{
			try
			{
				PrintWriter pw = new PrintWriter(new FileWriter(dataFile, true));
				for (int i = 0; ((i < index) && (i < cache.length)); i++)
				{
					pw.println(cache[i]);
				}
//				System.out.println("Log: Flushed buffer.");
				cache = new String[cacheSize];
				index = 0;
				pw.close();
			}
			catch (IOException e)
			{
				throw new RuntimeException ("Log: Error flushing buffer: " + e);
			}
		}
	}

	public void erase ()
	{
		flush();
		try
		{
			PrintWriter pw = new PrintWriter(new FileWriter(dataFile, false));
			pw.close();
		}
		catch (IOException e)
		{
			throw new RuntimeException ("Log: Error erasing data: " + e);
		}
	}

	public void close ()
	{
		flush();
	}
}