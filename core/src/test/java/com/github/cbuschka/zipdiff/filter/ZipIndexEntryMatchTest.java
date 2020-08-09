package com.github.cbuschka.zipdiff.filter;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ZipIndexEntryMatchTest
{

	@Test
	public void slashAtEndMatchesDirEntry()
	{
		ZipIndexEntryMatch match = new ZipIndexEntryMatch();
		match.setIncludes(Arrays.asList("**/"));
		boolean matches = match.matches("folder/dir/");
		assertTrue(matches);
	}

	@Test
	public void slashAtEndDoesNotMatchFileEntry()
	{
		ZipIndexEntryMatch match = new ZipIndexEntryMatch();
		match.setIncludes(Arrays.asList("**/"));
		boolean matches = match.matches("folder/sub/test.class");
		assertFalse(matches);
	}
}
