package com.github.cbuschka.zipdiff;

import java.io.File;
import java.util.Set;

public class ZipDiffToolArgs
{
	private boolean quiet;
	private File fileA;
	private File fileB;
	private Set<ZipDiffEntryType> entryTypes;

	public ZipDiffToolArgs(File fileA, File fileB, Set<ZipDiffEntryType> entryTypes, boolean quiet)
	{
		this.fileA = fileA;
		this.fileB = fileB;
		this.entryTypes = entryTypes;
		this.quiet = quiet;
	}

	public boolean isQuiet()
	{
		return quiet;
	}

	public File getFileA()
	{
		return this.fileA;
	}

	public File getFileB()
	{
		return this.fileB;
	}

	public Set<ZipDiffEntryType> getEntryTypes()
	{
		return entryTypes;
	}
}
