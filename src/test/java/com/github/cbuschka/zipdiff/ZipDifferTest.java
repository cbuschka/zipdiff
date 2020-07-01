package com.github.cbuschka.zipdiff;

import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ZipDifferTest
{
	private ChecksumCalculator checksumCalculator = new ChecksumCalculator();

	private ZipIndex zipIndexA;
	private ZipIndex zipIndexB;

	private ZipDiffer differ = new ZipDiffer(true);
	private ZipDiff zipDiff;

	@Test
	public void detectAdded() throws Exception
	{
		givenZipIndexA("file.kept", "file.kept.content");
		givenZipIndexB("file.kept", "file.kept.content", "file.added", "file.added.content");

		whenDiffed();

		thenDiffEntryCountIs(2);
		thenFileIsKept("file.kept");
		thenFileIsAdded("file.added");
	}

	@Test
	public void detectRemoved() throws Exception
	{
		givenZipIndexA("file.kept", "file.kept.content", "file.removed", "file.removed.content");
		givenZipIndexB("file.kept", "file.kept.content");

		whenDiffed();

		thenDiffEntryCountIs(2);
		thenFileIsKept("file.kept");
		thenFileIsRemoved("file.removed");
	}

	@Test
	public void detectChanged() throws Exception
	{
		givenZipIndexA("file.kept", "file.kept.content", "file.changed", "file.changed.content.unchanged");
		givenZipIndexB("file.kept", "file.kept.content", "file.changed", "file.changed.content.changed");

		whenDiffed();

		thenDiffEntryCountIs(2);
		thenFileIsKept("file.kept");
		thenFileIsChanged("file.changed");
	}

	@Test
	public void detectRenamed() throws Exception
	{
		givenZipIndexA("file.kept", "file.kept.content", "file.renamed.orig", "file.renamed.content");
		givenZipIndexB("file.kept", "file.kept.content", "file.renamed.new", "file.renamed.content");

		whenDiffed();

		thenDiffEntryCountIs(2);
		thenFileIsKept("file.kept");
		thenFileIsRenamed("file.renamed.orig", "file.renamed.new");
	}

	private void thenFileIsKept(String path)
	{
		ZipDiffEntry zipDiffEntry = this.zipDiff.getEntryByPath(path);
		assertThat(zipDiffEntry.getType(), is(ZipDiffEntryType.UNCHANGED));
	}

	private void thenFileIsRenamed(String path, String newPath)
	{
		ZipDiffEntry zipDiffEntry = this.zipDiff.getEntryByPath(path);
		assertThat(zipDiffEntry.getType(), is(ZipDiffEntryType.RENAMED));
		assertThat(zipDiffEntry.getOtherZipIndexEntry().getPath(), is(newPath));
	}

	private void thenFileIsRemoved(String path)
	{
		ZipDiffEntry zipDiffEntry = this.zipDiff.getEntryByPath(path);
		assertThat(zipDiffEntry.getType(), is(ZipDiffEntryType.DELETED));
	}

	private void thenFileIsAdded(String path)
	{
		ZipDiffEntry zipDiffEntry = this.zipDiff.getEntryByOtherPath(path);
		assertThat(zipDiffEntry.getType(), is(ZipDiffEntryType.ADDED));
	}

	private void thenFileIsChanged(String path)
	{
		ZipDiffEntry zipDiffEntry = this.zipDiff.getEntryByOtherPath(path);
		assertThat(zipDiffEntry.getType(), is(ZipDiffEntryType.MODIFIED));
	}

	private void thenDiffEntryCountIs(int n)
	{
		assertThat(this.zipDiff.size(), is(n));
	}

	private void whenDiffed()
	{
		zipDiff = this.differ.diff(this.zipIndexA, this.zipIndexB);
	}

	private void givenZipIndexA(String... fileWithContentPairs) throws IOException
	{
		ZipIndex zipIndex = buildZipIndex("a", fileWithContentPairs);
		this.zipIndexA = zipIndex;
	}

	private ZipIndex buildZipIndex(String zipPath, String[] fileWithContentPairs) throws UnsupportedEncodingException
	{
		Map<String, ZipIndexEntry> entries = new HashMap<>();
		for (int i = 0; i < fileWithContentPairs.length / 2; ++i)
		{
			String path = fileWithContentPairs[i * 2 + 0];
			byte[] data = fileWithContentPairs[i * 2 + 1].getBytes(StandardCharsets.UTF_8);
			entries.put(path, new ZipIndexEntry("", path, checksumCalculator.calcChecksum(data), data.length, data.length, checksumCalculator.calcCrc(data), data, null));
		}
		return new ZipIndex(zipPath, null, entries);
	}

	private void givenZipIndexB(String... fileWithContentPairs) throws IOException
	{
		ZipIndex zipIndex = buildZipIndex("b", fileWithContentPairs);
		this.zipIndexB = zipIndex;
	}
}
