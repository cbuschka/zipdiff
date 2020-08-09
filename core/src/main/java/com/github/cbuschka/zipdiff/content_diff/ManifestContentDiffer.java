package com.github.cbuschka.zipdiff.content_diff;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ManifestContentDiffer extends AbstractMapContentDiffer
{
	private static final String[] SUFFIXES = {"/MANIFEST.MF"};

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
			Map<String, String> manifest = new Parser(new BufferedReader(new InputStreamReader(zipIndexEntry.getDataStream(), StandardCharsets.UTF_8))).read();
			Map<String, String> otherManifest = new Parser(new BufferedReader(new InputStreamReader(zipIndexEntry.getDataStream(), StandardCharsets.UTF_8))).read();

			return diff(zipIndexEntry, manifest, otherZipIndexEntry, otherManifest);
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	private static class Parser
	{
		private BufferedReader rd;

		public Parser(BufferedReader rd)
		{
			this.rd = rd;
		}

		public Map<String, String> read() throws IOException
		{
			Map<String, String> map = new LinkedHashMap<>();
			List<String> lines = readLines();
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

		private List<String> readLines() throws IOException
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
