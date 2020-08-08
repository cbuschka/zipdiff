package com.github.cbuschka.zipdiff.filter;

import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntry;
import com.github.cbuschka.zipdiff.diff.ZipIndexDiffEntryType;

import java.util.Set;

public class ZipIndexDiffEntryMatch
{
	private ZipIndexEntryMatch old;

	private ZipIndexEntryMatch new_;

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

	public boolean matches(ZipIndexDiffEntry zipIndexDiffEntry)
	{
		if (this.old != null && !this.old.matches(zipIndexDiffEntry.getZipIndexEntry()))
		{
			return false;
		}

		if (this.new_ != null && !this.new_.matches(zipIndexDiffEntry.getOtherZipIndexEntry()))
		{
			return false;
		}

		if (this.types != null && !this.types.contains(zipIndexDiffEntry.getType()))
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
