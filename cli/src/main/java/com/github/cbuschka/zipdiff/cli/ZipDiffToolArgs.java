package com.github.cbuschka.zipdiff.cli;

import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntryType;
import org.apache.commons.cli.Options;

import java.io.File;
import java.util.Set;

public class ZipDiffToolArgs
{
	private final boolean recurse;
	private boolean showDiffs;
	private boolean usageRequested;
	private boolean showUnchanged;
	private boolean quiet;
	private Options options;
	private File fileA;
	private File fileB;
	private Set<ZipIndexDiffEntryType> entryTypes;

	public ZipDiffToolArgs(Options options, File fileA, File fileB,
						   Set<ZipIndexDiffEntryType> entryTypes,
						   boolean quiet, boolean usageRequested,
						   boolean recurse, boolean showDiffs,
						   boolean showUnchanged)
	{
		this.options = options;
		this.fileA = fileA;
		this.fileB = fileB;
		this.entryTypes = entryTypes;
		this.quiet = quiet;
		this.usageRequested = usageRequested;
		this.recurse = recurse;
		this.showDiffs = showDiffs;
		this.showUnchanged = showUnchanged;
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

	public Set<ZipIndexDiffEntryType> getEntryTypes()
	{
		return entryTypes;
	}

	public boolean isRecurse()
	{
		return recurse;
	}

	public boolean isShowDiffs()
	{
		return showDiffs;
	}

	public boolean isShowUnchanged()
	{
		return showUnchanged;
	}
}
