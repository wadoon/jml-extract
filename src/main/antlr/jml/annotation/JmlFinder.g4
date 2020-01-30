lexer grammar JmlFinder;

@header {
package jml.annotation;
}

JML_S: '//@' ->pushMode(insjml),more;
JML_M: '/*@' ->pushMode(inmjml),more;

SCOMMENT: '//' ->pushMode(scomment), skip;
MCOMMENT: '/*' ->pushMode(mcomment), skip;
STRING: '"' -> pushMode(string), skip;
JAVA: . -> skip;

mode inmjml;
INMJML_CLOSE: '*/' -> type(JML_M), popMode;
INMJML_STRING: '"' -> pushMode(string), more;
INMJML_ANY: .      -> more;

mode insjml;
INSJML_CLOSE:  [\n]-> type(JML_S), popMode;
INSJML_STRING: '"' -> pushMode(string), more;
INSJML_ANY: .      -> more;

mode scomment;
INSCOMMENT_CLOSE: [\n]->skip, popMode;
INSCOMMENT_ANY: . ->skip;

mode mcomment;
INMCOMMENT_CLOSE:'*/'->skip, popMode;
INMCOMMENT_ANY: . ->skip;

mode string;
STRING_CLOSE: ~'\\' '"' -> more, popMode;
STRING_ANY: . -> more;