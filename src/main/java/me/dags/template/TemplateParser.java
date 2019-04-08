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
import java.util.LinkedList;
import java.util.List;

class TemplateParser {

    static Template parse(CharReader reader) throws IOException {
        List<Template> parts = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        while (reader.next()) {
            char exit = readTemplateOuter(reader, sb);
            if (exit == '{') {
                addText(sb, parts);
                Template template = parseArg(reader);
                if (template == Template.EMPTY) {
                    continue;
                }
                parts.add(template);
            }
        }
        addText(sb, parts);
        return new Chain(parts);
    }

    private static Template parseArg(CharReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        char exit = readTemplateKey(reader, sb);
        if (exit == CharReader.EOF) {
            return Template.EMPTY;
        }
        String name = sb.toString();
        sb.setLength(0);
        if (exit == '}') {
            return new Arg(name);
        }
        exit = readTemplateChild(reader, sb);
        if (exit == CharReader.EOF) {
            return Template.EMPTY;
        }
        String template = sb.toString();
        sb.setLength(0);
        if (exit == '}') {
            return new Delegate(name, parse(new CharReader(template)), Template.EMPTY);
        }
        exit = readTemplateSeparator(reader, sb);
        if (exit == CharReader.EOF) {
            return Template.EMPTY;
        }
        String separator = sb.toString();
        return new Delegate(name, parse(new CharReader(template)), new PlainText(separator));
    }

    private static char readTemplateOuter(CharReader reader, StringBuilder buffer) throws IOException {
        boolean charEscaped = false;
        boolean stringEscaped = false;
        while (reader.next()) {
            char c = reader.character();
            if (charEscaped) {
                if (c == '{' || c == '}' || c == '|') {
                    buffer.append(c);
                } else {
                    buffer.append('\\');
                    buffer.append(c);
                }
                charEscaped = false;
                continue;
            }
            if (stringEscaped) {
                if (c == '`') {
                    stringEscaped = false;
                } else {
                    buffer.append(c);
                }
                continue;
            }
            if (c == '{') {
                return c;
            }
            if (c == '\\') {
                charEscaped = true;
                continue;
            }
            if (c == '`') {
                stringEscaped = true;
                continue;
            }
            buffer.append(c);
        }
        return CharReader.EOF;
    }

    private static char readTemplateKey(CharReader reader, StringBuilder buffer) throws IOException {
        while (reader.next()) {
            char c = reader.character();
            if (c == '|' || c == '}') {
                return c;
            }
            buffer.append(c);
        }
        return CharReader.EOF;
    }

    private static char readTemplateChild(CharReader reader, StringBuilder buffer) throws IOException {
        int depth = 0;
        while (reader.next()) {
            char c = reader.character();
            if (c == '{') {
                depth++;
                buffer.append(c);
                continue;
            }
            if (depth > 0) {
                if (c == '}') {
                    depth--;
                }
                buffer.append(c);
                continue;
            }
            if (c == '}' || c == '|') {
                return c;
            }
            buffer.append(c);
        }
        return CharReader.EOF;
    }

    private static char readTemplateSeparator(CharReader reader, StringBuilder buffer) throws IOException {
        boolean charEscaped = false;
        boolean stringEscaped = false;
        while (reader.next()) {
            char c = reader.character();
            if (charEscaped) {
                if (c == '{' || c == '}' || c == '|') {
                    buffer.append(c);
                } else {
                    buffer.append('\\');
                    buffer.append(c);
                }
                charEscaped = false;
                continue;
            }
            if (stringEscaped) {
                if (c == '`') {
                    stringEscaped = false;
                } else {
                    buffer.append(c);
                }
                continue;
            }
            if (c == '}') {
                return c;
            }
            if (c == '\\') {
                charEscaped = true;
                continue;
            }
            if (c == '`') {
                stringEscaped = true;
                continue;
            }
            buffer.append(c);
        }
        return CharReader.EOF;
    }

    private static void addText(StringBuilder sb, List<Template> parts) {
        if (sb.length() > 0) {
            parts.add(new PlainText(sb.toString()));
            sb.setLength(0);
        }
    }
}
