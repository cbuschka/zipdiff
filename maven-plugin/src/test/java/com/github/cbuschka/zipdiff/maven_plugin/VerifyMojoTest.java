package com.github.cbuschka.zipdiff.maven_plugin;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class VerifyMojoTest
{
	@Rule
	public TestResources resources = new TestResources();

	@Rule
	public MojoRule rule = new MojoRule();

	@Test
	public void testExecute() throws Exception
	{
		File project = resources.getBasedir("basic");
		File pom = new File(project, "pom.xml");
		assertNotNull(pom);
		assertTrue(pom.exists());

		VerifyMojo mojo = (VerifyMojo) rule.lookupMojo("verify", pom);
		assertNotNull(mojo);

		mojo.execute();
	}
}
