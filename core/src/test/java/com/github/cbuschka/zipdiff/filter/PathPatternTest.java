package com.github.cbuschka.zipdiff.filter;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PathPatternTest
{
	@Test
	public void extGlob()
	{
		boolean matches = PathPattern.matches("*.class", "test.class");

		assertTrue(matches);
	}

	@Test
	public void extGlobWithSingleFolder()
	{
		boolean matches = PathPattern.matches("folder/*.class", "folder/test.class");

		assertTrue(matches);
	}

	@Test
	public void dirAndExtGlobWithSingleFolder()
	{
		boolean matches = PathPattern.matches("**/*.class", "folder/test.class");

		assertTrue(matches);
	}

	@Test
	public void dirAndExtGlobWithMultipleFolders()
	{
		boolean matches = PathPattern.matches("**/*.class", "folder/sub/test.class");

		assertTrue(matches);
	}

	@Test
	public void dirGlobAtEnd()
	{
		boolean matches = PathPattern.matches("**/", "folder/sub/");

		assertTrue(matches);
	}
}
