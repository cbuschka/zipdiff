package com.github.cbuschka.zipdiff.maven_plugin;

import com.github.cbuschka.zipdiff.diff.ZipIndexDiff;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffer;
import com.github.cbuschka.zipdiff.filter.Config;
import com.github.cbuschka.zipdiff.filter.DiffRegisteringZipIndexDiffFilter;
import com.github.cbuschka.zipdiff.filter.Rule;
import com.github.cbuschka.zipdiff.filter.RuleBasedZipIndexDiffFilter;
import com.github.cbuschka.zipdiff.index.ZipIndex;
import com.github.cbuschka.zipdiff.index.ZipIndexReader;
import com.github.cbuschka.zipdiff.io.NullStringOut;
import com.github.cbuschka.zipdiff.process.ZipIndexDiffProcessor;
import com.github.cbuschka.zipdiff.report.ZipIndexDiffWriter;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.PluginParameterExpressionEvaluator;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "verify", defaultPhase = LifecyclePhase.VERIFY, requiresProject = true)
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
	@Parameter(property = "failIfDiffsPresent", defaultValue = "true")
	private boolean failIfDiffsPresent = true;
	@Parameter
	private List<Rule> rules = new ArrayList<>();
	@Parameter(defaultValue = "${session}", required = true, readonly = true)
	private MavenSession session;

	public void execute() throws MojoExecutionException
	{
		PluginParameterExpressionEvaluator expressionEvaluator = new PluginParameterExpressionEvaluator(session);
		try
		{
			Log log = getLog();
			log.info("Verifying...");
			log.info(String.format("Old: %s", expressionEvaluator.evaluate(old.getAbsolutePath(), String.class)));
			log.info(String.format("New: %s", expressionEvaluator.evaluate(new_.getAbsolutePath(), String.class)));

			ZipIndexDiff zipIndexDiff = diff();
			boolean diffsPresent = process(zipIndexDiff);
			if (diffsPresent && failIfDiffsPresent)
			{
				throw new MojoExecutionException("There are diffs present. Failing.");
			}
		}
		catch (IOException | ExpressionEvaluationException ex)
		{
			throw new MojoExecutionException("Failed to verify.", ex);
		}
	}

	private boolean process(ZipIndexDiff zipIndexDiff) throws IOException
	{
		ZipIndexDiffWriter writer = new ZipIndexDiffWriter(this.quiet ? new NullStringOut() : new MavenStringOut(getLog()));
		DiffRegisteringZipIndexDiffFilter diffRegisteringFilter = new DiffRegisteringZipIndexDiffFilter(writer);
		Config config = new Config();
		config.setRules(this.rules);
		RuleBasedZipIndexDiffFilter rulesBasedFilter = new RuleBasedZipIndexDiffFilter(config, diffRegisteringFilter);
		ZipIndexDiffProcessor diffProcessor = new ZipIndexDiffProcessor(rulesBasedFilter, true);
		diffProcessor.process(zipIndexDiff);
		return diffRegisteringFilter.isDiffPresent();
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
