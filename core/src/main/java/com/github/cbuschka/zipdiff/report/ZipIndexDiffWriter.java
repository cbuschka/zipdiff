package com.github.cbuschka.zipdiff.report;

import com.github.cbuschka.zipdiff.diff.ZipIndexDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntryType;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffHandler;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;
import com.github.cbuschka.zipdiff.io.StringOut;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;

public class ZipIndexDiffWriter implements ZipIndexDiffHandler
{
	private static final String DIFF_S_S = "  DIFF: %s%s\n";

	private StringOut stringOut;

	public ZipIndexDiffWriter(StringOut stringOut)
	{
		this.stringOut = stringOut;
	}

	@Override
	public void begin(ZipIndexDiff zipIndexDiff)
	{
		// left empty
	}

	@Override
	public void added(ZipIndexEntry otherZipIndexEntry)
	{
		write(ZipIndexDiffEntryType.ADDED, otherZipIndexEntry.getFullyQualifiedPath());
	}

	@Override
	public void deleted(ZipIndexEntry zipIndexEntry)
	{
		write(ZipIndexDiffEntryType.DELETED, zipIndexEntry.getFullyQualifiedPath());
	}

	@Override
	public void unchanged(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		write(ZipIndexDiffEntryType.UNCHANGED, zipIndexEntry.getFullyQualifiedPath());
	}

	@Override
	public void renamed(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		write(ZipIndexDiffEntryType.RENAMED, zipIndexEntry.getFullyQualifiedPath(), otherZipIndexEntry.getFullyQualifiedPath());
	}

	@Override
	public void modified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		write(ZipIndexDiffEntryType.MODIFIED, zipIndexEntry.getFullyQualifiedPath(), otherZipIndexEntry.getFullyQualifiedPath());
	}

	@Override
	public void finished()
	{
		try
		{
			this.stringOut.close();
		}
		catch (IOException ex)
		{
			throw new UndeclaredThrowableException(ex);
		}
	}

	private void write(ZipIndexDiffEntryType entryType, String path)
	{
		try
		{
			String s = String.format("%s: %s\n", entryType.name(), path);
			this.stringOut.write(s);
		}
		catch (IOException ex)
		{
			throw new UndeclaredThrowableException(ex);
		}
	}

	private void write(ZipIndexDiffEntryType entryType, String pathA, String pathB)
	{
		try
		{
			String s = String.format("%s: %s %s\n", entryType.name(), pathA, pathB);
			this.stringOut.write(s);
		}
		catch (IOException ex)
		{
			throw new UndeclaredThrowableException(ex);
		}
	}


	@Override
	public void startContentModified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		write(ZipIndexDiffEntryType.MODIFIED, zipIndexEntry.getFullyQualifiedPath());
	}

	@Override
	public void endContentModified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
	}

	public void contentAdded(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry, List<String> targetLines)
	{
		try
		{
			for (String line : targetLines)
			{
				this.stringOut.write(String.format(DIFF_S_S, "+", line.trim()));
			}
		}
		catch (IOException ex)
		{
			throw new UndeclaredThrowableException(ex);
		}

	}

	public void contentDeleted(ZipIndexEntry zipIndexEntry, List<String> sourceLines, ZipIndexEntry otherZipIndexEntry)
	{
		try
		{
			for (String line : sourceLines)
			{
				this.stringOut.write(String.format(DIFF_S_S, "-", line.trim()));
			}
		}
		catch (IOException ex)
		{
			throw new UndeclaredThrowableException(ex);
		}

	}


	public void contentUnchanged(ZipIndexEntry zipIndexEntry, List<String> sourceLines, ZipIndexEntry otherZipIndexEntry)
	{
		try
		{
			for (String line : sourceLines)
			{
				this.stringOut.write(String.format(DIFF_S_S, "=", line.trim()));
			}
		}
		catch (IOException ex)
		{
			throw new UndeclaredThrowableException(ex);
		}
	}

	public void contentModified(ZipIndexEntry zipIndexEntry, List<String> sourceLines, ZipIndexEntry otherZipIndexEntry, List<String> targetLines)
	{
		try
		{
			for (String line : sourceLines)
			{
				this.stringOut.write(String.format(DIFF_S_S, "-", line.trim()));
			}
			for (String line : targetLines)
			{
				this.stringOut.write(String.format(DIFF_S_S, "+", line.trim()));
			}
		}
		catch (IOException ex)
		{
			throw new UndeclaredThrowableException(ex);
		}
	}
}
