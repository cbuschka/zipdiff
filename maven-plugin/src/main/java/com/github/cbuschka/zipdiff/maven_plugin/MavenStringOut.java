package com.github.cbuschka.zipdiff.maven_plugin;

import com.github.cbuschka.zipdiff.io.StringOut;
import org.apache.maven.plugin.logging.Log;

import java.io.IOException;

class MavenStringOut implements StringOut
{
	private Log log;

	public MavenStringOut(Log log)
	{
		this.log = log;
	}

	@Override
	public void write(String s) throws IOException
	{
		if (s.endsWith("\n"))
		{
			s = s.substring(0, s.length() - "\n".length());
		}
		this.log.info(s);
	}

	@Override
	public void close() throws IOException
	{
		this.log = null;
	}
}
