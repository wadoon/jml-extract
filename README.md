# jml-extract [![Build Status](https://travis-ci.com/wadoon/jml-extract.svg?branch=master)](https://travis-ci.com/wadoon/jml-extract) [![CodeFactor](https://www.codefactor.io/repository/github/wadoon/jml-extract/badge)](https://www.codefactor.io/repository/github/wadoon/jml-extract)

*jml-extract* provides functionality to extract Java Modelling Language (JML) 
specification from Java Source Files. 
The result are a typed AST with JML-annotated nodes.

*Mission statement:* Our goals is to deliver a versatile, simple, lightweight tool that 
enables the experimenting with the JML or building analysis tools that support Java/JML.


This project delivers an API for use inside other verification tools and 
a simple cli interface which dumps the information into JSON files.

By using the Eclipse Java compiler (ECJ), we support **Java 13**.
 
We plan to support following *levels*:

* [x] **Level 0:** JML specification are attached as `Strings` to the AST nodes. 
      This gives the highest flexibility for 
* [ ] **Level 1:** Providing a parse tree of the JML comments. 
* [ ] **Level 2:** Type-inference of the JML parse trees 

## Getting Started with the API

See [Packages](https://github.com/wadoon/jml-extract/packages) for information on the 
Maven repository and available packages.

**Note:** identifier of groups, artefacts, packages are not fixed yet.

```java
JmlProject core = JmlCore.create();
core.setEnvironment(classpath, sourcefiles);
List<CompilationUnit> units = core.compileAllIn("../examples");
```

## Usage - Command Line

Get a version of the command line via: `grade :exe:shadowJar`, which generates the 
executable jar file `exe/build/libs/exe-*all.jar`.

```yaml
Usage: <main class> [-hv] [--expr] [--in] [--no-bodies]
                    [--show-compiler-options] [--stmt] [--version] [--[no-]
                    vm-classpath] [--encoding=<encoding>]
                    [--java-version=<javaVersion>] [-P=KEY=VALUE]...
                    [-c=PATH...]... [-s=PATH...]... [PATH...]
      [PATH...]
  -c, -cp, --classpath=PATH...
                            specify classpath. Entries need to JAR files and
                              folder. You can use ':' for sepearation
      --encoding=<encoding> set the encoding of the source files
      --expr                treat input as an expression
  -h, --help                show this help message
      --in                  read Java source from stdin
      --java-version=<javaVersion>
                            for future, currently not supported by JmlCore
      --no-bodies           do not parse the bodies of the methods
  -P=KEY=VALUE              set ecj compiler options. `--show-compiler-options`
                              shows all options
  -s, --source=PATH...      Source file to compile against. These files are no
                              taken into account for dumping.
      --show-compiler-options
                            shows all underlying compiler options
      --stmt                treat input as statements
  -v, --[no-]verbose
      --version             show program version
      --[no-]vm-classpath
```

## Architecture 

* Simple Layer upon on the Eclipse Java Compiler (`JmlProject.java`)
* `JmlProject` uses a lookup register for finding services.
* Services provides the functionality `jml.services`.
* Services are replaceable, e.g. customizing 

## Process Steps

1. Parsing of a Java file including type informations. done by ECJ.
   * Result: `CompilationUnit`
2. JML Pipeline triggers (`JmlProject::annotateWithJml`):
   1. Recognise JML comments: (`IJmlDetection`).
      * Each comment is analyzed if it is a JML comment
      * Information attached to `Comment` ast node.
      * Wrapper class for access these information `JmlComment`
   2. Assigning JML comments to AST nodes (`IJmlAttacher`)
      * Based upon the type of a JML comment. 
      * Information is put into as an additional property in the ASTNode. Access via `ASTProperties`.
   3. Annotation Processing for translating type annotation into JML specifications 
        (`IJmlAnnotationProcessor`).
      * **LEVEL 0** ends here.
   4. **TODO* Parse the JML comments into `ParserRuleContext` (antlr4's parse tree) (`IJmlAstFactory`).
      * See the grammar definitions `KeYJmlParser.g4`.
   5. Re-annotating the JML comments.
      * JML comments have annotation, a list of identifiers with "+" or "-" as prefix. 
        The annotations should help tools to filter out comments which are not meant for them.
      * In this step, hooks can update the annotation based upon the ast.
      * **LEVEL 1** ends here.
   6.       

## State of development (TODOs)

* [ ] proper error handling, 
  * [ ] propagate/accessibility for ECJ errors
  * [ ] deliver errors from JML 
* [ ] Support of `*.jml` files
* [ ] Unit and integration tests