package com.github.cbuschka.zipdiff.diff;

public enum ZipIndexDiffEntryType
{
	UNCHANGED,
	RENAMED,
	ADDED,
	DELETED,
	MODIFIED;

	public boolean isChange()
	{
		return this != UNCHANGED;
	}
}
