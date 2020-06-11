package com.github.cbuschka.zipdiff;

import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

public class ZipDiffTool
{
	private Function<File, ZipIndexReader> zipIndexReaderOpener = ZipIndexReader::open;
	private ZipDiffToolArgsParser argsParser = new ZipDiffToolArgsParser();
	private ZipDiffer zipDiffer = new ZipDiffer();

	public int run(String... stringArgs) throws IOException, ParseException
	{
		ZipDiffToolArgs args = argsParser.parse(stringArgs);

		ZipDiff diff = calcDiff(args.getFileA(), args.getFileB());

		writeDiff(args, diff);

		return diff.containsChanges() ? 1 : 0;
	}

	private ZipDiff calcDiff(File a, File b) throws IOException
	{
		ZipIndex indexA = readZipIndexFrom(a);
		ZipIndex indexB = readZipIndexFrom(b);
		return zipDiffer.diff(indexA, indexB);
	}

	private void writeDiff(ZipDiffToolArgs args, ZipDiff diff) throws IOException
	{
		try (ZipDiffWriter diffWriter = args.isQuiet() ? new NullZipDiffWriter() : new StreamZipDiffWriter();)
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
