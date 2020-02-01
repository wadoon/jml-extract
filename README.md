# jml-extract [![Build Status](https://travis-ci.com/wadoon/jml-extract.svg?branch=master)](https://travis-ci.com/wadoon/jml-extract)

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


## Usage


## State of development (TODOs)

* [ ] proper error handling, 
  * [ ] propagate/accessibility for ECJ errors
  * [ ] deliver errors from JML 
* [ ] Support of `*.jml` files
* [ ] 