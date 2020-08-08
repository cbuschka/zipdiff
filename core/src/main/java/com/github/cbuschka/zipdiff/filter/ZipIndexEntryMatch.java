package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.util.List;
import java.util.regex.Pattern;

public class ZipIndexEntryMatch
{
	private List<String> includes;

	private List<String> excludes;

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
		boolean included = included(zipIndexEntry.getFullyQualifiedPath());
		return included && !excluded(zipIndexEntry.getFullyQualifiedPath());
	}

	private boolean included(String path)
	{
		if (this.includes == null || this.includes.isEmpty())
		{
			return true;
		}

		for (String include : this.includes)
		{
			if (Pattern.matches(include, path))
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
			if (Pattern.matches(exclude, path))
			{
				return true;
			}
		}

		return false;
	}
}
