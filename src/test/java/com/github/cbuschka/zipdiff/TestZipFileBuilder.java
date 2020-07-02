package com.github.cbuschka.zipdiff;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TestZipFileBuilder
{
	private List<Entry> entries = new ArrayList<>();

	public static TestZipFileBuilder newZipFile()
	{
		return new TestZipFileBuilder();
	}

	TestZipFileBuilder()
	{
	}

	public TestZipFileBuilder withEntry(String path, byte[] data)
	{
		this.entries.add(new Entry(path, data));
		return this;
	}

	public TestZipFileBuilder withEntry(String path, TestZipFileBuilder zipFileBuilder)
	{
		return withEntry(path, zipFileBuilder.toBytes());
	}

	public TestZipFileBuilder withEntry(String path, String s)
	{
		return withEntry(path, s.getBytes(StandardCharsets.UTF_8));
	}

	public byte[] toBytes()
	{
		try (ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();)
		{
			writeTo(bytesOut);
			return bytesOut.toByteArray();
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	public void writeTo(OutputStream out) throws IOException
	{
		try (ZipOutputStream zipOut = new ZipOutputStream(out);)
		{
			for (Entry e : this.entries)
			{
				zipOut.putNextEntry(new ZipEntry(e.path));
				zipOut.write(e.data);
				zipOut.closeEntry();
			}
		}
	}

	private static class Entry
	{
		private String path;
		private byte[] data;

		public Entry(String path, byte[] data)
		{
			this.path = path;
			this.data = data;
		}
	}
}