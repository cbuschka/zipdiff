package com.github.cbuschka.zipdiff;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ZipIndex
{
	private String path;
	private final Map<String, ZipIndexEntry> entries;
	private BigInteger checksum;

	public ZipIndex(String path, BigInteger checksum, Map<String, ZipIndexEntry> entries)
	{
		this.path = path;
		this.checksum = checksum;
		this.entries = entries;
	}

	public boolean hasEntryWithPath(String path)
	{
		return this.entries.containsKey(path);
	}

	public BigInteger getChecksum()
	{
		return checksum;
	}

	public Iterable<ZipIndexEntry> entries()
	{
		return this.entries.values();
	}

	public String getPath()
	{
		return path;
	}

	public ZipIndexEntry getEntryByPath(String path)
	{
		return this.entries.get(path);
	}

	public List<ZipIndexEntry> getEntriesByChecksum(BigInteger checksum)
	{
		List<ZipIndexEntry> entries = new ArrayList<>();
		for (Map.Entry<String, ZipIndexEntry> mapEntry : this.entries.entrySet())
		{
			if (mapEntry.getValue().getChecksum().equals(checksum))
			{
				entries.add(mapEntry.getValue());
			}
		}

		return entries;
	}
}
