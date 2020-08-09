package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.diff.ZipIndexDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffHandler;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.util.List;

public class DiffRegisteringZipIndexDiffFilter extends AbstractZipIndexDiffFilter
{
	private boolean diffPresent = false;

	public DiffRegisteringZipIndexDiffFilter(ZipIndexDiffHandler handler)
	{
		super(handler);
	}

	@Override
	public void begin(ZipIndexDiff zipIndexDiff)
	{
		this.diffPresent = false;
		handler.begin(zipIndexDiff);
	}

	@Override
	public void added(ZipIndexEntry otherZipIndexEntry)
	{
		this.diffPresent = true;
		super.added(otherZipIndexEntry);
	}

	@Override
	public void deleted(ZipIndexEntry zipIndexEntry)
	{
		this.diffPresent = true;
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
		this.diffPresent = true;
		super.renamed(zipIndexEntry, otherZipIndexEntry);
	}

	@Override
	public void modified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		this.diffPresent = true;
		super.modified(zipIndexEntry, otherZipIndexEntry);
	}

	@Override
	public void startContentModified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		this.diffPresent = true;
		super.startContentModified(zipIndexEntry, otherZipIndexEntry);
	}

	@Override
	public void modifiedContentChanged(List<String> oldLines, List<String> newLines)
	{
		this.diffPresent = true;
		super.modifiedContentChanged(oldLines, newLines);
	}

	@Override
	public void modifiedContentDeleted(List<String> oldLines)
	{
		this.diffPresent = true;
		handler.modifiedContentDeleted(oldLines);
	}

	@Override
	public void modifiedContentInserted(List<String> newLines)
	{
		this.diffPresent = true;
		super.modifiedContentInserted(newLines);
	}

	@Override
	public void modifiedContentEqual(List<String> oldLines)
	{
		super.modifiedContentEqual(oldLines);
	}

	@Override
	public void endContentModified()
	{
		super.endContentModified();
	}

	@Override
	public void finished()
	{
		super.finished();
	}

	public boolean isDiffPresent()
	{
		return diffPresent;
	}
}
