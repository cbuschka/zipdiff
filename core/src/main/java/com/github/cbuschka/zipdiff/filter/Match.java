package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.util.List;
import java.util.Set;

public class Match
{
	private ZipIndexEntryMatch old = new ZipIndexEntryMatch();

	private ZipIndexEntryMatch new_ = new ZipIndexEntryMatch();

	private Set<DiffType> types;

	public void setOld(ZipIndexEntryMatch old)
	{
		this.old = old;
	}

	public void setNew(ZipIndexEntryMatch new_)
	{
		this.new_ = new_;
	}

	public ZipIndexEntryMatch getNew()
	{
		return new_;
	}

	public ZipIndexEntryMatch getOld()
	{
		return old;
	}

	public boolean matches(DiffType diffType, ZipIndexEntry zipIndexEntry, List<String> oldLines,
						   ZipIndexEntry otherZipIndexEntry, List<String> newLines)
	{
		if (this.types != null && !this.types.isEmpty() && !this.types.contains(diffType))
		{
			return false;
		}

		if (this.old != null && zipIndexEntry != null && !this.old.matches(zipIndexEntry, oldLines))
		{
			return false;
		}

		if (this.new_ != null && otherZipIndexEntry != null && !this.new_.matches(otherZipIndexEntry, newLines))
		{
			return false;
		}

		return true;
	}

	public void setTypes(Set<DiffType> types)
	{
		this.types = types;
	}

	public Set<DiffType> getTypes()
	{
		return types;
	}
}
