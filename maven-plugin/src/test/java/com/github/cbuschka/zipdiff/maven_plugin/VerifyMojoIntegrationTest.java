package com.github.cbuschka.zipdiff.maven_plugin;

import com.github.cbuschka.zipdiff.TestZipFile;
import com.github.cbuschka.zipdiff.TestZipFileBuilder;
import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VerifyMojoIntegrationTest
{
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	@Rule
	public TestZipFile emptyZipFile = TestZipFile.from(TestZipFileBuilder.newZipFile());
	@Rule
	public TestZipFile emptyDirOnlyZipFile = TestZipFile.from(TestZipFileBuilder.newZipFile().withEntry("dir"));

	private File testDir;

	private Verifier verifier;

	private Map<String, String> env = new HashMap<>();

	@Before
	public void setup() throws VerificationException, IOException
	{
		this.testDir = tempFolder.newFolder();
		verifier = new Verifier(testDir.getAbsolutePath());
		verifier.addCliOption("-N");
		verifier.deleteArtifact("com.github.cbuschka.zipdiff", "zipdiff-maven-plugin-it-pom", "1.0.0-SNAPSHOT", "pom");
		verifier.resetStreams();

		env.put("PLUGIN_VERSION", "2.0.0-SNAPSHOT");
	}

	@Test
	public void bothEmptyNoRules() throws Exception
	{
		ResourceExtractor.extractResourceToDestination(getClass(), "/no-rules/", this.testDir, true);

		env.put("OLD_FILE", emptyZipFile.getFile().getAbsolutePath());
		env.put("NEW_FILE", emptyZipFile.getFile().getAbsolutePath());
		verifier.executeGoal("com.github.cbuschka.zipdiff:zipdiff-maven-plugin:verify", env);

		verifier.verifyErrorFreeLog();
	}

	@Test
	public void ignoreEmptyDirs() throws Exception
	{
		ResourceExtractor.extractResourceToDestination(getClass(), "/ignore-empty-dirs/", this.testDir, true);

		env.put("OLD_FILE", emptyZipFile.getFile().getAbsolutePath());
		env.put("NEW_FILE", emptyDirOnlyZipFile.getFile().getAbsolutePath());
		verifier.executeGoal("com.github.cbuschka.zipdiff:zipdiff-maven-plugin:verify", env);

		verifier.verifyErrorFreeLog();
	}

	@Test
	public void big() throws Exception
	{
		ResourceExtractor.extractResourceToDestination(getClass(), "/big/", this.testDir, true);

		env.put("OLD_FILE", emptyZipFile.getFile().getAbsolutePath());
		env.put("NEW_FILE", emptyZipFile.getFile().getAbsolutePath());
		verifier.executeGoal("com.github.cbuschka.zipdiff:zipdiff-maven-plugin:verify", env);

		verifier.verifyErrorFreeLog();
	}
}
