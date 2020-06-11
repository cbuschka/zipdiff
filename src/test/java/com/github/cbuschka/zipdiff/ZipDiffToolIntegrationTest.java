package com.github.cbuschka.zipdiff;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

public class ZipDiffToolIntegrationTest
{
	@Rule
	public TestZipFile aZip = new TestZipFile("a.zip");
	@Rule
	public TestZipFile bZip = new TestZipFile("b.zip");

	private ZipDiffTool zipDiffTool = new ZipDiffTool();

	@Test
	public void testIt() throws IOException
	{
		zipDiffTool.run(aZip.getFile().getPath(), bZip.getFile().getPath());
	}
}
