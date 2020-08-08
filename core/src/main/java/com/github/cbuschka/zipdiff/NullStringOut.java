package com.github.cbuschka.zipdiff;

import java.io.IOException;
import java.io.Writer;

public class NullStringOut implements StringOut
{
	@Override
	public void write(String s) throws IOException
	{
		// intentionally left blank
	}

	@Override
	public void close() throws IOException
	{
		// intentionally left blank
	}
}
