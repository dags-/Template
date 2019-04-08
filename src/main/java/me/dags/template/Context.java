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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Context extends HashMap<String, Object> {

    private Context(Map<String, Object> source) {
        super(source);
    }

    public Context with(Map<?, ?> map) {
        for (Map.Entry e : map.entrySet()) {
            with(e.getKey().toString(), e.getValue());
        }
        return this;
    }

    public Context with(String name, Object value) {
        put(name, value);
        return this;
    }

    public static Context create() {
        return new Context(Collections.emptyMap());
    }

    public static Context of(String name, Object value) {
        return create().with(name, value);
    }

    public static Context of(Map<String, Object> other) {
        return new Context(other);
    }
}
