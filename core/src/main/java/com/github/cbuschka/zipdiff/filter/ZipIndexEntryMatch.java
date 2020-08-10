package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.util.List;

public class ZipIndexEntryMatch
{
	private PathMatch path = new PathMatch();

	private ContentMatch content = new ContentMatch();

	public boolean matches(ZipIndexEntry zipIndexEntry, List<String> lines)
	{
		if (!this.path.matches(zipIndexEntry))
		{
			return false;
		}

		if (lines != null && !this.content.matches(lines))
		{
			return false;
		}

		return true;
	}

	public ContentMatch getContent()
	{
		return content;
	}

	public PathMatch getPath()
	{
		return path;
	}

	public void setPath(PathMatch path)
	{
		this.path = path;
	}

	public void setContent(ContentMatch content)
	{
		this.content = content;
	}
}
