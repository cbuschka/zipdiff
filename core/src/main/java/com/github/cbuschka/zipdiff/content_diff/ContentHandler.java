package com.github.cbuschka.zipdiff.content_diff;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

public interface ContentHandler
{
	boolean handles(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry);

	ContentDiff diff(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry);
}
