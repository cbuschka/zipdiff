package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

public class ZipIndexEntryMatch
{
	private PathMatch path = new PathMatch();

	public boolean matches(ZipIndexEntry zipIndexEntry)
	{
		return this.path.matches(zipIndexEntry);
	}

	public PathMatch getPath()
	{
		return path;
	}

	public void setPath(PathMatch path)
	{
		this.path = path;
	}
}
