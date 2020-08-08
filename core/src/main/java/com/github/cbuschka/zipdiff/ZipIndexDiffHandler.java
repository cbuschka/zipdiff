package com.github.cbuschka.zipdiff;

import java.util.List;

public interface ZipIndexDiffHandler
{
	void begin(ZipIndexDiff zipIndexDiff);

	void added(ZipIndexEntry otherZipIndexEntry);

	void deleted(ZipIndexEntry zipIndexEntry);

	void unchanged(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry);

	void renamed(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry);

	void modified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry);

	void startContentModified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry);

	void modifiedContentChanged(List<String> oldLines, List<String> newLines);

	void modifiedContentDeleted(List<String> oldLines);

	void modifiedContentInserted(List<String> newLines);

	void modifiedContentEqual(List<String> oldLines);

	void endContentModified();

	void finished();
}
