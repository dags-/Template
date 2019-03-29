package me.dags.template;

import java.io.IOException;
import java.io.Writer;

public class PlainText implements Template {

    private final String text;

    public PlainText(String text) {
        this.text = text;
    }

    @Override
    public void apply(Object value, Writer writer) throws IOException {
        writer.write(text);
    }

    @Override
    public String toString() {
        return "Text{" + text + "}";
    }
}
