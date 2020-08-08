package com.github.cbuschka.zipdiff.filter;

import java.util.ArrayList;
import java.util.List;

public class Config
{
	private List<Rule> rules = new ArrayList<>();

	public List<Rule> getRules()
	{
		return rules;
	}

	public void setRules(List<Rule> rules)
	{
		this.rules = rules;
	}
}
