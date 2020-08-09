package com.github.cbuschka.zipdiff.content_diff;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.util.List;

public class ContentDiff
{
	private ZipIndexEntry zipIndexEntry;
	private ZipIndexEntry otherZipIndexEntry;

	private List<ContentDiffEntry> entries;

	public ContentDiff(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry, List<ContentDiffEntry> entries)
	{
		this.zipIndexEntry = zipIndexEntry;
		this.otherZipIndexEntry = otherZipIndexEntry;
		this.entries = entries;
	}

	public boolean hasChanges()
	{
		for (ContentDiffEntry entry : entries)
		{
			if (entry.getType().isChange())
			{
				return true;
			}
		}

		return false;
	}

	public ZipIndexEntry getZipIndexEntry()
	{
		return zipIndexEntry;
	}

	public ZipIndexEntry getOtherZipIndexEntry()
	{
		return otherZipIndexEntry;
	}

	public List<ContentDiffEntry> getEntries()
	{
		return this.entries;
	}
}
