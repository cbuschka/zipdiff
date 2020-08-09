package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntryType;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.util.Set;

public class ZipIndexDiffEntryMatch
{
	private ZipIndexEntryMatch old = new ZipIndexEntryMatch();

	private ZipIndexEntryMatch new_ = new ZipIndexEntryMatch();

	private Set<ZipIndexDiffEntryType> types;

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

	public boolean matches(ZipIndexDiffEntryType zipIndexDiffEntryType, ZipIndexEntry zipIndexEntry, ZipIndexEntry otherZipIndexEntry)
	{
		if (this.types != null && !this.types.isEmpty() && !this.types.contains(zipIndexDiffEntryType))
		{
			return false;
		}

		if (this.old != null && zipIndexEntry != null && !this.old.matches(zipIndexEntry))
		{
			return false;
		}

		if (this.new_ != null && otherZipIndexEntry != null && !this.new_.matches(otherZipIndexEntry))
		{
			return false;
		}

		return true;
	}

	public void setTypes(Set<ZipIndexDiffEntryType> types)
	{
		this.types = types;
	}

	public Set<ZipIndexDiffEntryType> getTypes()
	{
		return types;
	}
}
