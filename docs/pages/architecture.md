# Architecture

## Design descision

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
   4. **TODO** Parse the JML comments into `ParserRuleContext` (antlr4's parse tree) (`IJmlAstFactory`).
      * See the grammar definitions `KeYJmlParser.g4`.
   5. Re-annotating the JML comments.
      * JML comments have annotation, a list of identifiers with "+" or "-" as prefix. 
        The annotations should help tools to filter out comments which are not meant for them.
      * In this step, hooks can update the annotation based upon the ast.
      * **LEVEL 1** ends here.
   6.  **TODO** We support a type inference for JML expression. 
      * The idea is to rewrite JML into Java and using the Java compiler.
      * We need to translate certain aspect also in to this synthetic Java, e.g. 
      * Requires rewriting of JML identifiers, and also support for 
        JML constants, sorts, functions, predicates.
