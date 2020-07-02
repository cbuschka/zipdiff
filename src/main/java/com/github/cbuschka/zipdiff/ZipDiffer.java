package com.github.cbuschka.zipdiff;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ZipDiffer
{
	private boolean recurse;

	public ZipDiffer(boolean recurse)
	{
		this.recurse = recurse;
	}

	public void setRecurse(boolean recurse)
	{
		this.recurse = recurse;
	}

	public ZipDiff diff(ZipIndex a, ZipIndex b)
	{
		ZipDiff zipDiff = new ZipDiff();

		Set<ZipIndexEntry> alreadyProcessedSet = new HashSet<>();

		walkBEntries(b, a, zipDiff, alreadyProcessedSet);
		walkAEntriesLeft(a, b, zipDiff, alreadyProcessedSet);

		return zipDiff;
	}

	private void walkAEntriesLeft(ZipIndex a, ZipIndex b, ZipDiff zipDiff, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		for (ZipIndexEntry aEntry : a.entries())
		{
			if (alreadyProcessedSet.contains(aEntry))
			{
				continue;
			}

			ZipIndexEntry bEntry = getUnprocessedZipIndexEntryByPath(b, aEntry.getPath(), alreadyProcessedSet);
			if (aEntry.isFolder())
			{
				processAFolder(aEntry, bEntry, zipDiff, alreadyProcessedSet);
			}
			else
			{
				processAFile(aEntry, bEntry, b, zipDiff, alreadyProcessedSet);
			}
		}
	}

	private void processAFile(ZipIndexEntry aEntry, ZipIndexEntry bEntry, ZipIndex b, ZipDiff zipDiff, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		if (bEntry == null)
		{
			bEntry = getUnprocessedZipIndexEntryByChecksum(b, aEntry.getChecksum(), alreadyProcessedSet);
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

	private void processAFolder(ZipIndexEntry aEntry, ZipIndexEntry bEntry, ZipDiff zipDiff, Set<ZipIndexEntry> alreadyProcessedSet)
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

	private void walkBEntries(ZipIndex b, ZipIndex a, ZipDiff zipDiff, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		for (ZipIndexEntry bEntry : b.entries())
		{
			if (alreadyProcessedSet.contains(bEntry))
			{
				continue;
			}

			ZipIndexEntry aEntry = getUnprocessedZipIndexEntryByPath(a, bEntry.getPath(), alreadyProcessedSet);
			if (bEntry.isFolder())
			{
				processBFolder(bEntry, aEntry, zipDiff, alreadyProcessedSet);
			}
			else
			{
				processBFile(bEntry, aEntry, a, zipDiff, alreadyProcessedSet);
			}
		}
	}

	private void processBFile(ZipIndexEntry bEntry, ZipIndexEntry aEntry, ZipIndex a, ZipDiff zipDiff, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		if (aEntry == null)
		{
			aEntry = getUnprocessedZipIndexEntryByChecksum(a, bEntry.getChecksum(), alreadyProcessedSet);

			if (alreadyProcessedSet.contains(aEntry))
			{
				aEntry = null;
			}

			if (aEntry == null)
			{
				onBFileAdded(bEntry, zipDiff, alreadyProcessedSet);
			}
			else
			{
				onBFileRenamed(bEntry, aEntry, zipDiff, alreadyProcessedSet);
			}
		}
		else
		{
			if (aEntry.getChecksum().compareTo(bEntry.getChecksum()) != 0)
			{
				if (this.recurse && aEntry.getZipIndex() != null && bEntry.getZipIndex() != null)
				{
					diffModifiedBFile(bEntry, aEntry, zipDiff);
				}
				else
				{
					onBFileModified(bEntry, aEntry, zipDiff, alreadyProcessedSet);
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

	private ZipIndexEntry getUnprocessedZipIndexEntryByPath(ZipIndex zipIndex, String path, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		ZipIndexEntry entryByPath = zipIndex.getEntryByPath(path);
		if (alreadyProcessedSet.contains(entryByPath))
		{
			return null;
		}

		return entryByPath;
	}

	private ZipIndexEntry getUnprocessedZipIndexEntryByChecksum(ZipIndex zipIndex, BigInteger checksum, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		List<ZipIndexEntry> entriesByChecksum = zipIndex.getEntriesByChecksum(checksum);
		for (ZipIndexEntry entryByChecksum : entriesByChecksum)
		{
			if (!alreadyProcessedSet.contains(entryByChecksum))
			{
				return entryByChecksum;
			}
		}

		return null;
	}

	private void diffModifiedBFile(ZipIndexEntry bEntry, ZipIndexEntry aEntry, ZipDiff zipDiff)
	{
		ZipDiff subDiff = diff(aEntry.getZipIndex(), bEntry.getZipIndex());
		for (ZipDiffEntry subDiffEntry : subDiff.getEntries())
		{
			zipDiff.addEntry(subDiffEntry);
		}
	}

	private void onBFileModified(ZipIndexEntry bEntry, ZipIndexEntry aEntry, ZipDiff
			zipDiff, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.MODIFIED, aEntry, bEntry));
		alreadyProcessedSet.add(bEntry);
		alreadyProcessedSet.add(aEntry);
	}

	private void onBFileRenamed(ZipIndexEntry bEntry, ZipIndexEntry aEntry, ZipDiff
			zipDiff, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.RENAMED, aEntry, bEntry));
		alreadyProcessedSet.add(bEntry);
		alreadyProcessedSet.add(aEntry);
	}

	private void onBFileAdded(ZipIndexEntry bEntry, ZipDiff zipDiff, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.ADDED, null, bEntry));
		alreadyProcessedSet.add(bEntry);
	}

	private void processBFolder(ZipIndexEntry bEntry, ZipIndexEntry aEntry, ZipDiff
			zipDiff, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		if (aEntry == null)
		{
			onBFolderAdded(bEntry, aEntry, zipDiff, alreadyProcessedSet);
		}
		else
		{
			onBFolderUnchanged(bEntry, aEntry, zipDiff, alreadyProcessedSet);
		}
	}

	private void onBFolderUnchanged(ZipIndexEntry bEntry, ZipIndexEntry aEntry, ZipDiff
			zipDiff, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.UNCHANGED, aEntry, bEntry));
		alreadyProcessedSet.add(bEntry);
		alreadyProcessedSet.add(aEntry);
	}

	private void onBFolderAdded(ZipIndexEntry bEntry, ZipIndexEntry aEntry, ZipDiff
			zipDiff, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		zipDiff.addEntry(new ZipDiffEntry(ZipDiffEntryType.ADDED, aEntry, bEntry));
		alreadyProcessedSet.add(bEntry);
	}
}
