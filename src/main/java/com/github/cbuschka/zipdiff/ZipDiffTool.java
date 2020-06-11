package com.github.cbuschka.zipdiff;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

public class ZipDiffTool
{
	private Function<File, ZipIndexReader> zipIndexReaderOpener = ZipIndexReader::open;

	private ZipDiffer zipDiffer = new ZipDiffer();

	public void run(String[] args) throws IOException
	{
		File a = new File(args[0]);
		File b = new File(args[1]);

		ZipIndex indexA = readZipIndexFrom(a);
		ZipIndex indexB = readZipIndexFrom(b);

		ZipDiff diff = zipDiffer.diff(indexA, indexB);


	}

	private ZipIndex readZipIndexFrom(File file) throws IOException
	{
		try (ZipIndexReader in = zipIndexReaderOpener.apply(file);)
		{
			ZipIndex zipIndex = in.read();
			return zipIndex;
		}
	}
}
