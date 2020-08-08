package com.github.cbuschka.zipdiff.process;

import com.github.cbuschka.zipdiff.diff.ZipIndexDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntry;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntryType;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffHandler;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;
import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffAlgorithmListener;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ZipIndexDiffProcessor
{
	private ContentInfoUtil contentInfoUtil = new ContentInfoUtil();
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
					handleModified(entryType, entry.getZipIndexEntry(), entry.getOtherZipIndexEntry());
					break;
				default:
					throw new IllegalStateException("Unexpected entry type: " + entryType);
			}
		}
		this.handler.finished();
	}

	private void handleModified(ZipIndexDiffEntryType entryType, ZipIndexEntry entry, ZipIndexEntry other) throws IOException
	{
		if (this.showDiffs && canDiff(entry, other))
		{
			this.handler.startContentModified(entry, other);
			try
			{
				String aText = IOUtils.toString(entry.getDataStream(), StandardCharsets.UTF_8);
				String bText = IOUtils.toString(other.getDataStream(), StandardCharsets.UTF_8);
				Patch<String> patch = DiffUtils.diff(aText, bText, (DiffAlgorithmListener) null);
				for (AbstractDelta<String> delta : patch.getDeltas())
				{
					switch (delta.getType())
					{
						case INSERT:
							this.handler.modifiedContentInserted(delta.getTarget().getLines());
							break;
						case DELETE:
							this.handler.modifiedContentDeleted(delta.getSource().getLines());
							break;
						case EQUAL:
							this.handler.modifiedContentEqual(delta.getSource().getLines());
							break;
						case CHANGE:
							this.handler.modifiedContentChanged(delta.getSource().getLines(), delta.getTarget().getLines());
							break;
						default:
							throw new IllegalStateException();
					}
				}
				this.handler.endContentModified();
			}
			catch (DiffException ex)
			{
				throw new RuntimeException(ex);
			}
		}
		else
		{
			this.handler.modified(entry, other);
		}
	}

	private static final String[] SUFFIXES = {"/MANIFEST.MF", ".txt", ".properties", ".xml", ".xsd", ".yml", ".yaml",
			".json", "*.java"};

	private boolean canDiff(ZipIndexEntry entry, ZipIndexEntry other) throws IOException
	{
		for (String suffix : SUFFIXES)
		{
			if (entry.getPath().endsWith(suffix))
			{
				return true;
			}
		}

		ContentInfo aContentInfo = contentInfoUtil.findMatch(entry.getDataStream());
		ContentInfo bContentInfo = contentInfoUtil.findMatch(other.getDataStream());
		return aContentInfo != null && bContentInfo != null
				&& aContentInfo.getMimeType().equals(bContentInfo.getMimeType())
				&& aContentInfo.getMimeType().startsWith("text/");
	}
}
