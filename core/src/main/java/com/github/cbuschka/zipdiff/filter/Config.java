package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntryType;
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

	public Optional<Rule> getFirstMatchingRule(ZipIndexDiffEntryType type, ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry) {
		List<Rule> matchingRules = getMatchingRules(type, zipIndexEntry, otherZipIndexEntry);
		if(matchingRules.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(matchingRules.get(0));
	}

	public List<Rule> getMatchingRules(ZipIndexDiffEntryType type, ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		List<Rule> matchingRules = new ArrayList<>();
		for (Rule rule : this.rules)
		{
			if (rule.matches(type, zipIndexEntry, otherZipIndexEntry))
			{
				matchingRules.add(rule);
			}
		}

		return matchingRules;
	}

}
