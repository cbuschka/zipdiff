package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.diff.ZipIndexDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffHandler;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.util.List;

public class ZipIndexDiffFilter implements ZipIndexDiffHandler
{
	private Config config;
	private ZipIndexDiffHandler handler;

	public ZipIndexDiffFilter(Config config, ZipIndexDiffHandler handler)
	{
		this.config = config;
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
	public void modifiedContentChanged(List<String> oldLines, List<String> newLines)
	{
		handler.modifiedContentChanged(oldLines, newLines);
	}

	@Override
	public void modifiedContentDeleted(List<String> oldLines)
	{
		handler.modifiedContentDeleted(oldLines);
	}

	@Override
	public void modifiedContentInserted(List<String> newLines)
	{
		handler.modifiedContentInserted(newLines);
	}

	@Override
	public void modifiedContentEqual(List<String> oldLines)
	{
		handler.modifiedContentEqual(oldLines);
	}

	@Override
	public void endContentModified()
	{
		handler.endContentModified();
	}

	@Override
	public void finished()
	{
		handler.finished();
	}
}
