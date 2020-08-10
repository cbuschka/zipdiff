package com.github.cbuschka.zipdiff.cli;

import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntryType;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.util.EnumSet;
import java.util.List;

public class ZipDiffToolArgsParser
{
	private CommandLineParser commandLineParser = new DefaultParser();

	public ZipDiffToolArgs parse(String... args) throws ParseException
	{
		Options options = new Options();
		options.addOption(Option.builder("q").longOpt("quiet").desc("Suppress all output.").build());
		options.addOption(Option.builder("d").longOpt("diff").desc("Show diffs if possible.").build());
		options.addOption(Option.builder("r").longOpt("recursive").desc("Recurse into nested zip files.").build());
		options.addOption(Option.builder("h").longOpt("help").desc("Print usage information.").build());

		CommandLine commandLine = this.commandLineParser.parse(options, args);
		List<String> argList = commandLine.getArgList();
		boolean usageRequested = argList.size() != 2
				|| commandLine.hasOption("h")
				|| (argList.size() == 1 && argList.get(0).equals("help"));
		boolean quiet = commandLine.hasOption("q");
		boolean diff = commandLine.hasOption("d");
		boolean recurse = commandLine.hasOption("r");

		File fileA = null;
		File fileB = null;
		if (!usageRequested)
		{
			fileA = new File(argList.get(0));
			fileB = new File(argList.get(1));
		}

		return new ZipDiffToolArgs(options, fileA,
				fileB, EnumSet.allOf(ZipIndexDiffEntryType.class),
				quiet, usageRequested, recurse, diff, true);
	}
}
