package com.github.cbuschka.zipdiff.cli;

import com.github.cbuschka.zipdiff.TestZipFile;
import com.github.cbuschka.zipdiff.cli.ZipDiffTool;
import org.apache.commons.cli.ParseException;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

public class ZipIndexDiffToolTest
{
	@Rule
	public TestZipFile aZip = TestZipFile.from("a.zip");
	@Rule
	public TestZipFile bZip = TestZipFile.from("b.zip");

	private ZipDiffTool zipDiffTool = new ZipDiffTool();

	@Test
	public void testIt() throws IOException, ParseException
	{
		zipDiffTool.run(aZip.getFile().getPath(), bZip.getFile().getPath());
	}
}
