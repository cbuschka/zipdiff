# ZipDiff [![Build](https://api.travis-ci.com/cbuschka/zipdiff.svg?branch=master)](https://travis-ci.com/github/cbuschka/zipdiff) [![License](https://img.shields.io/github/license/cbuschka/zipdiff.svg)](https://github.com/cbuschka/zipdiff/blob/master/license.txt)

### A command line tool to diff jar/zip files

## Usage
```
mvn clean install

java -jar target/zipdiff.jar <zipfile1> <zipfile2>
```

Example output:
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

## License
Copyright (c) 2017-2020 by [Cornelius Buschka](https://github.com/cbuschka).

[Apache License, Version 2.0](./license.txt).
