package me.dags.template;

import java.io.IOException;
import java.io.Reader;

public class CharReader {

    public static final char EOF = (char) -1;

    private final Reader reader;

    private int value = -1;

    public CharReader(String string) {
        this(new java.io.StringReader(string));
    }

    public CharReader(Reader reader) {
        this.reader = reader;
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
        value = -1;
        return c;
    }
}
