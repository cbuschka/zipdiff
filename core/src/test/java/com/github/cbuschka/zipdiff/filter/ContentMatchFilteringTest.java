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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ContentMatchFilteringTest
{
	@Rule
	public TestZipFile oneZipFile = TestZipFile.from(TestZipFileBuilder.newZipFile()
			.withEntry("message.txt", "Hello world."));
	@Rule
	public TestZipFile otherZipFile = TestZipFile.from(TestZipFileBuilder.newZipFile()
			.withEntry("message.txt", "Hello world. NEW"));

	private Config config;
	private ZipIndexDiffRecorder recorder;
	private ZipIndexDiffProcessor processor;

	@Before
	public void before()
	{
		recorder = new ZipIndexDiffRecorder();
		config = new Config();
	}

	@Test
	public void detectsModifiedEntry() throws IOException
	{
		givenIsAProcessingPipeline();

		whenDiffed();

		List<ZipIndexDiffRecorder.Entry> entries = this.recorder.getEntries();
		assertEquals(1, entries.size());
		assertEquals(ZipIndexDiffEntryType.MODIFIED, entries.get(0).type);
	}

	@Test
	public void filtersUnchangedEntries() throws IOException
	{
		givenIsAContentFilteringConfig();
		givenIsAProcessingPipeline();

		whenDiffed();

		List<ZipIndexDiffRecorder.Entry> entries = this.recorder.getEntries();
		assertEquals(1, entries.size());
		assertEquals(ZipIndexDiffEntryType.UNCHANGED, entries.get(0).type);
	}

	private void givenIsAContentFilteringConfig()
	{
		config = new Config();
		com.github.cbuschka.zipdiff.filter.Rule rule = new com.github.cbuschka.zipdiff.filter.Rule();
		rule.setId("rule0");
		rule.getMatch().getOld().getPath().getIncludes().add("message.txt");
		rule.getMatch().getTypes().add(DiffType.CONTENT_MODIFIED);
		rule.getMatch().getOld().getContent().getIncludes().add("^Hello world.$");
		rule.getMatch().getNew().getContent().getIncludes().add("^Hello world. NEW$");
		rule.setAction(Action.IGNORE);
		config.getRules().add(rule);
	}

	private void whenDiffed() throws IOException
	{
		ZipIndexDiffer differ = new ZipIndexDiffer(true);
		ZipIndex oneZipIndex = ZipIndexReader.open(oneZipFile.getFile()).read();
		ZipIndex otherZipIndex = ZipIndexReader.open(otherZipFile.getFile()).read();
		ZipIndexDiff indexDiff = differ.diff(oneZipIndex, otherZipIndex);
		processor.process(indexDiff);
	}

	private void givenIsAProcessingPipeline()
	{
		RuleBasedZipIndexDiffFilter filter = new RuleBasedZipIndexDiffFilter(config, recorder);
		processor = new ZipIndexDiffProcessor(filter);
	}
}
