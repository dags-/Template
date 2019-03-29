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
