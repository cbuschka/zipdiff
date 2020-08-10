package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.util.List;

public class Rule
{
	private String id;

	private Match match = new Match();

	private Action action = Action.PROCESS;

	public Rule()
	{
	}

	public void setAction(Action action)
	{
		this.action = action;
	}

	public void setMatch(Match match)
	{
		this.match = match;
	}

	public Action getAction()
	{
		return action;
	}

	public Match getMatch()
	{
		return match;
	}

	public boolean matches(DiffType type, ZipIndexEntry zipIndexEntry, List<String> oldLines, ZipIndexEntry otherZipIndexEntry, List<String> newLines)
	{
		return this.match != null && this.match.matches(type, zipIndexEntry, oldLines, otherZipIndexEntry, newLines);
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}
}
