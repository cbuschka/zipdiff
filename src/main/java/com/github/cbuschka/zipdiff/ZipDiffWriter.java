package com.github.cbuschka.zipdiff;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;

public class ZipDiffWriter implements Closeable
{
	private Writer wr;

	public ZipDiffWriter(Writer wr)
	{
		this.wr = wr;
	}

	public void write(ZipDiff diff) throws IOException
	{
		for (ZipDiffEntry entry : diff.getEntries())
		{
			ZipDiffEntryType entryType = entry.getType();
			switch (entryType)
			{
				case ADDED:
					this.write(entryType, entry.getZipIndexEntry().getPath());
					break;
				case REMOVED:
					this.write(entryType, entry.getZipIndexEntry().getPath());
					break;
				case UNCHANGED:
					this.write(entryType, entry.getZipIndexEntry().getPath());
					break;
				case RENAMED:
					this.write(entryType, entry.getZipIndexEntry().getPath(), entry.getOtherZipIndexEntry().getPath());
					break;
				case CHANGED:
					this.write(entryType, entry.getZipIndexEntry().getPath());
					break;
				default:
					throw new IllegalStateException("Unexpected entry type: " + entryType);
			}
		}
	}

	private void write(ZipDiffEntryType entryType, String path) throws IOException
	{
		String s = String.format("%s: %s\n", entryType.name(), path);
		this.wr.write(s);
	}

	private void write(ZipDiffEntryType entryType, String pathA, String pathB) throws IOException
	{
		String s = String.format("%s: %s %s\n", entryType.name(), pathA, pathB);
		this.wr.write(s);
	}

	@Override
	public void close() throws IOException
	{
		this.wr.close();
	}
}
