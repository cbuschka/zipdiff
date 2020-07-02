package com.github.cbuschka.zipdiff;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DetectRenamedEntryWithCopyReverseTest
{
	@Rule
	public TestZipFile zipA = TestZipFile.from(TestZipFileBuilder.newZipFile()
			.withEntry("b.txt", "hello world!"));
	@Rule
	public TestZipFile zipB = TestZipFile.from(TestZipFileBuilder.newZipFile()
			.withEntry("a.txt", "hello world!")
			.withEntry("b.txt", "hello world!"));

	private ZipIndexDiffer differ = new ZipIndexDiffer(true);

	@Test
	public void testIt() throws IOException
	{
		ZipIndex indexA = ZipIndexReader.open(zipA.getFile()).read();
		ZipIndex indexB = ZipIndexReader.open(zipB.getFile()).read();

		ZipIndexDiff diff = differ.diff(indexA, indexB);

		assertThat(diff.getEntries().size(), is(2));
		assertThat(diff.getEntries().get(0).getType(), is(ZipIndexDiffEntryType.RENAMED));
		assertThat(diff.getEntries().get(1).getType(), is(ZipIndexDiffEntryType.ADDED));

	}
}
