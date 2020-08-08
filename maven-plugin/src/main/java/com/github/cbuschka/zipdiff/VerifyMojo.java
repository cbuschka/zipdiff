package com.github.cbuschka.zipdiff;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Mojo(name = "verify")
public class VerifyMojo extends AbstractMojo
{
	@Parameter(property = "a", required = true)
	private File a;
	@Parameter(property = "b", required = true)
	private File b;
	@Parameter(property = "recurse")
	private boolean recurse = true;
	@Parameter(property = "quiet")
	private boolean quiet = false;

	public void execute() throws MojoExecutionException
	{
		try
		{
			Log log = getLog();
			log.info("Verifying...");
			log.info(String.format("File a: %s", a.getAbsolutePath()));
			log.info(String.format("File b: %s", b.getAbsolutePath()));

			ZipIndexDiff zipIndexDiff = diff();

			dump(zipIndexDiff);
		}
		catch (IOException ex)
		{
			throw new MojoExecutionException("Failed to verify.", ex);
		}
	}

	private void dump(ZipIndexDiff zipIndexDiff) throws IOException
	{
		try (ZipDiffWriter diffWriter = new ZipDiffWriter(this.quiet ? new NullStringOut() : new MavenStringOut(getLog()), true))
		{
			diffWriter.write(zipIndexDiff);
		}
	}

	private ZipIndexDiff diff() throws IOException
	{
		ZipIndex zipIndexA = readZipIndex(this.a);
		ZipIndex zipIndexB = readZipIndex(this.b);

		ZipIndexDiffer zipIndexDiffer = new ZipIndexDiffer(this.recurse);
		ZipIndexDiff zipIndexDiff = zipIndexDiffer.diff(zipIndexA, zipIndexB);
		return zipIndexDiff;
	}

	private ZipIndex readZipIndex(File file) throws IOException
	{
		ZipIndexReader reader = new ZipIndexReader("", "", "", new FileInputStream(file));
		ZipIndex zipIndex = reader.read();
		reader.close();
		return zipIndex;
	}
}
