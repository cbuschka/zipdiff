package com.github.cbuschka.zipdiff;

public class ZipDiffEntry
{
	private ZipDiffEntryType type;

	private ZipIndexEntry zipIndexEntry;

	private ZipIndexEntry otherZipIndexEntry;

	public ZipDiffEntry(ZipDiffEntryType type, ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		this.type = type;
		this.zipIndexEntry = zipIndexEntry;
		this.otherZipIndexEntry = otherZipIndexEntry;
	}

	public ZipIndexEntry getZipIndexEntry()
	{
		return zipIndexEntry;
	}

	public ZipDiffEntryType getType()
	{
		return type;
	}

	public ZipIndexEntry getOtherZipIndexEntry()
	{
		return otherZipIndexEntry;
	}
}
