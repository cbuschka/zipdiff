package com.github.cbuschka.zipdiff.content_diff;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;

public class ContentDiffer
{
	private static Logger logger = LoggerFactory.getLogger(ContentDiffer.class);

	private static List<ContentHandler> differs = loadContentDiffers();

	private static ContentHandler defaultDiffer = new DefaultContentHandler();

	private static List<ContentHandler> loadContentDiffers()
	{
		List<ContentHandler> differs = new ArrayList<>();

		ServiceLoader<ContentHandler> loader = ServiceLoader.load(ContentHandler.class);
		for (ContentHandler curr : loader)
		{
			differs.add(curr);
		}

		differs.addAll(Arrays.asList(
				new ManifestContentHandler(),
				new PropertiesContentHandler(),
				new TextContentHandler()
		));

		logger.debug("Current content differs: {}", differs);

		return differs;
	}

	public static ContentDiff diff(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry, Charset encoding) {
		return getContentDifferFor(zipIndexEntry, otherZipIndexEntry).diff(zipIndexEntry, otherZipIndexEntry, encoding);
	}

	private static ContentHandler getContentDifferFor(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		for (ContentHandler differ : differs)
		{
			if (differ.handles(zipIndexEntry, otherZipIndexEntry))
			{
				return differ;
			}
		}

		return defaultDiffer;
	}

	private ContentDiffer()
	{
	}
}
