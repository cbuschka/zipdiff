package com.github.cbuschka.zipdiff;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Objects;

public class ZipIndexEntry
{
	private final long crc;
	private final String location;
	private final String pathPrefix;
	private final String path;
	private BigInteger checksum;
	private final long size;
	private final long compressedSize;
	private final ZipIndex zipIndex;
	private final byte[] data;

	public ZipIndexEntry(String location, String parentPath, String path, BigInteger checksum, long entrySize, long compressedSize, long entryCrc, byte[] data, ZipIndex zipIndex)
	{
		this.location = location;
		this.pathPrefix = parentPath;
		this.path = path;
		this.zipIndex = zipIndex;
		this.crc = entryCrc;
		this.compressedSize = compressedSize;
		this.size = entrySize;
		this.checksum = checksum;
		this.data = data;
	}

	public InputStream getDataStream()
	{
		return new ByteArrayInputStream(this.data);
	}

	public BigInteger getChecksum()
	{
		return checksum;
	}

	public String getFullyQualifiedPath()
	{
		return this.pathPrefix + path;
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

	public boolean isFolder()
	{
		return this.path.endsWith("/");
	}

	public boolean isFile()
	{
		return !isFolder();
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ZipIndexEntry that = (ZipIndexEntry) o;
		return this.path.equals(that.path)
				&& this.location.equals(that.location);
	}

	public String getLocation()
	{
		return location;
	}

	public String getPathPrefix()
	{
		return pathPrefix;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(pathPrefix, path);
	}

	@Override
	public String toString()
	{
		return "ZipIndexEntry{" +
				"location=" + this.location +
				",path=" + path + "}";
	}

	public long getCompressedSize()
	{
		return compressedSize;
	}
}
