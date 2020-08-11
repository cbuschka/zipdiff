package com.github.cbuschka.zipdiff.report;

import com.github.cbuschka.zipdiff.content_diff.ContentDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntryType;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffHandler;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.util.ArrayList;
import java.util.List;

public class ZipIndexDiffRecorder implements ZipIndexDiffHandler
{
	private List<Entry> entries = new ArrayList<>();

	@Override
	public void begin(ZipIndexDiff zipIndexDiff)
	{
	}

	@Override
	public void added(ZipIndexEntry otherZipIndexEntry)
	{
		record(ZipIndexDiffEntryType.ADDED, otherZipIndexEntry, null, null);
	}

	@Override
	public void deleted(ZipIndexEntry zipIndexEntry)
	{
		record(ZipIndexDiffEntryType.DELETED, zipIndexEntry, null, null);
	}

	@Override
	public void unchanged(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		record(ZipIndexDiffEntryType.UNCHANGED, zipIndexEntry, otherZipIndexEntry, null);
	}

	@Override
	public void renamed(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		record(ZipIndexDiffEntryType.RENAMED, zipIndexEntry, otherZipIndexEntry, null);
	}

	@Override
	public void modified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry, ContentDiff contentDiff)
	{
		record(ZipIndexDiffEntryType.MODIFIED, zipIndexEntry, otherZipIndexEntry, contentDiff);
	}

	@Override
	public void finished()
	{
	}

	protected void record(ZipIndexDiffEntryType entryType, ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry, ContentDiff contentDiff)
	{
		this.entries.add(new Entry(entryType, zipIndexEntry, otherZipIndexEntry, contentDiff));
	}

	public List<Entry> getEntries()
	{
		return entries;
	}

	public static class Entry
	{
		public final ZipIndexDiffEntryType type;
		public final ZipIndexEntry zipIndexEntry;
		public final ZipIndexEntry otherZipIndexEntry;
		public final ContentDiff contentDiff;

		public Entry(ZipIndexDiffEntryType type, ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry, ContentDiff contentDiff)
		{
			this.type = type;
			this.zipIndexEntry = zipIndexEntry;
			this.otherZipIndexEntry = otherZipIndexEntry;
			this.contentDiff = contentDiff;
		}
	}
}
