package me.dags.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

public class Chain implements Template {

    private final List<Template> template;

    public Chain(List<Template> template) {
        this.template = Collections.unmodifiableList(template);
    }

    @Override
    public void apply(Object value, Writer writer) throws IOException {
        for (Template part : template) {
            part.apply(value, writer);
        }
    }

    @Override
    public String toString() {
        return template.toString();
    }
}
