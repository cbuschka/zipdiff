# Maven Plugin Usage

## Plugin configuration

```xml
<plugin>
    <groupId>com.github.cbuschka.zipdiff</groupId>
    <artifactId>zipdiff-maven-plugin</artifactId>
    <executions>
        <execution>
            <id>zipdiff-deep-verify-artifact</id>
            <goals>
                <goal>verify</goal>
            </goals>
            <phase>verify</phase>
        </execution>
    </executions>
    <configuration>
        <!-- base line for diff, "old" -->
        <old>${project.basedir}/baseline/artifact.jar</old>
        <!-- file to be compared with "old" file, "new" -->
        <new>${project.build.directory}/artifact.jar</new>
        <!-- skip if true -->
        <skip>false</skip>
        <!-- fail the build if unfiltered diffs present and true, else only warning issues -->
        <failIfDiffsPresent>true</failIfDiffsPresent>
        <!-- recurse into nested zips -->
        <recurse>true</recurse>
        <!-- supress all output, if true -->
        <quiet>false</quiet>
        <!-- diff contents for modified entries, if true -->
        <diffContents>true</diffContents>
        <!-- emit unmodified events, if true -->
        <showUnchanged>false</showUnchanged>
        <rules>
            <rule>
                <id>informative-rule-id</id>
                <match>
                    <old>
                        <!--
                            match path from old zip, matches all if omitted,
                            matches if any includes matches and no excludes matches 
                        -->
                        <path>
                            <includes>
                                <include>**/</include>
                            </includes>
                            <excludes/>
                        </path>
                        <!--
                            match content from old zip, matches all if omitted,
                            matches if any includes matches and no excludes matches,
                            regexp, only if type CONTENT_
                        -->
                        <content>
                            <includes>
                                <include>^foo$</include>
                            </includes>
                            <excludes/>
                        </content>
                    </old>
                    <new>
                        <!--
                            match path from new zip, matches all if omitted,
                            matches if any includes matches and no excludes matches 
                        -->
                        <path>
                            <includes>
                                <include>**</include>
                            </includes>
                            <excludes/>
                        </path>
                        <!--
                            match content from new zip, matches all if omitted,
                            matches if any includes matches and no excludes matches,
                            regexp, only if type CONTENT_
                        -->
                        <content>
                            <includes>
                                <include>^foo$</include>
                            </includes>
                            <excludes/>
                        </content>
                    </new>
                    <!-- match diff type, matches if any matches -->
                    <types>
                        <type>UNCHANGED</type>
                        <type>RENAMED</type>
                        <type>ADDED</type>
                        <type>DELETED</type>
                        <type>MODIFIED</type>
                        <type>CONTENT_UNCHANGED</type>
                        <type>CONTENT_MODIFIED</type>
                        <type>CONTENT_ADDED</type>
                        <type>CONTENT_DELETED</type>
                    </types>
                </match>
            </rule>
        </rules>
    </configuration>
</plugin>
```

## Rules

### Ignore added directories

```xml
<rule>
    <id>ignore-added-dirs</id>
    <match>
        <new>
            <path>
                <includes>
                    <include>**</include>
                </includes>
            </path>
        </new>
        <types>
            <type>ADDED</type>
        </types>
    </match>
</rule>
```

### Ignore removed directories

```xml
<rule>
    <id>ignore-removed-dirs</id>
    <match>
        <old>
            <path>
                <includes>
                    <include>**/</include>
                </includes>
            </path>
        </old>
        <types>
            <type>DELETED</type>
        </types>
    </match>
</rule>
```

### Ignore modified MANIFEST.MF files

```xml
<rule>
    <id>ignore-modified-manifests</id>
    <match>
        <old>
            <path>
                <includes>
                    <include>**/META-INF/MANIFEST.MF</include>
                </includes>
            </path>
        </old>
        <types>
            <type>MODIFIED</type>
        </types>
    </match>
</rule>
```
