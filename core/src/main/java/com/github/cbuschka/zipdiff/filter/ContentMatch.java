package com.github.cbuschka.zipdiff.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ContentMatch
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

	public boolean matches(List<String> lines)
	{
		if (lines == null)
		{
			return false;
		}

		String content = lines.stream().collect(Collectors.joining("\n"));

		boolean included = included(content);
		return included && !excluded(content);
	}

	private boolean included(String content)
	{
		if (this.includes == null || this.includes.isEmpty())
		{
			return true;
		}

		for (String include : this.includes)
		{
			if (Pattern.compile(include, Pattern.MULTILINE | Pattern.DOTALL).matcher(content).matches())
			{
				return true;
			}
		}

		return false;
	}

	private boolean excluded(String content)
	{
		if (this.excludes == null || this.excludes.isEmpty())
		{
			return false;
		}

		for (String exclude : this.excludes)
		{
			if (Pattern.compile(exclude, Pattern.MULTILINE | Pattern.DOTALL).matcher(content).matches())
			{
				return true;
			}
		}

		return false;
	}
}
