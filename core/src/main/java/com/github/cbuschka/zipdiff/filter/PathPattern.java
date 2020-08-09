package com.github.cbuschka.zipdiff.filter;

import java.util.regex.Pattern;

public class PathPattern
{
	public static boolean matches(String pattern, String path)
	{
		String regex = "^"
				+ pattern
				.replaceAll("\\*\\*[/!](.)", "__PATH_DOUBLE_STAR_SLASH__$1")
				.replace("**", "__PATH_DOUBLE_STAR__")
				.replace(".", "__PATH_DOT__")
				.replace("*", "__PATH_SINGLE_STAR__")
				.replace("__PATH_SINGLE_STAR__", "[^/!]+")
				.replace("__PATH_DOUBLE_STAR__", ".*")
				.replace("__PATH_DOUBLE_STAR_SLASH__", ".*")
				.replace("__PATH_DOT__", "\\.")
				+ "$";
		return Pattern.matches(regex, path);
	}
}
