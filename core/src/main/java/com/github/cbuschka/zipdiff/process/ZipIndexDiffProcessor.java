package com.github.cbuschka.zipdiff.process;

import com.github.cbuschka.zipdiff.content_diff.ContentDiff;
import com.github.cbuschka.zipdiff.content_diff.ContentDiffer;
import com.github.cbuschka.zipdiff.content_diff.ContentDifferProvider;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntry;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntryType;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffHandler;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.io.IOException;

public class ZipIndexDiffProcessor
{
	private ZipIndexDiffHandler handler;

	public ZipIndexDiffProcessor(ZipIndexDiffHandler handler)
	{
		this.handler = handler;
	}

	public void process(ZipIndexDiff diff) throws IOException
	{
		this.handler.begin(diff);
		for (ZipIndexDiffEntry entry : diff.getEntries())
		{
			ZipIndexDiffEntryType entryType = entry.getType();
			switch (entryType)
			{
				case ADDED:
					this.handler.added(entry.getOtherZipIndexEntry());
					break;
				case DELETED:
					this.handler.deleted(entry.getZipIndexEntry());
					break;
				case UNCHANGED:
					this.handler.unchanged(entry.getZipIndexEntry(), entry.getOtherZipIndexEntry());
					break;
				case RENAMED:
					this.handler.renamed(entry.getZipIndexEntry(), entry.getOtherZipIndexEntry());
					break;
				case MODIFIED:
					handleModified(entry.getZipIndexEntry(), entry.getOtherZipIndexEntry());
					break;
				default:
					throw new IllegalStateException("Unexpected entry type: " + entryType);
			}
		}
		this.handler.finished();
	}

	private void handleModified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		ContentDiffer contentDiffer = ContentDifferProvider.getContentDifferFor(zipIndexEntry, otherZipIndexEntry);
		ContentDiff contentDiff = contentDiffer.diff(zipIndexEntry, otherZipIndexEntry);
		if (contentDiff.hasChanges())
		{
			this.handler.modified(zipIndexEntry, otherZipIndexEntry, contentDiff);
		}
		else
		{
			this.handler.unchanged(zipIndexEntry, otherZipIndexEntry);
		}
	}
}
