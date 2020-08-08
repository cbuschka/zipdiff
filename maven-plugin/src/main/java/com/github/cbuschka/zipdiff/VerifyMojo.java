package com.github.cbuschka.zipdiff;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "verify")
public class VerifyMojo extends AbstractMojo
{
	public void execute() throws MojoExecutionException
	{
		getLog().info("Hello, world.");
	}
}
