# Command Line Tool Usage
```
curl -sL https://raw.githubusercontent.com/cbuschka/zipdiff/master/zipdiff.sh | bash -s src/test/resources/a.zip src/test/resources/b.zip
```

or locally
```
./zipdiff.sh src/test/resources/a.zip src/test/resources/b.zip
```

## Example output:
```
KEPT: META-INF/
CHANGED: META-INF/MANIFEST.MF
KEPT: META-INF/maven/
KEPT: META-INF/maven/com.github.cbuschka.zipdiff/
KEPT: META-INF/maven/com.github.cbuschka.zipdiff/zipdiff/
KEPT: META-INF/maven/com.github.cbuschka.zipdiff/zipdiff/pom.properties
CHANGED: META-INF/maven/com.github.cbuschka.zipdiff/zipdiff/pom.xml
KEPT: com/
KEPT: com/github/
KEPT: com/github/cbuschka/
KEPT: com/github/cbuschka/zipdiff/
KEPT: com/github/cbuschka/zipdiff/ZipDiff.class
KEPT: com/github/cbuschka/zipdiff/ZipDiffEntry.class
KEPT: com/github/cbuschka/zipdiff/ZipDiffEntryType.class
KEPT: com/github/cbuschka/zipdiff/ChecksumCalculator.class
KEPT: com/github/cbuschka/zipdiff/UnclosableInputStream.class
KEPT: com/github/cbuschka/zipdiff/Main.class
KEPT: com/github/cbuschka/zipdiff/ZipIndexReader.class
KEPT: com/github/cbuschka/zipdiff/ZipDiffWriter$1.class
KEPT: com/github/cbuschka/zipdiff/ZipDiffer.class
KEPT: com/github/cbuschka/zipdiff/ZipDiffTool.class
KEPT: com/github/cbuschka/zipdiff/ZipDiffWriter.class
RENAMED: com/github/cbuschka/zipdiff/ZipIndexEntry.class com/github/cbuschka/zipdiff/ZipIndexEntryRenamed.class
REMOVED: com/github/cbuschka/zipdiff/ZipIndex.class
```
