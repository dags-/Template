import me.dags.template.Context;
import me.dags.template.Template;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class Examples {

    public static class Person {

        private final String name;

        private Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static void main(String[] a) throws IOException {
        example1();

        example2();

        example3();
    }

    private static void example1() throws IOException {
        Template template = Template.parse("\\{{rank} {person|{name}}\\}: {message}");

        Context context = Context.of("rank", "Admin")
                .with("person", new Person("Harry"))
                .with("message", "Hello world!");

        template.apply(context, System.out);
        System.out.println();
    }

    private static void example2() throws IOException {
        Template template = Template.parse("The People: {people|**{name}**|, }");

        Context context = Context.of("people", Arrays.asList(
                new Person("Harry"),
                new Person("Larry"),
                new Person("Barry"),
                new Person("Garry")
        ));

        template.apply(context, System.out);
        System.out.println();
    }

    private static void example3() throws IOException {
        Template template = Template.parse("Full Names: {people|{key}='{value|{name}}'|, }");

        Context context = Context.of(
                "people",
                Context.of("harry", new Person("Mr Harry"))
                        .with("larry", new Person("Dr Larry"))
                        .with("barry", new Person("Lord Barry"))
                        .with("garry", new Person("Sir Garry"))
        );

        template.apply(context, System.out);
        System.out.println();
    }
}
