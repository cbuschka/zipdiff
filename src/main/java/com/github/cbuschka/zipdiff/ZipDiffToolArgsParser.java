package com.github.cbuschka.zipdiff;

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
		options.addOption(Option.builder("q").longOpt("quiet").build());
		CommandLine commandLine = this.commandLineParser.parse(options, args);
		List<String> argList = commandLine.getArgList();
		if (argList.size() != 2)
		{
			throw new ParseException("Expected two files to diff.");
		}

		return new ZipDiffToolArgs(new File(argList.get(0)),
				new File(argList.get(1)), EnumSet.allOf(ZipDiffEntryType.class),
				commandLine.hasOption("q"));
	}
}
