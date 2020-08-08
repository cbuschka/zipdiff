package com.github.cbuschka.zipdiff;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;

public class ZipIndexReaderIntegrationTest
{
	@Test
	public void testIt() throws IOException
	{
		URL url = getClass().getResource("/test.zip");
		ZipIndex zipIndex = new ZipIndexReader(url.toExternalForm(), "", "", url.openStream()).read();
		Writer wr = new OutputStreamWriter(System.err, "UTF-8");
		new ZipIndexDumper(wr).dump(zipIndex);
		wr.close();
	}
}
