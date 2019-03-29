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
