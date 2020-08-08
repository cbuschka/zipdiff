package com.github.cbuschka.zipdiff.index;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumCalculator
{
	public BigInteger calcChecksum(InputStream in) throws IOException
	{
		MessageDigest messageDigest = newChecksumMessageDigest();

		byte[] bbuf = new byte[1024 * 4];
		int len;
		while ((len = in.read(bbuf, 0, bbuf.length)) > -1)
		{
			messageDigest.update(bbuf, 0, len);
		}
		return new BigInteger(messageDigest.digest());
	}

	public BigInteger calcChecksum(byte[] data)
	{
		try
		{
			return calcChecksum(new ByteArrayInputStream(data));
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	public MessageDigest newChecksumMessageDigest()
	{
		MessageDigest digest;
		try
		{
			digest = MessageDigest.getInstance("SHA-256");
		}
		catch (NoSuchAlgorithmException ex)
		{
			throw new RuntimeException(ex);
		}
		return digest;
	}

	public long calcCrc(byte[] data)
	{
		try
		{
			return calcCrc(new ByteArrayInputStream(data));
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	private long calcCrc(InputStream in) throws IOException
	{
		Checksum checksum = new CRC32();
		byte[] bbuf = new byte[1024 * 4];
		int len;
		while ((len = in.read(bbuf, 0, bbuf.length)) > -1)
		{
			checksum.update(bbuf, 0, len);
		}
		return checksum.getValue();
	}


}
