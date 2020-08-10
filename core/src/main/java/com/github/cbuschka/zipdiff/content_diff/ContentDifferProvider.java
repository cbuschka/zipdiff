package com.github.cbuschka.zipdiff.content_diff;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;

public class ContentDifferProvider
{
	private static Logger logger = LoggerFactory.getLogger(ContentDifferProvider.class);

	private static List<ContentDiffer> differs = loadContentDiffers();

	private static ContentDiffer defaultDiffer = new DefaultContentDiffer();

	private static List<ContentDiffer> loadContentDiffers()
	{
		List<ContentDiffer> differs = new ArrayList<>();

		ServiceLoader<ContentDiffer> loader = ServiceLoader.load(ContentDiffer.class);
		for (ContentDiffer curr : loader)
		{
			differs.add(curr);
		}

		differs.addAll(Arrays.asList(
				new ManifestContentDiffer(),
				new PropertiesContentDiffer(),
				new TextContentDiffer()
		));

		logger.debug("Current content differs: {}", differs);

		return differs;
	}

	public static ContentDiffer getContentDifferFor(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		for (ContentDiffer differ : differs)
		{
			if (differ.handles(zipIndexEntry, otherZipIndexEntry))
			{
				return differ;
			}
		}

		return defaultDiffer;
	}

	private ContentDifferProvider()
	{
	}
}
