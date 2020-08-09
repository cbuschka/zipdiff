package com.github.cbuschka.zipdiff.diff;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

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

	void contentModified(ZipIndexEntry zipIndexEntry, List<String> oldLines, ZipIndexEntry otherZipIndexEntry, List<String> newLines);

	void contentDeleted(ZipIndexEntry zipIndexEntry, List<String> oldLines, ZipIndexEntry otherZipIndexEntry);

	void contentAdded(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry, List<String> newLines);

	void contentUnchanged(ZipIndexEntry zipIndexEntry, List<String> oldLines, ZipIndexEntry otherZipIndexEntry);

	void endContentModified(ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry);

	void finished();
}
