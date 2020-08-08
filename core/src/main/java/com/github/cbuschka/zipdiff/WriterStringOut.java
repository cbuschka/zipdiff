package com.github.cbuschka.zipdiff;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;

public class WriterStringOut implements StringOut
{
	private Writer out;

	public WriterStringOut(PrintStream out)
	{
		this.out = new OutputStreamWriter(out);
	}

	public WriterStringOut(Writer out)
	{
		this.out = out;
	}

	@Override
	public void write(String s) throws IOException
	{
		this.out.write(s);
	}

	@Override
	public void close() throws IOException
	{
		this.out.close();
	}
}
