package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.diff.ZipIndexDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffHandler;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.util.List;

public class DiffRegisteringZipIndexDiffFilter extends AbstractZipIndexDiffFilter
{
	private int diffCount = 0;

	public DiffRegisteringZipIndexDiffFilter(ZipIndexDiffHandler handler)
	{
		super(handler);
	}

	@Override
	public void begin(ZipIndexDiff zipIndexDiff)
	{
		this.diffCount = 0;
		handler.begin(zipIndexDiff);
	}

	@Override
	public void added(ZipIndexEntry otherZipIndexEntry)
	{
		this.diffCount++;
		super.added(otherZipIndexEntry);
	}

	@Override
	public void deleted(ZipIndexEntry zipIndexEntry)
	{
		this.diffCount++;
		super.deleted(zipIndexEntry);
	}

	@Override
	public void unchanged(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		super.unchanged(zipIndexEntry, otherZipIndexEntry);
	}

	@Override
	public void renamed(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		this.diffCount++;
		super.renamed(zipIndexEntry, otherZipIndexEntry);
	}

	@Override
	public void modified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		this.diffCount++;
		super.modified(zipIndexEntry, otherZipIndexEntry);
	}

	@Override
	public void startContentModified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		this.diffCount++;
		super.startContentModified(zipIndexEntry, otherZipIndexEntry);
	}

	@Override
	public void contentModified(ZipIndexEntry zipIndexEntry, List<String> oldLines, ZipIndexEntry otherZipIndexEntry, List<String> newLines)
	{
		super.contentModified(zipIndexEntry, oldLines, otherZipIndexEntry, newLines);
	}

	@Override
	public void contentDeleted(ZipIndexEntry zipIndexEntry, List<String> oldLines, ZipIndexEntry otherZipIndexEntry)
	{
		handler.contentDeleted(zipIndexEntry, oldLines, otherZipIndexEntry);
	}

	@Override
	public void contentAdded(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry, List<String> newLines)
	{
		super.contentAdded(zipIndexEntry, otherZipIndexEntry, newLines);
	}

	@Override
	public void contentUnchanged(ZipIndexEntry zipIndexEntry, List<String> oldLines, ZipIndexEntry otherZipIndexEntry)
	{
		super.contentUnchanged(zipIndexEntry, oldLines, otherZipIndexEntry);
	}

	@Override
	public void endContentModified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		super.endContentModified(zipIndexEntry, otherZipIndexEntry);
	}

	@Override
	public void finished()
	{
		super.finished();
	}

	public int getDiffCount()
	{
		return diffCount;
	}

	public boolean isDiffPresent()
	{
		return this.diffCount > 0;
	}
}
