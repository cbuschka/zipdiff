package com.github.cbuschka.zipdiff.io;

import java.io.IOException;

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
