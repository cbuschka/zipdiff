package com.github.cbuschka.zipdiff.content_diff;

import java.util.List;

public class ContentDiffEntry
{
	private ContentDiffType type;

	private List<String> oldLines;

	private List<String> newLines;

	public ContentDiffEntry(ContentDiffType type, List<String> oldLines, List<String> newLines)
	{
		this.type = type;
		this.oldLines = oldLines;
		this.newLines = newLines;
	}

	public ContentDiffType getType()
	{
		return type;
	}

	public List<String> getNewLines()
	{
		return newLines;
	}

	public List<String> getOldLines()
	{
		return oldLines;
	}
}
