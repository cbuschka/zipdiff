package com.github.cbuschka.zipdiff.content_diff;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ManifestContentHandlerTest
{
	ManifestContentHandler differ = new ManifestContentHandler();

	@Test
	public void testIt() throws IOException
	{
		Map<String, String> propsA = differ.parser.read(new StringReader("equalkey: equalvalues\n" //
				+ "removedkey: removedvalue\n" //
				+ "changedkey: value"));
		Map<String, String> propsB = differ.parser.read(new StringReader("equalkey: equalvalues\n" //
				+ "addedkey: addedvalue\n" //
				+ "changedkey: changedvalue"));

		ContentDiff diff = differ.diff(null, propsA, null, propsB);
		List<ContentDiffEntry> entries = diff.getEntries();
		assertEquals(4, entries.size());
		assertEquals(ContentDiffType.CONTENT_UNCHANGED, entries.get(0).getType());
		assertEquals(ContentDiffType.CONTENT_DELETED, entries.get(1).getType());
		assertEquals(ContentDiffType.CONTENT_MODIFIED, entries.get(2).getType());
		assertEquals(ContentDiffType.CONTENT_ADDED, entries.get(3).getType());
	}
}
