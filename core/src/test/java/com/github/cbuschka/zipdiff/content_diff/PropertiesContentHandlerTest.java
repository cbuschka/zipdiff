package com.github.cbuschka.zipdiff.content_diff;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PropertiesContentHandlerTest
{

	@Test
	public void testIt()
	{
		Map<String, String> propsA = new LinkedHashMap<>();
		propsA.put("equalkey", "equalvalue");
		propsA.put("removedkey", "removedvalue");
		propsA.put("changedkey", "value");
		Map<String, String> propsB = new LinkedHashMap<>();
		propsB.put("equalkey", "equalvalue");
		propsB.put("addedkey", "addedvalue");
		propsB.put("changedkey", "changedvalue");

		PropertiesContentHandler differ = new PropertiesContentHandler();
		ContentDiff diff = differ.diff(null, propsA, null, propsB);
		List<ContentDiffEntry> entries = diff.getEntries();
		assertEquals(4, entries.size());
		assertEquals(ContentDiffType.CONTENT_UNCHANGED, entries.get(0).getType());
		assertEquals(ContentDiffType.CONTENT_DELETED, entries.get(1).getType());
		assertEquals(ContentDiffType.CONTENT_MODIFIED, entries.get(2).getType());
		assertEquals(ContentDiffType.CONTENT_ADDED, entries.get(3).getType());
	}
}
