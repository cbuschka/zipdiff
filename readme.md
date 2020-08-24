# ZipDiff [![Build](https://api.travis-ci.com/cbuschka/zipdiff.svg?branch=master)](https://travis-ci.com/github/cbuschka/zipdiff) [![Maven Central](https://img.shields.io/maven-central/v/com.github.cbuschka.zipdiff/zipdiff-maven-plugin.svg)](https://search.maven.org/search?q=g:com.github.cbuschka.zipdiff%20AND%20a:zipdiff-maven-plugin) [![License](https://img.shields.io/github/license/cbuschka/zipdiff.svg)](https://github.com/cbuschka/zipdiff/blob/master/license.txt)

### A lib, cli tool and maven plugin to diff jar/zip/ear/war/... files

## Usage

### Maven Plugin

```xml
<plugin>
    <groupId>com.github.cbuschka.zipdiff</groupId>
    <artifactId>zipdiff-maven-plugin</artifactId>
    <version>2.0.0</version>
</plugin>
```

[Maven Plugin Configuration](./doc/maven-plugin-usage.md)

### CLI Tool

[Command Line Tool Usage](./doc/cli-tool-usage.md)

### Java Lib

[Java Lib Usage](./doc/java-lib-usage.md)

## Build
### Required prerequisites:
* maven
* java >= 8

```
mvn clean install
```

## License
Copyright (c) 2017-2020 by [Cornelius Buschka](https://github.com/cbuschka).

[Apache License, Version 2.0](./license.txt).
