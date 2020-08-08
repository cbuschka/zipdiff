package com.github.cbuschka.zipdiff.cli;

import org.apache.commons.cli.ParseException;

import java.io.IOException;

public class Main
{
	public static void main(String[] args) throws IOException, ParseException
	{
		int exitCode = new ZipDiffTool().run(args);
		System.exit(exitCode);
	}
}
