package com.github.cbuschka.zipdiff.content_diff;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
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
	public ContentDiff diff(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry, Charset encoding)
	{
		try
		{
			Map<String, String> properties = load(zipIndexEntry.getDataStream(), encoding.name());
			Map<String, String> otherProperties = load(zipIndexEntry.getDataStream(), encoding.name());

			return diff(zipIndexEntry, properties, otherZipIndexEntry, otherProperties);
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	private Map<String, String> load(InputStream in, String encoding) throws IOException
	{
		Properties props = new Properties();
		props.load(new InputStreamReader(in, Charset.forName(encoding)));
		Map<String, String> map = new LinkedHashMap<>();
		for (Map.Entry<Object, Object> entry : props.entrySet())
		{
			map.put((String) entry.getKey(), (String) entry.getValue());
		}
		return map;
	}
}
