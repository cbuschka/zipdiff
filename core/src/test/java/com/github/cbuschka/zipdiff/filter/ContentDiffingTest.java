package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.TestZipFile;
import com.github.cbuschka.zipdiff.TestZipFileBuilder;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntryType;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffer;
import com.github.cbuschka.zipdiff.index.ZipIndex;
import com.github.cbuschka.zipdiff.index.ZipIndexReader;
import com.github.cbuschka.zipdiff.process.ZipIndexDiffProcessor;
import com.github.cbuschka.zipdiff.report.ZipIndexDiffRecorder;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ContentDiffingTest
{
	@Rule
	public TestZipFile oneZipFile = TestZipFile.from(TestZipFileBuilder.newZipFile()
			.withEntry("test.properties", "key=overriddenvalue\nkey=value"));
	@Rule
	public TestZipFile otherZipFile = TestZipFile.from(TestZipFileBuilder.newZipFile()
			.withEntry("test.properties", "key=value"));

	private ZipIndexDiffRecorder recorder = new ZipIndexDiffRecorder();
	private ZipIndexDiffProcessor processor;

	@Test
	public void detectsUnchangedByContentDiff() throws IOException
	{
		whenDiffed();

		List<ZipIndexDiffRecorder.Entry> entries = this.recorder.getEntries();
		assertEquals(1, entries.size());
		assertEquals(ZipIndexDiffEntryType.UNCHANGED, entries.get(0).type);
	}

	private void whenDiffed() throws IOException
	{
		ZipIndexDiffer differ = new ZipIndexDiffer(true);
		ZipIndex oneZipIndex = ZipIndexReader.open(oneZipFile.getFile()).read();
		ZipIndex otherZipIndex = ZipIndexReader.open(otherZipFile.getFile()).read();
		ZipIndexDiff indexDiff = differ.diff(oneZipIndex, otherZipIndex);
		processor = new ZipIndexDiffProcessor(recorder);
		processor.process(indexDiff);
	}
}
