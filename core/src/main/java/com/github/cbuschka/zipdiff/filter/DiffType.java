package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.content_diff.ContentDiffType;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntryType;

import java.util.NoSuchElementException;

public enum DiffType
{
	UNCHANGED(ZipIndexDiffEntryType.UNCHANGED, null),
	RENAMED(ZipIndexDiffEntryType.RENAMED, null),
	ADDED(ZipIndexDiffEntryType.ADDED, null),
	DELETED(ZipIndexDiffEntryType.DELETED, null),
	MODIFIED(ZipIndexDiffEntryType.MODIFIED, null),

	CONTENT_UNCHANGED(null, ContentDiffType.CONTENT_UNCHANGED),
	CONTENT_MODIFIED(null, ContentDiffType.CONTENT_MODIFIED),
	CONTENT_ADDED(null, ContentDiffType.CONTENT_ADDED),
	CONTENT_DELETED(null, ContentDiffType.CONTENT_DELETED);

	private ZipIndexDiffEntryType zipIndexDiffEntryType;
	private ContentDiffType contentDiffType;

	public static DiffType instanceFor(ContentDiffType contentDiffType)
	{
		if (contentDiffType == null)
		{
			return null;
		}

		for (DiffType curr : values())
		{
			if (curr.contentDiffType == contentDiffType)
			{
				return curr;
			}
		}

		throw new NoSuchElementException("No diff type for " + contentDiffType + ".");
	}

	public static DiffType instanceFor(ZipIndexDiffEntryType zipIndexDiffEntryType)
	{
		if (zipIndexDiffEntryType == null)
		{
			return null;
		}

		for (DiffType curr : values())
		{
			if (curr.zipIndexDiffEntryType == zipIndexDiffEntryType)
			{
				return curr;
			}
		}

		throw new NoSuchElementException("No diff type for " + zipIndexDiffEntryType + ".");
	}

	DiffType(ZipIndexDiffEntryType zipIndexDiffEntryType, ContentDiffType contentDiffType)
	{
		this.zipIndexDiffEntryType = zipIndexDiffEntryType;
		this.contentDiffType = contentDiffType;
	}
}
