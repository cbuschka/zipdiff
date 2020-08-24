package com.github.cbuschka.zipdiff.content_diff;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ManifestContentHandler extends AbstractMapContentDiffer
{
	private static final String[] SUFFIXES = {"/MANIFEST.MF"};

	Parser parser = new Parser();

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
	public ContentDiff diff(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry, Charset unusedEncoding)
	{
		try
		{
			Map<String, String> manifest = this.parser.read(new BufferedReader(new InputStreamReader(zipIndexEntry.getDataStream(), StandardCharsets.UTF_8)));
			Map<String, String> otherManifest = this.parser.read(new BufferedReader(new InputStreamReader(zipIndexEntry.getDataStream(), StandardCharsets.UTF_8)));

			return diff(zipIndexEntry, manifest, otherZipIndexEntry, otherManifest);
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	static class Parser
	{
		public Map<String, String> read(Reader in) throws IOException
		{
			BufferedReader rd = new BufferedReader(in);

			Map<String, String> map = new LinkedHashMap<>();
			List<String> lines = readLines(rd);
			String key = null;
			for (int i = 0; i < lines.size(); ++i)
			{
				String line = lines.get(i);
				if (line.startsWith(" "))
				{
					map.put(key, map.get(key) + line.substring(1));
				}
				else
				{
					int colonPos = line.indexOf(':');
					if (colonPos != -1)
					{
						key = line.substring(0, colonPos).trim();
						String value = line.substring(colonPos + 1).trim();
						map.put(key, value);
					}
				}
			}

			return map;
		}

		private List<String> readLines(BufferedReader rd) throws IOException
		{
			List<String> lines = new ArrayList<>();
			String line;
			while ((line = rd.readLine()) != null)
			{
				lines.add(line);
			}

			return lines;
		}
	}

}
