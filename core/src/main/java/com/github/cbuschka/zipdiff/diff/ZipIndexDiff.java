package com.github.cbuschka.zipdiff.diff;

import com.github.cbuschka.zipdiff.index.ZipIndex;

import java.util.ArrayList;
import java.util.List;

public class ZipIndexDiff
{
	private ZipIndex indexA;
	private ZipIndex indexB;

	private List<ZipIndexDiffEntry> entries = new ArrayList<>();

	public ZipIndexDiff(ZipIndex a, ZipIndex b)
	{
		this.indexA = a;
		this.indexB = b;
	}

	public ZipIndex getIndexA()
	{
		return indexA;
	}

	public ZipIndex getIndexB()
	{
		return indexB;
	}

	public List<ZipIndexDiffEntry> getEntries()
	{
		return this.entries;
	}

	public void addEntry(ZipIndexDiffEntry entry)
	{
		this.entries.add(entry);
	}

	public int size()
	{
		return this.entries.size();
	}

	public ZipIndexDiffEntry getEntryByPath(String path)
	{
		for (ZipIndexDiffEntry entry : this.entries)
		{
			if (entry.getZipIndexEntry() != null && entry.getZipIndexEntry().getPath().equals(path))
			{
				return entry;
			}
		}

		return null;
	}

	public ZipIndexDiffEntry getEntryByOtherPath(String path)
	{
		for (ZipIndexDiffEntry entry : this.entries)
		{
			if (entry.getOtherZipIndexEntry() != null && entry.getOtherZipIndexEntry().getPath().equals(path))
			{
				return entry;
			}
		}

		return null;
	}

	public boolean containsChanges()
	{

		for (ZipIndexDiffEntry entry : this.entries)
		{
			if (entry.getType().isChange())
			{
				return true;
			}
		}

		return false;
	}
}
