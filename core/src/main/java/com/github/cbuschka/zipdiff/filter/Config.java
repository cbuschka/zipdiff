package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Config
{
	private List<Rule> rules = new ArrayList<>();
	private Action defaultAction = Action.PROCESS;

	public List<Rule> getRules()
	{
		return rules;
	}

	public void setRules(List<Rule> rules)
	{
		this.rules = rules;
	}

	public Action getDefaultAction()
	{
		return defaultAction;
	}

	public void setDefaultAction(Action defaultAction)
	{
		this.defaultAction = defaultAction;
	}

	public Optional<Rule> getFirstMatchingRule(DiffType type, ZipIndexEntry zipIndexEntry, List<String> oldLines, ZipIndexEntry otherZipIndexEntry, List<String> newLines)
	{
		List<Rule> matchingRules = getMatchingRules(type, zipIndexEntry, oldLines, otherZipIndexEntry, newLines);
		if (matchingRules.isEmpty())
		{
			return Optional.empty();
		}

		return Optional.of(matchingRules.get(0));
	}

	private List<Rule> getMatchingRules(DiffType type, ZipIndexEntry zipIndexEntry, List<String> oldLines, ZipIndexEntry otherZipIndexEntry, List<String> newLines)
	{
		List<Rule> matchingRules = new ArrayList<>();
		for (Rule rule : this.rules)
		{
			if (rule.matches(type, zipIndexEntry, oldLines, otherZipIndexEntry, newLines))
			{
				matchingRules.add(rule);
			}
		}

		return matchingRules;
	}
}
