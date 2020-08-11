package com.github.cbuschka.zipdiff.content_diff;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.util.Arrays;
import java.util.List;

public class DefaultContentHandler implements ContentHandler
{
	@Override
	public boolean handles(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		return true;
	}

	@Override
	public ContentDiff diff(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		List<ContentDiffEntry> entries = Arrays.asList(new ContentDiffEntry(ContentDiffType.CONTENT_MODIFIED, Arrays.asList("Binary data"), Arrays.asList("Binary data")));
		return new ContentDiff(zipIndexEntry, otherZipIndexEntry, entries);
	}
}
