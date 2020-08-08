package com.github.cbuschka.zipdiff.diff;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

public class ZipIndexDiffEntry
{
	private ZipIndexDiffEntryType type;

	private ZipIndexEntry zipIndexEntry;

	private ZipIndexEntry otherZipIndexEntry;

	public ZipIndexDiffEntry(ZipIndexDiffEntryType type, ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		this.type = type;
		this.zipIndexEntry = zipIndexEntry;
		this.otherZipIndexEntry = otherZipIndexEntry;
	}

	public ZipIndexEntry getZipIndexEntry()
	{
		return zipIndexEntry;
	}

	public ZipIndexDiffEntryType getType()
	{
		return type;
	}

	public ZipIndexEntry getOtherZipIndexEntry()
	{
		return otherZipIndexEntry;
	}
}
