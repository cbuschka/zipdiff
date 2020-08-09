package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntryType;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConfigTest
{
	private Config config;

	private ZipIndexEntry zipIndexEntry = new ZipIndexEntry("a.zip", "", "package/Class.class", null, -1L, -1L, -1L, null, null);
	private ZipIndexEntry otherZipIndexEntry = new ZipIndexEntry("b.zip", "", "package/Class.class", null, -1L, -1L, -1L, null, null);

	private void givenConfigIsEmpty()
	{
		this.config = new Config();
	}

	private void givenConfigHasRuleForZipIndexEntry()
	{
		this.config = new Config();
		Rule rule = new Rule();
		rule.getMatch().getOld().getPath().setIncludes(Arrays.asList("package/**/*.class"));
		this.config.getRules().add(rule);
	}

	@Test
	public void newConfigNoRuleForDeleted()
	{
		givenConfigIsEmpty();

		Optional<Rule> optRule = config.getFirstMatchingRule(ZipIndexDiffEntryType.DELETED, zipIndexEntry, null);

		assertFalse(optRule.isPresent());
	}

	@Test
	public void newConfigNoRuleForAdded()
	{
		givenConfigIsEmpty();

		Optional<Rule> optRule = config.getFirstMatchingRule(ZipIndexDiffEntryType.ADDED, null, otherZipIndexEntry);

		assertFalse(optRule.isPresent());
	}

	@Test
	public void ruleMatchesByInclude()
	{
		givenConfigHasRuleForZipIndexEntry();

		Optional<Rule> optRule = config.getFirstMatchingRule(ZipIndexDiffEntryType.DELETED, zipIndexEntry, null);

		assertTrue(optRule.isPresent());
	}
}
