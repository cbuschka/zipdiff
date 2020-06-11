package com.github.cbuschka.zipdiff;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.function.Function;

public class ZipDiffTool
{
	private Function<File, ZipIndexReader> zipIndexReaderOpener = ZipIndexReader::open;
	private Function<Writer, ZipDiffWriter> zipDiffWriterNew = ZipDiffWriter::new;

	private ZipDiffer zipDiffer = new ZipDiffer();

	public void run(String... args) throws IOException
	{
		File a = new File(args[0]);
		File b = new File(args[1]);

		ZipDiff diff = calcDiff(a, b);

		writeDiff(diff);
	}

	private ZipDiff calcDiff(File a, File b) throws IOException
	{
		ZipIndex indexA = readZipIndexFrom(a);
		ZipIndex indexB = readZipIndexFrom(b);
		return zipDiffer.diff(indexA, indexB);
	}

	private void writeDiff(ZipDiff diff) throws IOException
	{
		try (ZipDiffWriter diffWriter = this.zipDiffWriterNew.apply(new OutputStreamWriter(System.out, Charset.defaultCharset()));)
		{
			diffWriter.write(diff);
		}
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
