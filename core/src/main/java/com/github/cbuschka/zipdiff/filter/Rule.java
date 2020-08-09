package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntryType;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

public class Rule
{
	private String id;

	private ZipIndexDiffEntryMatch match = new ZipIndexDiffEntryMatch();

	private Action action = Action.PROCESS;

	public Rule()
	{
	}

	public void setAction(Action action)
	{
		this.action = action;
	}

	public void setMatch(ZipIndexDiffEntryMatch match)
	{
		this.match = match;
	}

	public Action getAction()
	{
		return action;
	}

	public ZipIndexDiffEntryMatch getMatch()
	{
		return match;
	}

	public boolean matches(ZipIndexDiffEntryType type, ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		return this.match != null && this.match.matches(type, zipIndexEntry, otherZipIndexEntry);
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
