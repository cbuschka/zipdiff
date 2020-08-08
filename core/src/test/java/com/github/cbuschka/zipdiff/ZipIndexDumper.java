package com.github.cbuschka.zipdiff;

import com.github.cbuschka.zipdiff.index.ZipIndex;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.io.IOException;
import java.io.Writer;

public class ZipIndexDumper
{
	private Writer wr;

	public ZipIndexDumper(Writer wr)
	{
		this.wr = wr;
	}

	public void dump(ZipIndex zipIndex) throws IOException
	{
		dump(zipIndex, 0);
	}

	private void dump(ZipIndex zipIndex, int i) throws IOException
	{
		writeLineWithIndent(i, zipIndex.getPath());
		for (ZipIndexEntry entry : zipIndex.entries())
		{
			writeLineWithIndent(i + 1, String.format("path=%s\tsize=%d\tcrc32=%s\tsha256=%s", entry.getPath(),
					entry.getSize(),
					entry.getCrc() != 0 ? Long.toHexString(entry.getCrc()) : "none",
					entry.getChecksum().toString(16)
			));
			if (entry.getZipIndex() != null)
			{
				dump(entry.getZipIndex(), i + 1);
			}
		}
	}

	private void writeLineWithIndent(int i, String s) throws IOException
	{
		writeIndent(i);
		wr.write(s);
		wr.write("\n");
	}

	private void writeIndent(int n) throws IOException
	{
		for (int i = 0; i < n; ++i)
		{
			wr.write("  ");
		}
	}
}
