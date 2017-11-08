package com.github.cbuschka.zipdiff;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UnclosableInputStream extends FilterInputStream
{
	public UnclosableInputStream(InputStream in)
	{
		super(in);
	}

	@Override
	public void close() throws IOException
	{
		// intentionally skipped
	}
}
