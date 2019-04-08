/*
 * MIT License
 *
 * Copyright (c) 2019 dags
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
