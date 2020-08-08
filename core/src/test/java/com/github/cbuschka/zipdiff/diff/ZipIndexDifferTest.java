package com.github.cbuschka.zipdiff.diff;

import com.github.cbuschka.zipdiff.diff.ZipIndexDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntry;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntryType;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffer;
import com.github.cbuschka.zipdiff.index.ChecksumCalculator;
import com.github.cbuschka.zipdiff.index.ZipIndex;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ZipIndexDifferTest
{
	private ChecksumCalculator checksumCalculator = new ChecksumCalculator();

	private ZipIndex zipIndexA;
	private ZipIndex zipIndexB;

	private ZipIndexDiffer differ = new ZipIndexDiffer(true);
	private ZipIndexDiff zipIndexDiff;

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
		ZipIndexDiffEntry zipIndexDiffEntry = this.zipIndexDiff.getEntryByPath(path);
		assertThat(zipIndexDiffEntry.getType(), is(ZipIndexDiffEntryType.UNCHANGED));
	}

	private void thenFileIsRenamed(String path, String newPath)
	{
		ZipIndexDiffEntry zipIndexDiffEntry = this.zipIndexDiff.getEntryByPath(path);
		assertThat(zipIndexDiffEntry.getType(), is(ZipIndexDiffEntryType.RENAMED));
		assertThat(zipIndexDiffEntry.getOtherZipIndexEntry().getPath(), is(newPath));
	}

	private void thenFileIsRemoved(String path)
	{
		ZipIndexDiffEntry zipIndexDiffEntry = this.zipIndexDiff.getEntryByPath(path);
		assertThat(zipIndexDiffEntry.getType(), is(ZipIndexDiffEntryType.DELETED));
	}

	private void thenFileIsAdded(String path)
	{
		ZipIndexDiffEntry zipIndexDiffEntry = this.zipIndexDiff.getEntryByOtherPath(path);
		assertThat(zipIndexDiffEntry.getType(), is(ZipIndexDiffEntryType.ADDED));
	}

	private void thenFileIsChanged(String path)
	{
		ZipIndexDiffEntry zipIndexDiffEntry = this.zipIndexDiff.getEntryByOtherPath(path);
		assertThat(zipIndexDiffEntry.getType(), is(ZipIndexDiffEntryType.MODIFIED));
	}

	private void thenDiffEntryCountIs(int n)
	{
		assertThat(this.zipIndexDiff.size(), is(n));
	}

	private void whenDiffed()
	{
		zipIndexDiff = this.differ.diff(this.zipIndexA, this.zipIndexB);
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
			entries.put(path, new ZipIndexEntry("location", "", path, checksumCalculator.calcChecksum(data), data.length, data.length, checksumCalculator.calcCrc(data), data, null));
		}
		return new ZipIndex(zipPath, null, entries);
	}

	private void givenZipIndexB(String... fileWithContentPairs) throws IOException
	{
		ZipIndex zipIndex = buildZipIndex("b", fileWithContentPairs);
		this.zipIndexB = zipIndex;
	}
}
