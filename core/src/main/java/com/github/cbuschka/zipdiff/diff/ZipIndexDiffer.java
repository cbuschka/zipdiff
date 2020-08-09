package com.github.cbuschka.zipdiff.diff;

import com.github.cbuschka.zipdiff.content_diff.ContentDiff;
import com.github.cbuschka.zipdiff.content_diff.ContentDiffer;
import com.github.cbuschka.zipdiff.content_diff.ContentDifferProvider;
import com.github.cbuschka.zipdiff.index.ZipIndex;
import com.github.cbuschka.zipdiff.index.ZipIndexEntry;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ZipIndexDiffer
{
	private boolean recurse;

	public ZipIndexDiffer(boolean recurse)
	{
		this.recurse = recurse;
	}

	public void setRecurse(boolean recurse)
	{
		this.recurse = recurse;
	}

	public ZipIndexDiff diff(ZipIndex a, ZipIndex b)
	{
		ZipIndexDiff zipIndexDiff = new ZipIndexDiff(a, b);

		Set<ZipIndexEntry> alreadyProcessedSet = new HashSet<>();

		walkBEntries(b, a, zipIndexDiff, alreadyProcessedSet);
		walkAEntriesLeft(a, b, zipIndexDiff, alreadyProcessedSet);

		return zipIndexDiff;
	}

	private void walkAEntriesLeft(ZipIndex a, ZipIndex b, ZipIndexDiff zipIndexDiff, Set<ZipIndexEntry> alreadyProcessedSet)
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
				processAFolder(aEntry, bEntry, zipIndexDiff, alreadyProcessedSet);
			}
			else
			{
				processAFile(aEntry, bEntry, b, zipIndexDiff, alreadyProcessedSet);
			}
		}
	}

	private void processAFile(ZipIndexEntry aEntry, ZipIndexEntry bEntry, ZipIndex b, ZipIndexDiff zipIndexDiff, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		if (bEntry == null)
		{
			bEntry = getUnprocessedZipIndexEntryByChecksum(b, aEntry.getChecksum(), alreadyProcessedSet);
			if (bEntry == null)
			{
				zipIndexDiff.addEntry(new ZipIndexDiffEntry(ZipIndexDiffEntryType.DELETED, aEntry, bEntry));
				alreadyProcessedSet.add(bEntry);
				alreadyProcessedSet.add(aEntry);
			}
		}
		else
		{
			// already processd
		}
	}

	private void processAFolder(ZipIndexEntry aEntry, ZipIndexEntry bEntry, ZipIndexDiff zipIndexDiff, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		if (bEntry == null)
		{
			zipIndexDiff.addEntry(new ZipIndexDiffEntry(ZipIndexDiffEntryType.DELETED, aEntry, bEntry));
			alreadyProcessedSet.add(aEntry);
		}
		else
		{
			// already processd
		}
	}

	private void walkBEntries(ZipIndex b, ZipIndex a, ZipIndexDiff zipIndexDiff, Set<ZipIndexEntry> alreadyProcessedSet)
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
				processBFolder(bEntry, aEntry, zipIndexDiff, alreadyProcessedSet);
			}
			else
			{
				processBFile(bEntry, aEntry, a, zipIndexDiff, alreadyProcessedSet);
			}
		}
	}

	private void processBFile(ZipIndexEntry bEntry, ZipIndexEntry aEntry, ZipIndex a, ZipIndexDiff zipIndexDiff, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		if (aEntry == null)
		{
			aEntry = getUnprocessedZipIndexEntryByChecksum(a, bEntry.getChecksum(), alreadyProcessedSet);
			if (aEntry == null)
			{
				onBFileAdded(bEntry, zipIndexDiff, alreadyProcessedSet);
			}
			else
			{
				onBFileRenamed(bEntry, aEntry, zipIndexDiff, alreadyProcessedSet);
			}
		}
		else
		{
			if (aEntry.getChecksum().compareTo(bEntry.getChecksum()) != 0)
			{
				if (this.recurse && aEntry.getZipIndex() != null && bEntry.getZipIndex() != null)
				{
					diffModifiedBFile(bEntry, aEntry, zipIndexDiff);
				}
				else
				{
					onBFileModifiedByChecksum(bEntry, aEntry, zipIndexDiff, alreadyProcessedSet);
				}
			}
			else
			{
				zipIndexDiff.addEntry(new ZipIndexDiffEntry(ZipIndexDiffEntryType.UNCHANGED, aEntry, bEntry));
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

	private void diffModifiedBFile(ZipIndexEntry bEntry, ZipIndexEntry aEntry, ZipIndexDiff zipIndexDiff)
	{
		ZipIndexDiff subDiff = diff(aEntry.getZipIndex(), bEntry.getZipIndex());
		for (ZipIndexDiffEntry subDiffEntry : subDiff.getEntries())
		{
			zipIndexDiff.addEntry(subDiffEntry);
		}
	}

	private void onBFileModifiedByChecksum(ZipIndexEntry bEntry, ZipIndexEntry aEntry, ZipIndexDiff
			zipIndexDiff, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		Optional<ContentDiffer> optContentDiffer = ContentDifferProvider.getContentDifferFor(aEntry, bEntry);
		if (optContentDiffer.isPresent())
		{
			ContentDiffer contentDiffer = optContentDiffer.get();
			ContentDiff contentDiff = contentDiffer.diff(aEntry, bEntry);
			if (contentDiff.hasChanges())
			{
				zipIndexDiff.addEntry(new ZipIndexDiffEntry(ZipIndexDiffEntryType.MODIFIED, aEntry, bEntry));
			}
			else
			{
				zipIndexDiff.addEntry(new ZipIndexDiffEntry(ZipIndexDiffEntryType.UNCHANGED, aEntry, bEntry));
			}
		}
		else
		{
			zipIndexDiff.addEntry(new ZipIndexDiffEntry(ZipIndexDiffEntryType.MODIFIED, aEntry, bEntry));
		}
		alreadyProcessedSet.add(bEntry);
		alreadyProcessedSet.add(aEntry);
	}

	private void onBFileRenamed(ZipIndexEntry bEntry, ZipIndexEntry aEntry, ZipIndexDiff
			zipIndexDiff, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		zipIndexDiff.addEntry(new ZipIndexDiffEntry(ZipIndexDiffEntryType.RENAMED, aEntry, bEntry));
		alreadyProcessedSet.add(bEntry);
		alreadyProcessedSet.add(aEntry);
	}

	private void onBFileAdded(ZipIndexEntry bEntry, ZipIndexDiff zipIndexDiff, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		zipIndexDiff.addEntry(new ZipIndexDiffEntry(ZipIndexDiffEntryType.ADDED, null, bEntry));
		alreadyProcessedSet.add(bEntry);
	}

	private void processBFolder(ZipIndexEntry bEntry, ZipIndexEntry aEntry, ZipIndexDiff
			zipIndexDiff, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		if (aEntry == null)
		{
			onBFolderAdded(bEntry, aEntry, zipIndexDiff, alreadyProcessedSet);
		}
		else
		{
			onBFolderUnchanged(bEntry, aEntry, zipIndexDiff, alreadyProcessedSet);
		}
	}

	private void onBFolderUnchanged(ZipIndexEntry bEntry, ZipIndexEntry aEntry, ZipIndexDiff
			zipIndexDiff, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		zipIndexDiff.addEntry(new ZipIndexDiffEntry(ZipIndexDiffEntryType.UNCHANGED, aEntry, bEntry));
		alreadyProcessedSet.add(bEntry);
		alreadyProcessedSet.add(aEntry);
	}

	private void onBFolderAdded(ZipIndexEntry bEntry, ZipIndexEntry aEntry, ZipIndexDiff
			zipIndexDiff, Set<ZipIndexEntry> alreadyProcessedSet)
	{
		zipIndexDiff.addEntry(new ZipIndexDiffEntry(ZipIndexDiffEntryType.ADDED, aEntry, bEntry));
		alreadyProcessedSet.add(bEntry);
	}
}
