package com.github.cbuschka.zipdiff.content_diff;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class ContentDifferProvider
{
	private static List<ContentDiffer> differs = Arrays.asList(
			new ManifestContentDiffer(),
			new PropertiesContentDiffer(),
			new TextContentDiffer()
	);

	public static Optional<ContentDiffer> get(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		for (ContentDiffer differ : differs)
		{
			if (differ.handles(zipIndexEntry, otherZipIndexEntry))
			{
				return Optional.of(differ);
			}
		}

		return Optional.empty();
	}
}
