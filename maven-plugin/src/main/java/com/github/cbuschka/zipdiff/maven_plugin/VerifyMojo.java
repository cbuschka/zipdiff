package com.github.cbuschka.zipdiff.maven_plugin;

import com.github.cbuschka.zipdiff.diff.ZipIndexDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffer;
import com.github.cbuschka.zipdiff.filter.Config;
import com.github.cbuschka.zipdiff.filter.Rule;
import com.github.cbuschka.zipdiff.filter.ZipIndexDiffFilter;
import com.github.cbuschka.zipdiff.index.ZipIndex;
import com.github.cbuschka.zipdiff.index.ZipIndexReader;
import com.github.cbuschka.zipdiff.io.NullStringOut;
import com.github.cbuschka.zipdiff.process.ZipIndexDiffProcessor;
import com.github.cbuschka.zipdiff.report.ZipIndexDiffWriter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "verify")
public class VerifyMojo extends AbstractMojo
{
	@Parameter(property = "old", required = true)
	private File old;
	@Parameter(name = "new", property = "new", required = true)
	private File new_;
	@Parameter(property = "recurse")
	private boolean recurse = true;
	@Parameter(property = "quiet")
	private boolean quiet = false;
	@Parameter
	private List<Rule> rules = new ArrayList<>();

	public void execute() throws MojoExecutionException
	{
		try
		{
			Log log = getLog();
			log.info("Verifying...");
			log.info(String.format("Old: %s", old.getAbsolutePath()));
			log.info(String.format("New: %s", new_.getAbsolutePath()));

			ZipIndexDiff zipIndexDiff = diff();
			process(zipIndexDiff);
		}
		catch (IOException ex)
		{
			throw new MojoExecutionException("Failed to verify.", ex);
		}
	}

	private void process(ZipIndexDiff zipIndexDiff) throws IOException
	{
		ZipIndexDiffWriter writer = new ZipIndexDiffWriter(this.quiet ? new NullStringOut() : new MavenStringOut(getLog()));
		Config config = new Config();
		config.setRules(this.rules);
		ZipIndexDiffFilter filter = new ZipIndexDiffFilter(config, writer);
		ZipIndexDiffProcessor diffProcessor = new ZipIndexDiffProcessor(filter, true);
		diffProcessor.process(zipIndexDiff);
	}

	private ZipIndexDiff diff() throws IOException
	{
		ZipIndex zipIndexA = readZipIndex(this.old);
		ZipIndex zipIndexB = readZipIndex(this.new_);

		ZipIndexDiffer zipIndexDiffer = new ZipIndexDiffer(this.recurse);
		ZipIndexDiff zipIndexDiff = zipIndexDiffer.diff(zipIndexA, zipIndexB);
		return zipIndexDiff;
	}

	private ZipIndex readZipIndex(File file) throws IOException
	{
		ZipIndexReader reader = ZipIndexReader.open(file);
		ZipIndex zipIndex = reader.read();
		reader.close();
		return zipIndex;
	}

	public void setOld(File old)
	{
		this.old = old;
	}

	public File getOld()
	{
		return old;
	}

	public File getNew()
	{
		return new_;
	}

	public void setNew(File new_)
	{
		this.new_ = new_;
	}

	public List<Rule> getRules()
	{
		return rules;
	}

	public boolean isQuiet()
	{
		return quiet;
	}

	public boolean isRecurse()
	{
		return recurse;
	}
}
