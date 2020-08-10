package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.content_diff.ContentDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffHandler;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

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
	public void modified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry, ContentDiff contentDiff)
	{
		this.diffCount++;
		super.modified(zipIndexEntry, otherZipIndexEntry, contentDiff);
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
}
