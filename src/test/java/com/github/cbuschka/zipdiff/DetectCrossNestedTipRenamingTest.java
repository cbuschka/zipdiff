package com.github.cbuschka.zipdiff;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DetectCrossNestedTipRenamingTest
{
	@Rule
	public TestZipFile zipA = TestZipFile.from(TestZipFileBuilder.newZipFile()
			.withEntry("b.txt", "hello world!"));
	@Rule
	public TestZipFile zipB = TestZipFile.from(TestZipFileBuilder.newZipFile()
			.withEntry("a.zip", TestZipFileBuilder.newZipFile().withEntry("b2.txt", "hello world!")));

	private ZipDiffer differ = new ZipDiffer(true);

	@Test
	public void doesNotDetectCrossZipRenamings() throws IOException
	{
		ZipIndex indexA = ZipIndexReader.open(zipA.getFile()).read();
		ZipIndex indexB = ZipIndexReader.open(zipB.getFile()).read();

		ZipDiff diff = differ.diff(indexA, indexB);

		assertThat(diff.getEntries().size(), is(2));
		assertThat(diff.getEntries().get(0).getType(), is(ZipDiffEntryType.ADDED));
		assertThat(diff.getEntries().get(1).getType(), is(ZipDiffEntryType.DELETED));

	}
}
