package com.github.cbuschka.zipdiff;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffAlgorithmListener;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Patch;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StreamZipDiffWriter implements ZipDiffWriter
{
	private ContentInfoUtil contentInfoUtil = new ContentInfoUtil();
	private Writer wr;
	private ZipDiffToolArgs toolArgs;

	public StreamZipDiffWriter(ZipDiffToolArgs toolArgs)
	{
		this.toolArgs = toolArgs;
		this.wr = new OutputStreamWriter(System.out, Charset.defaultCharset());
	}

	public void write(ZipDiff diff) throws IOException
	{
		for (ZipDiffEntry entry : diff.getEntries())
		{
			ZipDiffEntryType entryType = entry.getType();
			switch (entryType)
			{
				case ADDED:
					this.write(entryType, entry.getOtherZipIndexEntry().getFullyQualifiedPath());
					break;
				case DELETED:
					this.write(entryType, entry.getZipIndexEntry().getFullyQualifiedPath());
					break;
				case UNCHANGED:
					this.write(entryType, entry.getZipIndexEntry().getFullyQualifiedPath());
					break;
				case RENAMED:
					this.write(entryType, entry.getZipIndexEntry().getFullyQualifiedPath(), entry.getOtherZipIndexEntry().getFullyQualifiedPath());
					break;
				case MODIFIED:
					this.writeModified(entryType, entry.getZipIndexEntry(), entry.getOtherZipIndexEntry());
					break;
				default:
					throw new IllegalStateException("Unexpected entry type: " + entryType);
			}
		}
	}

	private void write(ZipDiffEntryType entryType, String path) throws IOException
	{
		String s = String.format("%s: %s%n", entryType.name(), path);
		this.wr.write(s);
	}

	private void write(ZipDiffEntryType entryType, String pathA, String pathB) throws IOException
	{
		String s = String.format("%s: %s %s%n", entryType.name(), pathA, pathB);
		this.wr.write(s);
	}

	private void writeModified(ZipDiffEntryType entryType, ZipIndexEntry entry, ZipIndexEntry other) throws IOException
	{
		String s = String.format("%s: %s %s%n", entryType.name(), entry.getFullyQualifiedPath(), other.getFullyQualifiedPath());
		this.wr.write(s);

		if (this.toolArgs.isShowDiffs())
		{
			ContentInfo aContentInfo = contentInfoUtil.findMatch(entry.getDataStream());
			ContentInfo bContentInfo = contentInfoUtil.findMatch(other.getDataStream());
			if (aContentInfo.getMimeType().equals(bContentInfo.getMimeType()) && aContentInfo.getMimeType().startsWith("text/"))
			{
				try
				{
					String aText = IOUtils.toString(entry.getDataStream(), StandardCharsets.UTF_8);
					String bText = IOUtils.toString(other.getDataStream(), StandardCharsets.UTF_8);
					Patch<String> patch = DiffUtils.diff(aText, bText, (DiffAlgorithmListener) null);
					this.wr.write(String.valueOf(patch));
				}
				catch (DiffException ex)
				{
					throw new RuntimeException(ex);
				}
			}
		}
	}

	@Override
	public void close() throws IOException
	{
		this.wr.close();
	}
}
