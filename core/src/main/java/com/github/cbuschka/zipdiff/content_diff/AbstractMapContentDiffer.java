package com.github.cbuschka.zipdiff.content_diff;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class AbstractMapContentDiffer implements ContentDiffer
{
	protected ContentDiff diff(ZipIndexEntry zipIndexEntry, Map<String, String> map, ZipIndexEntry otherZipIndexEntry, Map<String, String> otherMap)
	{
		List<ContentDiffEntry> entries = new ArrayList<>();
		for (Map.Entry<String, String> entry : map.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			String otherValue = otherMap.get(key);
			if (otherValue == null)
			{
				entries.add(new ContentDiffEntry(ContentDiffType.CONTENT_DELETED, Arrays.asList(key + ": " + value), null));
			}
			else if (value.equals(otherValue))
			{
				entries.add(new ContentDiffEntry(ContentDiffType.CONTENT_UNCHANGED, Arrays.asList(key + ": " + value), null));
			}
			else
			{
				entries.add(new ContentDiffEntry(ContentDiffType.CONTENT_MODIFIED, Arrays.asList(key + ": " + value), Arrays.asList(key + ": " + otherValue)));
			}
		}

		for (Map.Entry<String, String> otherEntry : otherMap.entrySet())
		{
			String key = otherEntry.getKey();
			String otherValue = otherEntry.getValue();
			if (!map.containsKey(key))
			{
				entries.add(new ContentDiffEntry(ContentDiffType.CONTENT_ADDED, null, Arrays.asList(key + ": " + otherValue)));
			}
		}

		return new ContentDiff(zipIndexEntry, otherZipIndexEntry, entries);
	}
}
