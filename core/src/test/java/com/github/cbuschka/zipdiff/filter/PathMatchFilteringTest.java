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
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PathMatchFilteringTest
{
	@Rule
	public TestZipFile oneZipFile = TestZipFile.from(TestZipFileBuilder.newZipFile()
			.withEntry("message.txt", "Hello world."));
	@Rule
	public TestZipFile otherZipFile = TestZipFile.from(TestZipFileBuilder.newZipFile()
			.withEntry("message.txt", "Hello world."));

	private Config config = new Config();
	private ZipIndexDiffRecorder recorder = new ZipIndexDiffRecorder();
	private ZipIndexDiffProcessor processor;

	@Test
	public void detectsUnchangedEntries() throws IOException
	{
		givenIsAProcessingPipeline();

		whenDiffed();

		List<ZipIndexDiffRecorder.Entry> entries = this.recorder.getEntries();
		assertEquals(1, entries.size());
		assertEquals(ZipIndexDiffEntryType.UNCHANGED, entries.get(0).type);
	}

	@Test
	public void filtersUnchangedEntries() throws IOException
	{
		givenIsAUnchangedFilteringConfig();
		givenIsAProcessingPipeline();

		whenDiffed();

		List<ZipIndexDiffRecorder.Entry> entries = this.recorder.getEntries();
		assertEquals(0, entries.size());
	}

	private void givenIsAUnchangedFilteringConfig()
	{
		config = new Config();
		com.github.cbuschka.zipdiff.filter.Rule rule = new com.github.cbuschka.zipdiff.filter.Rule();
		rule.setId("rule0");
		rule.getMatch().getOld().getPath().getIncludes().add("message.txt");
		rule.getMatch().getTypes().add(DiffType.UNCHANGED);
		rule.setAction(Action.IGNORE);
		config.getRules().add(rule);
	}

	private void whenDiffed() throws IOException
	{
		ZipIndexDiffer differ = new ZipIndexDiffer(StandardCharsets.UTF_8, true);
		ZipIndex oneZipIndex = ZipIndexReader.open(oneZipFile.getFile()).read();
		ZipIndex otherZipIndex = ZipIndexReader.open(otherZipFile.getFile()).read();
		ZipIndexDiff indexDiff = differ.diff(oneZipIndex, otherZipIndex);
		processor.process(indexDiff);
	}

	private void givenIsAProcessingPipeline()
	{
		RuleBasedZipIndexDiffFilter filter = new RuleBasedZipIndexDiffFilter(config, recorder);
		processor = new ZipIndexDiffProcessor(filter, StandardCharsets.UTF_8);
	}
}
