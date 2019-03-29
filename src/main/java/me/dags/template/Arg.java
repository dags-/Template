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
