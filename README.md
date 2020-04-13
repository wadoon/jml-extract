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

* [x] **Level 0:** JML specification are attached as `Strings` to the corresponding AST nodes. 
      This gives the highest flexibility as JML keeps untouched, but you need to write your own JML parsing.
    * [x] **Level 0+:** Support for adding annotation processors for translation of annotation to JML specification.      
      You can use ```IJmlAnnotationProcessor``` to translate into 
* [ ] **Level 1:** Providing a parse tree of the JML comments. 
    * *Open question: How should we deal with Java blocks inside JML, e.g. Java in Lambda functions.*
* [ ] **Level 2:** Type-inference of the JML parse trees 

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


## State of development (TODOs)

* [ ] proper error handling, 
  * [ ] propagate/accessibility for ECJ errors
  * [ ] deliver errors from JML 
* [ ] Support of `*.jml` files
* [ ] Unit and integration tests