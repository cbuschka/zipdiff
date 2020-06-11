package com.github.cbuschka.zipdiff;

public enum ZipDiffEntryType
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
