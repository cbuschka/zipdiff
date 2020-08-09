package com.github.cbuschka.zipdiff.process;

import com.github.cbuschka.zipdiff.content_diff.ContentDiff;
import com.github.cbuschka.zipdiff.content_diff.ContentDiffEntry;
import com.github.cbuschka.zipdiff.content_diff.ContentDiffer;
import com.github.cbuschka.zipdiff.content_diff.ContentDifferProvider;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntry;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntryType;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffHandler;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.io.IOException;
import java.util.Optional;

public class ZipIndexDiffProcessor
{
	private ZipIndexDiffHandler handler;
	private boolean showDiffs;

	public ZipIndexDiffProcessor(ZipIndexDiffHandler handler, boolean showDiffs)
	{
		this.showDiffs = showDiffs;
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
		Optional<ContentDiffer> optContentDiffer = ContentDifferProvider.get(zipIndexEntry, otherZipIndexEntry);
		if (this.showDiffs && optContentDiffer.isPresent())
		{
			ContentDiffer contentDiffer = optContentDiffer.get();
			ContentDiff contentDiff = contentDiffer.diff(zipIndexEntry, otherZipIndexEntry);
			this.handler.startContentModified(zipIndexEntry, otherZipIndexEntry);
			for (ContentDiffEntry entry : contentDiff.getEntries())
			{
				switch (entry.getType())
				{
					case CONTENT_ADDED:
						this.handler.contentAdded(zipIndexEntry, otherZipIndexEntry, entry.getNewLines());
						break;
					case CONTENT_DELETED:
						this.handler.contentDeleted(zipIndexEntry, entry.getOldLines(), otherZipIndexEntry);
						break;
					case CONTENT_UNCHANGED:
						this.handler.contentUnchanged(zipIndexEntry, entry.getOldLines(), otherZipIndexEntry);
						break;
					case CONTENT_MODIFIED:
						this.handler.contentModified(zipIndexEntry, entry.getOldLines(), otherZipIndexEntry, entry.getNewLines());
						break;
					default:
						throw new IllegalArgumentException("Unknown content diff type " + entry.getType() + ".");
				}
			}
			this.handler.endContentModified(zipIndexEntry, otherZipIndexEntry);
		}
		else
		{
			this.handler.modified(zipIndexEntry, otherZipIndexEntry);
		}
	}
}
