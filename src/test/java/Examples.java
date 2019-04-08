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

import me.dags.template.Context;
import me.dags.template.Template;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Examples {

    public static void main(String[] a) throws IOException {
        example1();

        example2();

        example3();
    }

    private static void example1() throws IOException {
        class Person {
            private final String name;

            Person(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        Template template = Template.parse("\\{{rank} {person|{name}}\\}: {message}");

        Context context = Context.of("rank", "Admin")
                .with("person", new Person("Harry"))
                .with("message", "Hello world!");

        template.apply(context, System.out);
        System.out.println();
    }

    private static void example2() throws IOException {
        class Person {
            private final String name;

            Person(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

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
        Template template = Template.parse("Names: {people|**{get}**|, }");
        List<Supplier<String>> list = Arrays.asList(() -> "Garry", () -> "Larry", () -> "Harry");
        Context context = Context.of("people", list);
        template.apply(context, System.out);
        System.out.println();
    }
}
