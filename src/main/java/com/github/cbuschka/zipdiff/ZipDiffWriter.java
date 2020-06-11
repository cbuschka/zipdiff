package com.github.cbuschka.zipdiff;

import java.io.Closeable;
import java.io.IOException;

public interface ZipDiffWriter extends Closeable
{
	void write(ZipDiff diff) throws IOException;
}
