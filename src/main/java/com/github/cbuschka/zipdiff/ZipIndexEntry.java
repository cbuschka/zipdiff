package com.github.cbuschka.zipdiff;

import java.math.BigInteger;

public class ZipIndexEntry
{
	private final long crc;
	private final String path;
	private BigInteger checksum;
	private final long size;
	private final long compressedSize;
	private final ZipIndex zipIndex;

	public ZipIndexEntry(String entryPath, BigInteger checksum, long entrySize, long compressedSize, long entryCrc, ZipIndex zipIndex)
	{
		this.path = entryPath;
		this.zipIndex = zipIndex;
		this.crc = entryCrc;
		this.compressedSize = compressedSize;
		this.size = entrySize;
		this.checksum = checksum;
	}

	public BigInteger getChecksum()
	{
		return checksum;
	}

	public String getPath()
	{
		return path;
	}

	public ZipIndex getZipIndex()
	{
		 return this.zipIndex;
	}

	public long getSize()
	{
		return size;
	}

	public long getCrc()
	{
		return crc;
	}

	public long getCompressedSize()
	{
		return compressedSize;
	}
}
