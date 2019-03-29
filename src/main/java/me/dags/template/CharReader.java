package me.dags.template;

import java.io.IOException;
import java.io.Reader;

public class CharReader {

    public static final char EOF = (char) -1;

    private final Reader reader;
    private final StringBuilder raw;

    private int value = -1;

    public CharReader(String string) {
        this(new java.io.StringReader(string), string.length());
    }

    public CharReader(Reader reader, int bufferSize) {
        this.reader = reader;
        this.raw = new StringBuilder(bufferSize);
    }

    public StringBuilder raw() {
        return raw;
    }

    public boolean next() throws IOException {
        if (value == -1) {
            int i = reader.read();
            if (i == -1) {
                return false;
            }
            value = i;
        }
        return true;
    }

    public char character() {
        char c = (char) value;
        raw.append(c);
        value = -1;
        return c;
    }
}
