package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.util.ArrayList;
import java.util.List;

public class ZipIndexEntryMatch
{
	private List<String> includes = new ArrayList<>();

	private List<String> excludes = new ArrayList<>();

	public List<String> getIncludes()
	{
		return includes;
	}

	public void setIncludes(List<String> includes)
	{
		this.includes = includes;
	}

	public List<String> getExcludes()
	{
		return excludes;
	}

	public void setExcludes(List<String> excludes)
	{
		this.excludes = excludes;
	}

	public boolean matches(ZipIndexEntry zipIndexEntry)
	{
		String path = zipIndexEntry.getFullyQualifiedPath();
		return matches(path);
	}

	public boolean matches(String path)
	{
		boolean included = included(path);
		return included && !excluded(path);
	}

	private boolean included(String path)
	{
		if (this.includes == null || this.includes.isEmpty())
		{
			return true;
		}

		for (String include : this.includes)
		{
			if (PathPattern.matches(include, path))
			{
				return true;
			}
		}

		return false;
	}

	private boolean excluded(String path)
	{
		if (this.excludes == null || this.excludes.isEmpty())
		{
			return false;
		}

		for (String exclude : this.excludes)
		{
			if (PathPattern.matches(exclude, path))
			{
				return true;
			}
		}

		return false;
	}
}
