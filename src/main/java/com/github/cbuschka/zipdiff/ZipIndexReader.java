package com.github.cbuschka.zipdiff;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipIndexReader implements Closeable
{
	private ChecksumCalculator checksumCalculator = new ChecksumCalculator();
	private final ZipInputStream zipIn;
	private final String path;
	private final String entryPathPrefix;
	private final MessageDigest zipMessageDigest;

	public static ZipIndexReader open(File f)
	{
		try
		{
			String path = f.toURI().toURL().toExternalForm();
			FileInputStream zipDataIn = new FileInputStream(f);
			return new ZipIndexReader(path, f.getAbsolutePath(), zipDataIn);
		}
		catch (IOException ex)
		{
			throw new UndeclaredThrowableException(ex);
		}
	}

	public ZipIndexReader(String path, String entryPathPrefix, InputStream zipDataIn)
	{
		this.path = path;
		this.entryPathPrefix = entryPathPrefix;
		this.zipMessageDigest = this.checksumCalculator.newChecksumMessageDigest();
		this.zipIn = new ZipInputStream(new DigestInputStream(zipDataIn, zipMessageDigest));
	}

	public ZipIndex read() throws IOException
	{
		Map<String, ZipIndexEntry> entryMap = new LinkedHashMap<>();

		ZipIndexEntry indexEntry;
		while ((indexEntry = readIndexEntryOrNull()) != null)
		{
			entryMap.put(indexEntry.getPath(), indexEntry);
		}

		ZipIndex index = new ZipIndex(path, new BigInteger(this.zipMessageDigest.digest()), entryMap);
		return index;
	}

	private ZipIndexEntry readIndexEntryOrNull() throws IOException
	{
		ZipEntry nextEntry = zipIn.getNextEntry();
		if (nextEntry == null)
		{
			return null;
		}

		byte[] data = IOUtils.toByteArray(new UnclosableInputStream(zipIn));

		String entryPath = nextEntry.getName();
		long entryCompressedSize = nextEntry.getCompressedSize();
		long entryCrc = nextEntry.getCrc();
		long entrySize = nextEntry.getSize();

		ZipIndex subIndex = null;
		BigInteger checksum;
		if (isZip(entryPath))
		{
			subIndex = readZipIndex(entryPath, new ByteArrayInputStream(data));
			checksum = subIndex.getChecksum();
		}
		else
		{
			checksum = this.checksumCalculator.calcChecksum(new ByteArrayInputStream(data));
		}

		return new ZipIndexEntry(this.entryPathPrefix, entryPath, checksum, entrySize, entryCompressedSize, entryCrc, data, subIndex);
	}

	private ZipIndex readZipIndex(String entryPath, InputStream in) throws IOException
	{
		ZipIndexReader rd = new ZipIndexReader(this.path + "!" + entryPath, this.entryPathPrefix + entryPath + "!", in);
		ZipIndex zipIndex = rd.read();
		rd.close();
		return zipIndex;
	}

	private boolean isZip(String entryPath)
	{
		return entryPath.toLowerCase().endsWith(".zip")
				|| entryPath.toLowerCase().endsWith(".jar");
	}

	public void close() throws IOException
	{
		this.zipIn.close();
	}
}
