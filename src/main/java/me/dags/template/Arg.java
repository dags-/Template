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
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

public class Arg implements Template {

    private final String name;

    public Arg(String name) {
        this.name = name;
    }

    @Override
    public void apply(Object value, Writer writer) throws IOException {
        Optional<Object> optional = Arg.getValue(value, name);
        if (optional.isPresent()) {
            Object o = optional.get();
            if (o instanceof Template) {
                ((Template) o).apply(value, writer);
            } else {
                writer.write(o.toString());
            }
        }
    }

    @Override
    public String toString() {
        return "Arg{" + name + "}";
    }

    static Optional<Object> getValue(Object o, String name) {
        if (name.equals(".")) {
            return Optional.of(o);
        }

        if (o instanceof Map) {
            Map map = (Map) o;
            return Optional.ofNullable(map.get(name));
        }

        try {
            String getter = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
            Method method = o.getClass().getMethod(getter);
            method.setAccessible(true);
            return Optional.ofNullable(method.invoke(o));
        } catch (Exception e) {
            try {
                Method method = o.getClass().getMethod(name);
                method.setAccessible(true);
                return Optional.ofNullable(method.invoke(o));
            } catch (Exception ex) {
                return Optional.empty();
            }
        }
    }
}
