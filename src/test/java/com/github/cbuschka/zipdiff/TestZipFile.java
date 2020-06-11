package com.github.cbuschka.zipdiff;

import org.junit.rules.ExternalResource;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class TestZipFile extends ExternalResource
{
	private String resourcePath;

	private File file;

	public TestZipFile(String resourcePath)
	{
		this.resourcePath = resourcePath;
	}

	public File getFile()
	{
		return this.file;
	}

	@Override
	protected void before() throws URISyntaxException
	{
		URL url = Thread.currentThread().getContextClassLoader().getResource(this.resourcePath);
		URI uri = url.toURI();
		this.file = new File(uri.getPath());
	}
}