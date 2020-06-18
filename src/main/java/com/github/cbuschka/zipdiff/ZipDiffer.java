package com.github.cbuschka.zipdiff;

import java.util.HashSet;
import java.util.Set;

public class ZipDiffer
{
	public ZipDiff diff(ZipIndex a, ZipIndex b)
	{
		ZipDiff zipDiff = new ZipDiff();

		Set<ZipIndexEntry> alreadyProcessedSet = new HashSet<>();

		for (ZipIndexEntry bEntry : b.entries())
		{
			if (alreadyProcessedSet.contains(bEntry))
			{
				continue;
			}

			ZipIndexEntry aEntry = a.getEntryByPath(bEntry.getPath());
			if (bEntry.isFolder())
			{
				if (aEntry == null)
				{
					zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.ADDED, aEntry, bEntry));
					alreadyProcessedSet.add(bEntry);
				}
				else
				{
					zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.UNCHANGED, aEntry, bEntry));
					alreadyProcessedSet.add(bEntry);
					alreadyProcessedSet.add(aEntry);
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
							alreadyProcessedSet.add(bEntry);
						}
						else
						{
							zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.RENAMED, aEntry, bEntry));
							alreadyProcessedSet.add(bEntry);
							alreadyProcessedSet.add(aEntry);
						}
					}
				}
				else
				{
					if (aEntry.getChecksum().compareTo(bEntry.getChecksum()) != 0)
					{
						zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.MODIFIED, aEntry, bEntry));
						alreadyProcessedSet.add(bEntry);
						alreadyProcessedSet.add(aEntry);

						if (aEntry.getZipIndex() != null && bEntry.getZipIndex() != null)
						{
							ZipDiffer subDiffer = new ZipDiffer();
							ZipDiff subDiff = subDiffer.diff(aEntry.getZipIndex(), bEntry.getZipIndex());
							for (ZipDiffEntry subDiffEntry : subDiff.getEntries())
							{
								zipDiff.addEntry(subDiffEntry);
							}
						}
					}
					else
					{
						zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.UNCHANGED, aEntry, bEntry));
						alreadyProcessedSet.add(bEntry);
						alreadyProcessedSet.add(aEntry);
					}
				}
			}
		}

		for (ZipIndexEntry aEntry : a.entries())
		{
			if (alreadyProcessedSet.contains(aEntry))
			{
				continue;
			}

			ZipIndexEntry bEntry = b.getEntryByPath(aEntry.getPath());
			if (aEntry.isFolder())
			{
				if (bEntry == null)
				{
					zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.DELETED, aEntry, bEntry));
					alreadyProcessedSet.add(aEntry);
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
						alreadyProcessedSet.add(bEntry);
						alreadyProcessedSet.add(aEntry);
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
