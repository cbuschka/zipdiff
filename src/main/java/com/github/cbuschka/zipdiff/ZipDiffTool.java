package com.github.cbuschka.zipdiff;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

public class ZipDiffTool
{
	private ZipDiffToolArgsParser argsParser = new ZipDiffToolArgsParser();
	private Function<File, ZipIndexReader> zipIndexReaderOpener = ZipIndexReader::open;
	private ZipDiffer zipDiffer = new ZipDiffer(true);

	public int run(String... args) throws IOException, ParseException
	{
		ZipDiffToolArgs toolArgs = argsParser.parse(args);
		if (toolArgs.isUsageRequested())
		{
			printUsage(toolArgs);
			return 2;
		}
		else
		{
			return runDiff(toolArgs);
		}
	}

	private void printUsage(ZipDiffToolArgs toolArgs)
	{
		HelpFormatter formatter = new HelpFormatter();
		formatter.setLeftPadding(2);
		formatter.printHelp("zipdiff [options] <zipfile1> <zipfile2>",
				"\nA command line tool to diff jar/zip files.\n\n",
				toolArgs.getOptions(), "\nPlease report issues at https://github.com/cbuschka/zipdiff/issues.\n",
				false);
	}

	private int runDiff(ZipDiffToolArgs toolArgs) throws IOException
	{
		this.zipDiffer.setRecurse(toolArgs.isRecurse());

		ZipDiff diff = calcDiff(toolArgs.getFileA(), toolArgs.getFileB());

		writeDiff(toolArgs, diff);

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
