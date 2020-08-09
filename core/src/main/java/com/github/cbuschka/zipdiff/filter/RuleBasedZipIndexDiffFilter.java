package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.diff.ZipIndexDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntryType;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffHandler;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class RuleBasedZipIndexDiffFilter extends AbstractZipIndexDiffFilter
{
	private static Logger logger = LoggerFactory.getLogger(RuleBasedZipIndexDiffFilter.class);

	private Config config;
	private Boolean forwardModifiedContent;

	public RuleBasedZipIndexDiffFilter(Config config, ZipIndexDiffHandler handler)
	{
		super(handler);
		this.config = config;
	}

	@Override
	public void begin(ZipIndexDiff zipIndexDiff)
	{
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
			this.forwardModifiedContent = Boolean.TRUE;
			super.startContentModified(zipIndexEntry, otherZipIndexEntry);
		}
		else
		{
			this.forwardModifiedContent = Boolean.FALSE;
		}
	}

	@Override
	public void modifiedContentChanged(List<String> oldLines, List<String> newLines)
	{
		if (this.forwardModifiedContent)
		{
			super.modifiedContentChanged(oldLines, newLines);
		}
	}

	@Override
	public void modifiedContentDeleted(List<String> oldLines)
	{
		if (this.forwardModifiedContent)
		{
			handler.modifiedContentDeleted(oldLines);
		}
	}

	@Override
	public void modifiedContentInserted(List<String> newLines)
	{
		if (this.forwardModifiedContent)
		{
			super.modifiedContentInserted(newLines);
		}
	}

	@Override
	public void modifiedContentEqual(List<String> oldLines)
	{
		if (this.forwardModifiedContent)
		{
			super.modifiedContentEqual(oldLines);
		}
	}

	@Override
	public void endContentModified()
	{
		super.endContentModified();
		this.forwardModifiedContent = null;
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

}
