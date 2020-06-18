package com.github.cbuschka.zipdiff;

import org.apache.commons.cli.Options;

import java.io.File;
import java.util.Set;

public class ZipDiffToolArgs
{
	private final boolean recurse;
	private boolean usageRequested;
	private boolean quiet;
	private Options options;
	private File fileA;
	private File fileB;
	private Set<ZipDiffEntryType> entryTypes;

	public ZipDiffToolArgs(Options options, File fileA, File fileB,
						   Set<ZipDiffEntryType> entryTypes,
						   boolean quiet, boolean usageRequested,
						   boolean recurse)
	{
		this.options = options;
		this.fileA = fileA;
		this.fileB = fileB;
		this.entryTypes = entryTypes;
		this.quiet = quiet;
		this.usageRequested = usageRequested;
		this.recurse = recurse;
	}

	public Options getOptions()
	{
		return options;
	}

	public boolean isUsageRequested()
	{
		return usageRequested;
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

	public boolean isRecurse()
	{
		return recurse;
	}
}
