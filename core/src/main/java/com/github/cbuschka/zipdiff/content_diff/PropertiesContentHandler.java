package com.github.cbuschka.zipdiff.content_diff;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesContentHandler extends AbstractMapContentDiffer
{
	private static final String[] SUFFIXES = {".properties"};

	@Override
	public boolean handles(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		return handlesSuffix(zipIndexEntry.getFullyQualifiedPath()) && handlesSuffix(otherZipIndexEntry.getFullyQualifiedPath());
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
			Map<String, String> properties = load(zipIndexEntry.getDataStream());
			Map<String, String> otherProperties = load(zipIndexEntry.getDataStream());

			return diff(zipIndexEntry, properties, otherZipIndexEntry, otherProperties);
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	private Map<String, String> load(InputStream in) throws IOException
	{
		Properties props = new Properties();
		props.load(in);
		Map<String, String> map = new LinkedHashMap<>();
		for (Map.Entry<Object, Object> entry : props.entrySet())
		{
			map.put((String) entry.getKey(), (String) entry.getValue());
		}
		return map;
	}
}
