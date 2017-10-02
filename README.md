# scConfigDsl 

`scConfigDsl` is a minimal wrapper around the most important functions of [Typesafe Config](https://github.com/typesafehub/config) for Scala. It provides a very natural DSL to access sections and keys of the configuration.

Simple usage examples:

Creating a new configuration instance

``` scala
val cfg = new Config
```

Getting a sub-section of the configuration:

``` scala
cfg / "foo" / "bar"
```

Getting a specific key (type inferred from variable type)

``` scala
val key : Option[String] = cfg / "foo" / "bar" % "key" 
```
Tyoe inferred from getOrElse call 

``` scala
val intValue = cfg / "foo" / "bar" % "key" getOrElse(0)
```

Getting the names of all direct child elements of a config:

``` scala
val childElements = cfg children
```

