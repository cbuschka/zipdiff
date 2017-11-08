package com.github.cbuschka.zipdiff;

public class ZipDiffer
{
	public ZipDiff diff(ZipIndex a, ZipIndex b)
	{
		ZipDiff zipDiff = new ZipDiff();

		for (ZipIndexEntry bEntry : b.entries())
		{
			ZipIndexEntry aEntry = a.getEntryByPath(bEntry.getPath());
			if (aEntry == null)
			{
				aEntry = a.getEntryByChecksum(bEntry.getChecksum());
				if (aEntry == null)
				{
					zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.ADDED, null, bEntry));
				}
				else
				{
					zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.RENAMED, aEntry, bEntry));
				}
			}
			else
			{
				if (aEntry.getCrc() != bEntry.getCrc())
				{
					zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.CHANGED, aEntry, bEntry));
				}
				else
				{
					zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.KEPT, aEntry, bEntry));
				}
			}
		}

		for (ZipIndexEntry aEntry : a.entries())
		{
			ZipIndexEntry bEntry = b.getEntryByPath(aEntry.getPath());
			if (bEntry == null)
			{
				bEntry = b.getEntryByChecksum(aEntry.getChecksum());
				if (bEntry == null)
				{
					zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.REMOVED, aEntry, bEntry));
				}
			}
		}

		return zipDiff;
	}
}
