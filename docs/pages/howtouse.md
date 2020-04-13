# How to use



## Getting Started with the API

See [Packages](https://github.com/wadoon/jml-extract/packages) for information on the 
Maven repository and available packages.

**Note:** identifier of groups, artefacts, packages are not fixed yet.

```java
JmlProject core = JmlCore.create();
core.setEnvironment(classpath, sourcefiles);
List<CompilationUnit> units = core.compileAllIn("../examples");
```
