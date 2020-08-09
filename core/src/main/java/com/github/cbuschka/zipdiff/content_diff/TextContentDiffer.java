package com.github.cbuschka.zipdiff.content_diff;

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
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TextContentDiffer implements ContentDiffer
{
	private static final String[] SUFFIXES = {".txt", ".properties", ".xml", ".xsd", ".yml", ".yaml",
			".json", "*.java"};

	private ContentInfoUtil contentInfoUtil = new ContentInfoUtil();

	@Override
	public boolean handles(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		if (handlesSuffix(zipIndexEntry.getFullyQualifiedPath()) && handlesSuffix(otherZipIndexEntry.getFullyQualifiedPath()))
		{
			return true;
		}

		return handlesContent(zipIndexEntry) && handlesContent(otherZipIndexEntry);
	}

	private boolean handlesContent(ZipIndexEntry zipIndexEntry)
	{
		try
		{
			ContentInfo contentInfo = contentInfoUtil.findMatch(zipIndexEntry.getDataStream());
			return contentInfo != null
					&& contentInfo.getMimeType().startsWith("text/");
		}
		catch (IOException ex)
		{
			throw new UndeclaredThrowableException(ex);
		}
	}

	private boolean handlesSuffix(String path)
	{
		for (String suffix : SUFFIXES)
		{
			if (path.endsWith(suffix))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public ContentDiff diff(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		try
		{
			List<ContentDiffEntry> entries = new ArrayList<>();
			String aText = IOUtils.toString(zipIndexEntry.getDataStream(), StandardCharsets.UTF_8);
			String bText = IOUtils.toString(otherZipIndexEntry.getDataStream(), StandardCharsets.UTF_8);
			Patch<String> patch = DiffUtils.diff(aText, bText, (DiffAlgorithmListener) null);
			for (AbstractDelta<String> delta : patch.getDeltas())
			{
				switch (delta.getType())
				{
					case INSERT:
						entries.add(new ContentDiffEntry(ContentDiffType.CONTENT_ADDED, null, delta.getTarget().getLines()));
						break;
					case DELETE:
						entries.add(new ContentDiffEntry(ContentDiffType.CONTENT_DELETED, delta.getSource().getLines(), null));
						break;
					case EQUAL:
						entries.add(new ContentDiffEntry(ContentDiffType.CONTENT_UNCHANGED, delta.getSource().getLines(), delta.getTarget().getLines()));
						break;
					case CHANGE:
						entries.add(new ContentDiffEntry(ContentDiffType.CONTENT_MODIFIED, delta.getSource().getLines(), delta.getTarget().getLines()));
						break;
					default:
						throw new IllegalStateException();
				}
			}

			return new ContentDiff(zipIndexEntry, otherZipIndexEntry, entries);
		}
		catch (DiffException | IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}
}
