package com.github.cbuschka.zipdiff;

import java.util.ArrayList;
import java.util.List;

public class ZipDiff
{
	private List<ZipDiffEntry> entries = new ArrayList<>();

	public Iterable<ZipDiffEntry> getEntries()
	{
		return this.entries;
	}

	public void addEntry(ZipDiffEntry entry)
	{
		this.entries.add(entry);
	}

	public int size()
	{
		return this.entries.size();
	}

	public ZipDiffEntry getEntryByPath(String path)
	{
		for (ZipDiffEntry entry : this.entries)
		{
			if (entry.getZipIndexEntry() != null && entry.getZipIndexEntry().getPath().equals(path))
			{
				return entry;
			}
		}

		return null;
	}

	public ZipDiffEntry getEntryByOtherPath(String path)
	{
		for (ZipDiffEntry entry : this.entries)
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

		for (ZipDiffEntry entry : this.entries)
		{
			if (entry.getType().isChange())
			{
				return true;
			}
		}

		return false;
	}
}
