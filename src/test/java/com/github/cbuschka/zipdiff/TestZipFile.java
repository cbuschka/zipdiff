package com.github.cbuschka.zipdiff;

import org.junit.rules.ExternalResource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class TestZipFile extends ExternalResource
{
	private final String resourcePath;
	private final TestZipFileBuilder zipFileBuilder;

	private File file;

	public static TestZipFile from(String resourcePath)
	{
		return new TestZipFile(resourcePath, null);
	}

	private TestZipFile(String resourcePath, TestZipFileBuilder zipFileBuilder)
	{
		this.resourcePath = resourcePath;
		this.zipFileBuilder = zipFileBuilder;
	}

	public static TestZipFile from(TestZipFileBuilder zipFileBuilder)
	{
		return new TestZipFile(null, zipFileBuilder);
	}

	public File getFile()
	{
		return this.file;
	}

	@Override
	protected void before() throws URISyntaxException, FileNotFoundException, IOException
	{
		if (this.resourcePath != null)
		{
			URL url = Thread.currentThread().getContextClassLoader().getResource(this.resourcePath);
			if (url == null)
			{
				throw new FileNotFoundException("Classpath resource " + this.resourcePath + " not found.");
			}
			URI uri = url.toURI();
			this.file = new File(uri.getPath());
		}
		else
		{
			this.file = File.createTempFile("testzip", ".zip");
			try (FileOutputStream fileOut = new FileOutputStream(this.file))
			{
				this.zipFileBuilder.writeTo(fileOut);
			}
		}
	}
}