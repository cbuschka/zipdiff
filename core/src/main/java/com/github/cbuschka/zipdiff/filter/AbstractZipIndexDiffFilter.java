package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.diff.ZipIndexDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffHandler;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.util.List;

public abstract class AbstractZipIndexDiffFilter implements ZipIndexDiffHandler
{
	protected ZipIndexDiffHandler handler;

	protected AbstractZipIndexDiffFilter(ZipIndexDiffHandler handler)
	{
		this.handler = handler;
	}

	@Override
	public void begin(ZipIndexDiff zipIndexDiff)
	{
		handler.begin(zipIndexDiff);
	}

	@Override
	public void added(ZipIndexEntry otherZipIndexEntry)
	{
		handler.added(otherZipIndexEntry);
	}

	@Override
	public void deleted(ZipIndexEntry zipIndexEntry)
	{
		handler.deleted(zipIndexEntry);
	}

	@Override
	public void unchanged(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		handler.unchanged(zipIndexEntry, otherZipIndexEntry);
	}

	@Override
	public void renamed(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		handler.renamed(zipIndexEntry, otherZipIndexEntry);
	}

	@Override
	public void modified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		handler.modified(zipIndexEntry, otherZipIndexEntry);
	}

	@Override
	public void startContentModified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		handler.startContentModified(zipIndexEntry, otherZipIndexEntry);
	}

	@Override
	public void contentModified(ZipIndexEntry zipIndexEntry, List<String> oldLines, ZipIndexEntry otherZipIndexEntry, List<String> newLines)
	{
		handler.contentModified(zipIndexEntry, oldLines, otherZipIndexEntry, newLines);
	}

	@Override
	public void contentDeleted(ZipIndexEntry zipIndexEntry, List<String> oldLines, ZipIndexEntry otherZipIndexEntry)
	{
		handler.contentDeleted(zipIndexEntry, oldLines, otherZipIndexEntry);
	}

	@Override
	public void contentAdded(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry, List<String> newLines)
	{
		handler.contentAdded(zipIndexEntry, otherZipIndexEntry, newLines);
	}

	@Override
	public void contentUnchanged(ZipIndexEntry zipIndexEntry, List<String> oldLines, ZipIndexEntry otherZipIndexEntry)
	{
		handler.contentUnchanged(zipIndexEntry, oldLines, otherZipIndexEntry);
	}

	@Override
	public void endContentModified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		handler.endContentModified(zipIndexEntry, otherZipIndexEntry);
	}

	@Override
	public void finished()
	{
		handler.finished();
	}
}
