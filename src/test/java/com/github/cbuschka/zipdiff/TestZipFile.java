package com.github.cbuschka.zipdiff;

import org.junit.rules.ExternalResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TestZipFile extends ExternalResource
{
	private File zipFile;
	private int entryCount = 1;

	public static TestZipFile newZipFile()
	{
		return new TestZipFile();
	}

	public TestZipFile withEntryCount(int entryCount)
	{
		this.entryCount = entryCount;
		return this;
	}

	public String getPath()
	{
		checkCreated();

		return zipFile.getPath();
	}

	private void checkNotCreated()
	{
		if (this.zipFile != null)
		{
			throw new IllegalStateException("Already created.");
		}
	}

	private void checkCreated()
	{
		if (this.zipFile == null)
		{
			throw new IllegalStateException("Not created, yet.");
		}
	}

	@Override
	protected void before() throws IOException
	{
		checkNotCreated();

		this.zipFile = File.createTempFile("junittest", ".zip");
		try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(this.zipFile), Charset.defaultCharset());)
		{
			for (int i = 0; i < this.entryCount; ++i)
			{
				zipOut.putNextEntry(new ZipEntry(String.valueOf("file" + i)));
				zipOut.closeEntry();
			}
		}
	}

	@Override
	protected void after()
	{
		if (this.zipFile != null)
		{
			this.zipFile.delete();
		}
	}
}