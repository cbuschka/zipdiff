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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
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
	public void setup() throws VerificationException, IOException, ParserConfigurationException, SAXException
	{
		this.testDir = tempFolder.newFolder();
		verifier = new Verifier(testDir.getAbsolutePath());
		verifier.addCliOption("-N");
		verifier.deleteArtifact("com.github.cbuschka.zipdiff", "zipdiff-maven-plugin-it-pom", "1.0.0-SNAPSHOT", "pom");
		verifier.resetStreams();

		env.put("PLUGIN_VERSION", getPluginVersion());
	}

	private String getPluginVersion() throws IOException, ParserConfigurationException, SAXException
	{
		File basedir = new File("");
		File pomXmlFile = new File(basedir.getParentFile(), "pom.xml");
		if (!pomXmlFile.exists())
		{
			throw new FileNotFoundException("No pom.xml found: " + pomXmlFile.getAbsolutePath());
		}

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document doc = documentBuilder.parse(pomXmlFile);
		Element documentElement = doc.getDocumentElement();
		NodeList versionElements = documentElement.getElementsByTagName("version");
		if (versionElements.getLength() == 0)
		{
			throw new IllegalStateException("No <version> tag in pom.xml found.");
		}
		Element versionElement = (Element) versionElements.item(0);
		String version = versionElement.getTextContent().trim();
		return version;
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
