# Template

## Syntax
Templates take the following form:
```
"Hello {name}" + {name: "World"} => "Hello World"
```
The text between the braces ("{}") is used as a key/getter for a value held in the root object.  
To refer to the root object itself, you can use the `{.}` notation.


Nested values are supported:
```
"Hello {planet|{name}}" + {planet: {name: "World"}} => "Hello World"
```
The left-hand side of the pipe ("|") defines the key/getter of the desired value.  
The right-hand side of the pipe defines a new template to be applied to said value.  

Iterable values are supported:
```
"Planets: {planets|{name}|, }" + {planets: [{name: "Earth"}, {name: "Mars"}, {name: "Pluto"}]} => "Planets: Earth, Mars, Pluto"
```
The left-hand side of the first pipe ("|") defines the key/getter of the desired iterable value.  
The right-hand side of the first pipe defines a new template to be applied to each of the elements.  
The right-hand side of the _second_ pipe is repeated between each element.

## Usage/Code Examples
#### Dependency:
```
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    compile "com.github.dags-:Template:0.1"
}
```

#### Basic rendering:
```
Template template = Template.parse("Hello {name}!");
Context context = Context.of("name", "World");

// prints "Hello World!"
template.apply(context, System.out);
```
_The context object is just a Map<String, Object>_  
_We can use any object for rendering providing the expected getters are accessible._

#### Nested value rendering:
```
Template template = Template.parse("Hello {user|{name}}!");
Context context = Context.of("user", Context.of("name", "Harry"));

// prints "Hello Harry!"
template.apply(context, System.out);
```

#### Getter value rendering:
```
Supplier<String> supplier = () -> "Garry";
Template template = Template.parse("Hello {user|{get}}!");
Context context = Context.of("user", supplier);

// prints "Hello Garry!"
template.apply(context, System.out);
```
_The template reflectively calls 'Suppier.get()' to retrieve the value._

#### Iterable value rendering:
```
Template template = Template.parse("My list: {list|**{.}**|, }");
Context context = Context.of("list", Arrays.asList("one", "two", "three"));

// prints "My list: **one**, **two**, **three**"
template.apply(context, System.out);
```
