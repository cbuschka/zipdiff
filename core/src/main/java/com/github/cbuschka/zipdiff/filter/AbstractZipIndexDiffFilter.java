package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.content_diff.ContentDiff;
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
	public void modified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry, ContentDiff contentDiff)
	{
		handler.modified(zipIndexEntry, otherZipIndexEntry, contentDiff);
	}

	@Override
	public void finished()
	{
		handler.finished();
	}
}
