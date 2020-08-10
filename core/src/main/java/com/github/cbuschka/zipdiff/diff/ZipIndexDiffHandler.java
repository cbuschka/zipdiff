package com.github.cbuschka.zipdiff.diff;

import com.github.cbuschka.zipdiff.content_diff.ContentDiff;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.util.List;

public interface ZipIndexDiffHandler
{
	void begin(ZipIndexDiff zipIndexDiff);

	void added(ZipIndexEntry otherZipIndexEntry);

	void deleted(ZipIndexEntry zipIndexEntry);

	void unchanged(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry);

	void renamed(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry);

	void modified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry, ContentDiff contentDiff);

	void finished();
}
