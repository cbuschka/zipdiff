package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.content_diff.ContentDiff;
import com.github.cbuschka.zipdiff.content_diff.ContentDiffEntry;
import com.github.cbuschka.zipdiff.content_diff.ContentDiffType;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntryType;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffHandler;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class RuleBasedZipIndexDiffFilter extends AbstractZipIndexDiffFilter
{
	private static Logger logger = LoggerFactory.getLogger(RuleBasedZipIndexDiffFilter.class);

	private Config config;

	public RuleBasedZipIndexDiffFilter(Config config, ZipIndexDiffHandler handler)
	{
		super(handler);
		this.config = config;
	}

	@Override
	public void begin(ZipIndexDiff zipIndexDiff)
	{
		logger.debug("Rule(s): {}", this.config.getRules().stream().map(Rule::getId).collect(toList()));
		logger.debug("Default action: {}", this.config.getDefaultAction());

		handler.begin(zipIndexDiff);
	}

	@Override
	public void added(ZipIndexEntry otherZipIndexEntry)
	{
		if (shouldBeProcessed(ZipIndexDiffEntryType.ADDED, null, otherZipIndexEntry))
		{
			super.added(otherZipIndexEntry);
		}
	}

	@Override
	public void deleted(ZipIndexEntry zipIndexEntry)
	{
		if (shouldBeProcessed(ZipIndexDiffEntryType.DELETED, zipIndexEntry, null))
		{
			super.deleted(zipIndexEntry);
		}
	}

	@Override
	public void unchanged(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		if (shouldBeProcessed(ZipIndexDiffEntryType.UNCHANGED, zipIndexEntry, otherZipIndexEntry))
		{
			super.unchanged(zipIndexEntry, otherZipIndexEntry);
		}
	}

	@Override
	public void renamed(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		if (shouldBeProcessed(ZipIndexDiffEntryType.RENAMED, zipIndexEntry, otherZipIndexEntry))
		{
			super.renamed(zipIndexEntry, otherZipIndexEntry);
		}
	}

	@Override
	public void modified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry, ContentDiff contentDiff)
	{
		if (shouldBeProcessed(ZipIndexDiffEntryType.MODIFIED, zipIndexEntry, otherZipIndexEntry))
		{
			contentDiff = filterContent(zipIndexEntry, otherZipIndexEntry, contentDiff);
			if (contentDiff.hasChanges())
			{
				super.modified(zipIndexEntry, otherZipIndexEntry, contentDiff);
			}
			else
			{
				super.unchanged(zipIndexEntry, otherZipIndexEntry);
			}
		}
	}

	private ContentDiff filterContent(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry, ContentDiff contentDiff)
	{
		List<ContentDiffEntry> newEntries = new ArrayList<>();
		for (ContentDiffEntry entry : contentDiff.getEntries())
		{
			if (shouldBeProcessed(entry.getType(), zipIndexEntry, entry.getOldLines(), otherZipIndexEntry, entry.getNewLines()))
			{
				newEntries.add(entry);
			}
		}

		return new ContentDiff(zipIndexEntry, otherZipIndexEntry, newEntries);
	}

	@Override
	public void finished()
	{
		super.finished();
	}

	private boolean shouldBeProcessed(ZipIndexDiffEntryType type, ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		return shouldBeProcessed(DiffType.instanceFor(type), zipIndexEntry, null, otherZipIndexEntry, null);
	}

	private boolean shouldBeProcessed(ContentDiffType type, ZipIndexEntry zipIndexEntry, List<String> oldLines, ZipIndexEntry otherZipIndexEntry, List<String> newLines)
	{
		return shouldBeProcessed(DiffType.instanceFor(type), zipIndexEntry, oldLines, otherZipIndexEntry, newLines);
	}

	private boolean shouldBeProcessed(DiffType type, ZipIndexEntry zipIndexEntry, List<String> oldLines, ZipIndexEntry otherZipIndexEntry, List<String> newLines)
	{
		Optional<Rule> optRule = this.config.getFirstMatchingRule(type, zipIndexEntry, oldLines, otherZipIndexEntry, newLines);
		boolean shouldBeProcessed = optRule.map(Rule::getAction).orElse(this.config.getDefaultAction()) == Action.PROCESS;
		logger.trace("type={} old={} new={} => process={}", type, zipIndexEntry != null ? zipIndexEntry.getFullyQualifiedPath() : "",
				otherZipIndexEntry != null ? otherZipIndexEntry.getFullyQualifiedPath() : "", shouldBeProcessed);
		return shouldBeProcessed;
	}
}
