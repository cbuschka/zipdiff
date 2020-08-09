package com.github.cbuschka.zipdiff.content_diff;

public enum ContentDiffType
{
	CONTENT_UNCHANGED,
	CONTENT_MODIFIED,
	CONTENT_ADDED,
	CONTENT_DELETED;

	public boolean isChange() {
		return this != CONTENT_UNCHANGED;
	}
}
