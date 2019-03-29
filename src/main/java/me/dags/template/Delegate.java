package me.dags.template;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class Delegate implements Template {

    private final String name;
    private final Template template;
    private final Template separator;

    public Delegate(String name, Template template, Template separator) {
        this.name = name;
        this.template = template;
        this.separator = separator;
    }

    @Override
    public void apply(Object value, Writer writer) throws IOException {
        Optional<Object> optional = Arg.getValue(value, name);
        if (!optional.isPresent()) {
            return;
        }

        Object o = optional.get();
        if (separator == Template.EMPTY) {
            write(writer, o, false);
            return;
        }

        if (o instanceof Iterable) {
            Iterator iterator = ((Iterable) o).iterator();
            while (iterator.hasNext()) {
                Object e = iterator.next();
                boolean next = iterator.hasNext();
                write(writer, e, next);
            }
        } else if (o instanceof Map) {
            Iterator iterator = ((Map) o).entrySet().iterator();
            while (iterator.hasNext()) {
                Object e = iterator.next();
                boolean next = iterator.hasNext();
                write(writer, e, next);
            }
        } else if (o.getClass().isArray()) {
            int length = Array.getLength(o);
            for (int i = 0; i < length; i++) {
                Object e = Array.get(o, i);
                boolean next = i + 1 < length;
                write(writer, e, next);
            }
        } else {
            write(writer, o, false);
        }
    }

    @Override
    public String toString() {
        if (separator == Template.EMPTY) {
            return "Delegate{" + name + "@" + template + "}";
        }
        return "Delegate{" + name + "@" + template + "," + separator + "}";
    }

    private void write(Writer writer, Object value, boolean next) throws IOException {
        template.apply(value, writer);
        if (next) {
            separator.apply(value, writer);
        }
    }
}
