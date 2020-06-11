package com.github.cbuschka.zipdiff;

import java.io.IOException;

public class NullZipDiffWriter implements ZipDiffWriter
{
	@Override
	public void write(ZipDiff diff) throws IOException
	{
	}

	@Override
	public void close() throws IOException
	{
	}
}
