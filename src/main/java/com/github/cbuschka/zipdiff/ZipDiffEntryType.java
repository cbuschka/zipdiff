package com.github.cbuschka.zipdiff;

public enum ZipDiffEntryType
{
	UNCHANGED,
	RENAMED,
	ADDED,
	REMOVED,
	CHANGED;

	public boolean isChange()
	{
		return this != UNCHANGED;
	}
}
