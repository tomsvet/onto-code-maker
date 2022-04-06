# OntoCodeMaker

2021 - 2022

Project created as The Master's Thesis at Faculty of Information Technology, Brno University of Technology  

Created by Tomáš Svetlík, Bc.

Supervised by [doc. Ing. Radek Burget, Ph.D.](https://www.fit.vut.cz/person/burgetr/.cs)


OntoCodeMaker is a tool for generating programming code from OWL ontology definitions. 
Supported output languages int this version are Java and Python. Generated code consist of class definitions, class serializations, vocabulary class and a class that serves as the entry point to the generated code. 

## Description

### Mapping
The mapping from OWL to Java follows the following table.

| OWL entity    | OWL expression            | Java entity  |
| ------------- | -------------             |------------- |
| Class         | Class A                   | class A      |
|               | Class A (with subclasses extended more than one class)  |interface A; class A implements interface A   |
|               | A subClassOf B            | class A; class B extends A |
|               | A subClassOf B; C subClassOf B|interface A; interface B; class B extends interface A, interface B |
|               | A equivalentClass B       | interface AB;  A implements interface AB; B implements interface AB|
|               | A intersectionOf B and C  |  class A implements B,C    |
|               | A unionOf B and C         | interface/class B and C extends/implements interface A |
|               | A complementOf B          | B extends C; A extends C      |
| Property A    | domain B                  | property A in the class B |
|               | range B                   | class B is type property type |
|               | functional                | cardinality of property A is 1 |
|               | inverse functional        | in the range class of property A is created functional property with range to class of A|
|               | equivalentOf B            | B is property with same domain,range as A |
|               | subPropertyOf B           | B is property with same domain,range as A, all values from A are also in B |
|               | inverseOf B               | in the range class of property A is created property B |

Restrictions, disjoints are only comments.


The mapping from OWL to Python follows the following table.

## Installation
####Building 
OntoCodeMaker source code may be downloaded/cloned from git [repository](https://github.com/tomsvet/onto-code-maker).
OntoCodeMaker may be build from the sources by maven. 


####Running
OntoCodeMaker is distributed as platform-independent runnable jar archive. 
Jar archive is available in the [Releases](https://github.com/tomsvet/onto-code-maker/releases).
Run following command to generate source code.
```bash
java -jar OntoCodeMaker.jar [options...] <input-file> [<input-file> ...]
```
List of available parametres:
```bash
 OntoCodeMaker [options...] <input-file> [<input-file> ...]
  <input-file>                  The input file to read from (one or more)
  -d,--destination <destination>  Define the destination of generated source
                                  code.
                                  Default dir is actual dir.
  -f,--format <format>            Syntax type of the input file. If absent it
                                  will try to guess.
                                  Supported formats: RDF/XML, Turtle, N-Triples, N-Quads, JSON-LD, TriG and TriX.
  -h,--help                       Print help message.
  -l,--language <language>        Define the language of final source code.
                                  Default language is Java.
                                  All supported languages: java,python.
  -p,--package <package>          Define the package of generated source code.
                                  Default package is empty.
```
## Usage
```bash
java -jar onto-code-maker.jar ontology.owl -d /ta -f RDF/XML -l java -p org.example.package
```

Example usage:

## License