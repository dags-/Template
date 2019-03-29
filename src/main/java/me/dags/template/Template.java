package me.dags.template;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public interface Template {

    Template EMPTY = (arg, writer) -> {};

    void apply(Object value, Writer writer) throws IOException;

    default void apply(Object value, OutputStream out) throws IOException {
        Writer writer = new OutputStreamWriter(out);
        apply(value, writer);
        writer.flush();
    }

    static Template parse(CharReader reader) throws IOException {
        return TemplateParser.parse(reader);
    }

    static Template parse(String template) throws IOException {
        return parse(new CharReader(template));
    }
}
