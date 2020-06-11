package com.github.cbuschka.zipdiff;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

public class ZipDiffToolIntegrationTest
{
	@Rule
	public TestZipFile aZip = TestZipFile.newZipFile();
	@Rule
	public TestZipFile bZip = TestZipFile.newZipFile();

	private ZipDiffTool zipDiffTool = new ZipDiffTool();

	@Test
	public void testIt() throws IOException
	{
		zipDiffTool.run(aZip.getPath(), bZip.getPath());
	}
}
