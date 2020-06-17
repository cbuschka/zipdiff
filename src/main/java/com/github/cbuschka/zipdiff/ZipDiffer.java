package com.github.cbuschka.zipdiff;

public class ZipDiffer
{
	public ZipDiff diff(ZipIndex a, ZipIndex b)
	{
		ZipDiff zipDiff = new ZipDiff();

		for (ZipIndexEntry bEntry : b.entries())
		{
			ZipIndexEntry aEntry = a.getEntryByPath(bEntry.getPath());
			if (bEntry.isFolder())
			{
				if (aEntry == null)
				{
					zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.ADDED, aEntry, bEntry));
				}
				else
				{
					zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.UNCHANGED, aEntry, bEntry));
				}
			}
			else
			{
				if (aEntry == null)
				{
					if (bEntry.isFile())
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
				}
				else
				{
					if (aEntry.getCrc() != bEntry.getCrc())
					{
						zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.MODIFIED, aEntry, bEntry));
					}
					else
					{
						zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.UNCHANGED, aEntry, bEntry));
					}
				}
			}
		}

		for (ZipIndexEntry aEntry : a.entries())
		{
			ZipIndexEntry bEntry = b.getEntryByPath(aEntry.getPath());
			if (aEntry.isFolder())
			{
				if (bEntry == null)
				{
					zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.DELETED, aEntry, bEntry));
				}
				else
				{
					// already processd
				}
			}
			else
			{
				if (bEntry == null)
				{
					bEntry = b.getEntryByChecksum(aEntry.getChecksum());
					if (bEntry == null)
					{
						zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.DELETED, aEntry, bEntry));
					}
				}
				else
				{
					// already processd
				}
			}
		}

		return zipDiff;
	}
}
