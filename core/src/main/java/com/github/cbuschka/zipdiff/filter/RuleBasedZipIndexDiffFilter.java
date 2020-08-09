package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.content_diff.ContentDiffType;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntryType;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffHandler;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		if (shouldBeForwarded(ZipIndexDiffEntryType.ADDED, null, otherZipIndexEntry))
		{
			super.added(otherZipIndexEntry);
		}
	}

	@Override
	public void deleted(ZipIndexEntry zipIndexEntry)
	{
		if (shouldBeForwarded(ZipIndexDiffEntryType.DELETED, zipIndexEntry, null))
		{
			super.deleted(zipIndexEntry);
		}
	}

	@Override
	public void unchanged(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		if (shouldBeForwarded(ZipIndexDiffEntryType.UNCHANGED, zipIndexEntry, otherZipIndexEntry))
		{
			super.unchanged(zipIndexEntry, otherZipIndexEntry);
		}
	}

	@Override
	public void renamed(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		if (shouldBeForwarded(ZipIndexDiffEntryType.RENAMED, zipIndexEntry, otherZipIndexEntry))
		{
			super.renamed(zipIndexEntry, otherZipIndexEntry);
		}
	}

	@Override
	public void modified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		if (shouldBeForwarded(ZipIndexDiffEntryType.MODIFIED, zipIndexEntry, otherZipIndexEntry))
		{
			super.modified(zipIndexEntry, otherZipIndexEntry);
		}
	}

	@Override
	public void startContentModified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		if (shouldBeForwarded(ZipIndexDiffEntryType.MODIFIED, zipIndexEntry, otherZipIndexEntry))
		{
			super.startContentModified(zipIndexEntry, otherZipIndexEntry);
		}
	}

	@Override
	public void contentModified(ZipIndexEntry zipIndexEntry, List<String> oldLines, ZipIndexEntry otherZipIndexEntry, List<String> newLines)
	{
		if (shouldBeForwarded(ZipIndexDiffEntryType.MODIFIED, zipIndexEntry, otherZipIndexEntry)
				&& shouldBeForwarded(ContentDiffType.CONTENT_MODIFIED, zipIndexEntry, otherZipIndexEntry))
		{
			super.contentModified(zipIndexEntry, oldLines, otherZipIndexEntry, newLines);
		}
	}

	@Override
	public void contentDeleted(ZipIndexEntry zipIndexEntry, List<String> oldLines, ZipIndexEntry otherZipIndexEntry)
	{
		if (shouldBeForwarded(ZipIndexDiffEntryType.MODIFIED, zipIndexEntry, otherZipIndexEntry)
				&& shouldBeForwarded(ContentDiffType.CONTENT_DELETED, zipIndexEntry, otherZipIndexEntry))
		{
			handler.contentDeleted(zipIndexEntry, oldLines, otherZipIndexEntry);
		}
	}

	@Override
	public void contentAdded(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry, List<String> newLines)
	{
		if (shouldBeForwarded(ZipIndexDiffEntryType.MODIFIED, zipIndexEntry, otherZipIndexEntry)
				&& shouldBeForwarded(ContentDiffType.CONTENT_ADDED, null, otherZipIndexEntry))
		{
			super.contentAdded(zipIndexEntry, otherZipIndexEntry, newLines);
		}
	}

	@Override
	public void contentUnchanged(ZipIndexEntry zipIndexEntry, List<String> oldLines, ZipIndexEntry otherZipIndexEntry)
	{
		if (shouldBeForwarded(ZipIndexDiffEntryType.MODIFIED, zipIndexEntry, otherZipIndexEntry)
				&& shouldBeForwarded(ContentDiffType.CONTENT_UNCHANGED, zipIndexEntry, otherZipIndexEntry))
		{
			super.contentUnchanged(zipIndexEntry, oldLines, otherZipIndexEntry);
		}
	}

	@Override
	public void endContentModified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		if (shouldBeForwarded(ZipIndexDiffEntryType.MODIFIED, zipIndexEntry, otherZipIndexEntry))
		{
			super.endContentModified(zipIndexEntry, otherZipIndexEntry);
		}
	}

	@Override
	public void finished()
	{
		super.finished();
	}

	private boolean shouldBeForwarded(ZipIndexDiffEntryType type, ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		Optional<Rule> optRule = this.config.getFirstMatchingRule(type, zipIndexEntry, otherZipIndexEntry);
		boolean shouldBeForwarded = optRule.map(Rule::getAction).orElse(this.config.getDefaultAction()) == Action.PROCESS;
		if (logger.isTraceEnabled())
		{
			logger.trace("type={} old={} new={} => forward={}", type, zipIndexEntry != null ? zipIndexEntry.getFullyQualifiedPath() : "",
					otherZipIndexEntry != null ? otherZipIndexEntry.getFullyQualifiedPath() : "", shouldBeForwarded);
		}
		return shouldBeForwarded;
	}

	private boolean shouldBeForwarded(ContentDiffType type, ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		Optional<Rule> optRule = this.config.getFirstMatchingRule(type, zipIndexEntry, otherZipIndexEntry);
		boolean shouldBeForwarded = optRule.map(Rule::getAction).orElse(this.config.getDefaultAction()) == Action.PROCESS;
		if (logger.isTraceEnabled())
		{
			logger.trace("type={} old={} new={} => forward={}", type, zipIndexEntry != null ? zipIndexEntry.getFullyQualifiedPath() : "",
					otherZipIndexEntry != null ? otherZipIndexEntry.getFullyQualifiedPath() : "", shouldBeForwarded);
		}
		return shouldBeForwarded;
	}
}
