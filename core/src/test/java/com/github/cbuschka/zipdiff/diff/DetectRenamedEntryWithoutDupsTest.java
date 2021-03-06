package com.github.cbuschka.zipdiff.diff;

import com.github.cbuschka.zipdiff.TestZipFile;
import com.github.cbuschka.zipdiff.TestZipFileBuilder;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntryType;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffer;
import com.github.cbuschka.zipdiff.index.ZipIndex;
import com.github.cbuschka.zipdiff.index.ZipIndexReader;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DetectRenamedEntryWithoutDupsTest
{
	@Rule
	public TestZipFile zipA = TestZipFile.from(TestZipFileBuilder
			.newZipFile().withEntry("a.txt", "hello world!"));
	@Rule
	public TestZipFile zipB = TestZipFile.from(TestZipFileBuilder
			.newZipFile().withEntry("b.txt", "hello world!"));

	private ZipIndexDiffer differ = new ZipIndexDiffer(StandardCharsets.UTF_8, true);

	@Test
	public void detectsRenameWithoutDups() throws IOException
	{
		ZipIndex indexA = ZipIndexReader.open(zipA.getFile()).read();
		ZipIndex indexB = ZipIndexReader.open(zipB.getFile()).read();

		ZipIndexDiff diff = differ.diff(indexA, indexB);

		assertThat(diff.getEntries().size(), is(1));
		assertThat(diff.getEntries().get(0).getType(), is(ZipIndexDiffEntryType.RENAMED));

	}
}
