package com.github.cbuschka.zipdiff.io;

import java.io.IOException;

public interface StringOut
{
	void write(String s) throws IOException;

	void close() throws IOException;
}
