// Grammar from the KeY-Project, cleaned and mergeds
lexer grammar KeYJMLLexer;

@header {
    //package de.uka.ilkd.key.speclang.jml.translation;
}

@members {}

/* Java keywords */
BOOLEAN : 'boolean';
BYTE : 'byte';
FALSE : 'false';
INSTANCEOF : 'instanceof';
INT : 'int';
LONG : 'long';
NEW : 'new';
NULL : 'null';
SHORT : 'short';
SUPER : 'super';
THIS : 'this';
TRUE : 'true';
VOID : 'void';

/* JML and JML* keywords */
ACCESSIBLE      : 'accessible';
ASSIGNABLE      : 'assignable';
BREAKS          : 'breaks';
CONTINUES       : 'continues';
DECREASES       : 'decreases'; // internal translation for 'measured_by'
DEPENDS         : 'depends';  // internal translation for 'accessible' on model fields
DETERMINES      : 'determines';  //KeY extension, not official JML
ENSURES         : 'ensures';
ENSURES_FREE    : 'ensures_free';
LOOP_DETERMINES : 'loop_determines';  // internal translation for 'determines' in loop invariants
LOOP_SEPARATES  : 'loop_separates';  //KeY extension, deprecated
MODEL_METHOD_AXIOM    : 'model_method_axiom';  //KeY extension, not official JML
MERGE_PARAMS    : 'merge_params';  //KeY extension, not official JML
NON_NULL        : 'non_null';
NULLABLE        : 'nullable';
REPRESENTS      : 'represents';
REQUIRES        : 'requires';
REQUIRES_FREE   : 'requires_free';
RETURNS         : 'returns';  //KeY extension, not official JML
SEPARATES       : 'separates';  //KeY extension, not official JML
SIGNALS         : 'signals';
SIGNALS_ONLY    : 'signals_only';

/* JML keywords prefixed with a backslash */
ALLFIELDS            : '\\all_fields';  //KeY extension, not official JML
ALLOBJECTS           : '\\all_objects';  //KeY extension, not official JML
BACKUP               : '\\backup';  //KeY extension, not official JML
BEFORE               : '\\before';  //KeY extension, not official JML
BIGINT               : '\\bigint';
BSUM                 : '\\bsum';  //KeY extension, not official JML
BY                   : '\\by';  //KeY extension, not official JML
DECLASSIFIES         : '\\declassifies';  //KeY extension, not official JML
DISJOINT             : '\\disjoint';  //KeY extension, not official JML
DOMAIN_IMPLIES_CREATED : '\\domain_implies_created';  //KeY extension, not official JML
DURATION             : '\\duration';
ELEMTYPE             : '\\elemtype';
EMPTYSET             : '\\empty';
ERASES               : '\\erases';  //KeY extension, not official JML
EVERYTHING           : '\\everything';
EXCEPTION            : '\\exception';
EXISTS               : '\\exists';
FORALL               : '\\forall';
FREE                 : '\\free';  //KeY extension, not official JML
FRESH                : '\\fresh';
INDEX                : '\\index';
INDEXOF              : '\\seq_indexOf';  //KeY extension, not official JML
INTERSECT            : '\\intersect';  //KeY extension, not official JML
INTO                 : '\\into';
INV                  : '\\inv';  //KeY extension, not official JML
INVARIANT_FOR        : '\\invariant_for';
IN_DOMAIN            : '\\in_domain';  //KeY extension, not official JML
IS_FINITE            : '\\is_finite';  //KeY extension, not official JML
IS_INITIALIZED       : '\\is_initialized';
ITSELF               : '\\itself';  //KeY extension, not official JML
LBLNEG               : '\\lblneg';
LBLPOS               : '\\lblpos';
LOCKSET              : '\\lockset';
LOCSET               : '\\locset';  //KeY extension, not official JML
MAP                  : '\\map';  //KeY extension, not official JML
MAPEMPTY             : '\\map_empty';  //KeY extension, not official JML
MAP_GET              : '\\map_get';  //KeY extension, not official JML
MAP_OVERRIDE         : '\\map_override';  //KeY extension, not official JML
MAP_REMOVE           : '\\map_remove';  //KeY extension, not official JML
MAP_SINGLETON        : '\\map_singleton';  //KeY extension, not official JML
MAP_SIZE             : '\\map_size';  //KeY extension, not official JML
MAP_UPDATE           :  '\\map_update';  //KeY extension, not official JML
MAX                  : '\\max';
MEASURED_BY          : '\\measured_by';
MIN                  : '\\min';
NEWELEMSFRESH        : '\\new_elems_fresh';  //KeY extension, not official JML
NEW_OBJECTS          : '\\new_objects';  //KeY extension, not official JML
NONNULLELEMENTS      : '\\nonnullelements';
NOTHING              : '\\nothing';
NOT_ASSIGNED         : '\\not_assigned';
NOT_MODIFIED         : '\\not_modified';
NOT_SPECIFIED        : '\\not_specified';
NUM_OF               : '\\num_of';
OLD                  : '\\old';
PERMISSION           : '\\permission';
PRE                  : '\\pre';
PRODUCT              : '\\product';
REACH                : '\\reach';
REACHLOCS            : '\\reachLocs';  //KeY extension, not official JML
REAL                 : '\\real';
RESULT               : '\\result';
SAME                 : '\\same';
SEQ                  : '\\seq';  //KeY extension, not official JML
SEQ2MAP              : '\\seq_2_map';  //KeY extension, not official JML
SEQCONCAT            : '\\seq_concat';  //KeY extension, not official JML
SEQDEF               : '\\seq_def';  //KeY extension, not official JML
SEQEMPTY             : '\\seq_empty';  //KeY extension, not official JML
SEQGET               : '\\seq_get';  //KeY extension, not official JML
SEQREPLACE           : '\\seq_put';  //KeY extension, not official JML
SEQREVERSE           : '\\seq_reverse';  //KeY extension, not official JML
SEQSINGLETON         : '\\seq_singleton';  //KeY extension, not official JML
SEQSUB               : '\\seq_sub';  //KeY extension, not official JML
SETMINUS             : '\\set_minus';  //KeY extension, not official JML
SINGLETON            : '\\singleton';  //KeY extension, not official JML
SPACE                : '\\space';
STATIC_INVARIANT_FOR : '\\static_invariant_for';  //KeY extension, not official JML
STRICTLY_NOTHING     : '\\strictly_nothing';  //KeY extension, not official JML
STRING_EQUAL         : '\\string_equal';  //KeY extension, not official JML
SUBSET               : '\\subset';
SUCH_THAT            : '\\such_that';
SUM                  : '\\sum';
TRANSACTIONUPDATED   : '\\transactionUpdated';  //KeY extension, not official JML
TRANSIENT            : '\\transient';  //KeY extension, not official JML
TYPE                 : '\\TYPE';
TYPEOF               : '\\typeof';
TYPE_SMALL           : '\\type';
UNION                : '\\set_union';  //KeY extension, not official JML
UNIONINF             : '\\infinite_union';  //KeY extension, not official JML
VALUES               : '\\values';
WORKINGSPACE         : '\\working_space';
// ONLY_ACCESSED     : '\\only_accessed'; // too many common lexemes
// ONLY_ASSIGNED     : '\\only_assigned';
// ONLY_CALLED       : '\\only_called';
// ONLY_CAPTURED     : '\\only_captured';

AND : '&';
BITWISENOT : '~';
COLON : ':';
COMMA : ',';
DIV : '/';
DOT : '.';
DOTDOT : '..';
EQUAL_SINGLE : '=';
EQV_ANTIV: '<==>' | '<=!=>';
EQ_NEQ : '==' | '!=';
GEQ : '>=';
IMPLIES : '==>';
IMPLIESBACKWARD : '<==';
INCLUSIVEOR : '|';
LARROW : '<-';
LBRACE : '{';
LEQ : '<=';
LOCKSET_LEQ: '<#=';
LOCKSET_LT: '<#';
LOGICALAND : '&&';
LOGICALOR : '||';
MINUS : '-';
MOD : '%';
MULT : '*';
NOT : '!';
PLUS : '+';
QUESTIONMARK : '?';
RARROW : '->';
RBRACE : '}';
SEMI : ';';
SHIFTLEFT : '<<';
SHIFTRIGHT : '>>';
ST : '<:';
UNSIGNEDSHIFTRIGHT : '>>>';
XOR : '^';
GT : '>';
LT : '<';
LPAREN: '(';
RPAREN: ')';
LBRACKET: '[';
RBRACKET: ']';

fragment
LETTER: 'a'..'z' | 'A'..'Z' | '_' | '$';

fragment
BINDIGIT: '0'..'1';

fragment
OCTDIGIT: '0'..'7';

fragment
NONZERODECDIGIT: '1'..'9';

fragment
DECDIGIT: '0'..'9';

fragment
DIGIT: '0'..'9';

fragment
HEXDIGIT: DIGIT | 'a' .. 'f' | 'A' .. 'F';

fragment
BINPREFIX: '0' ('b'|'B');

fragment
OCTPREFIX: '0';

fragment
HEXPREFIX: '0' ('x'|'X');

fragment
LONGSUFFIX: 'l' | 'L';

BINLITERAL: BINPREFIX BINDIGIT ((BINDIGIT | '_')* BINDIGIT)? LONGSUFFIX?;
OCTLITERAL: OCTPREFIX OCTDIGIT ((OCTDIGIT | '_')* OCTDIGIT)? LONGSUFFIX?;
DECLITERAL: ('0' | (NONZERODECDIGIT ((DECDIGIT | '_')* DECDIGIT)?)) LONGSUFFIX?;
HEXLITERAL: HEXPREFIX ((HEXDIGIT | '_')* HEXDIGIT)? LONGSUFFIX?;

fragment
LETTERORDIGIT: LETTER | DIGIT;

IDENT:  LETTER (LETTERORDIGIT)*
       | '<' LETTERORDIGIT* '>'
;

JML_IDENT: '\\' IDENT;

CHAR_LITERAL:
        '\''
                (~('\''|'\\') |
                 ('\\' ('\'' | '\\' | 'n' | 'r' | 't' | 'b' | 'f' | '"' | OCT_CHAR
                 | 'u' HEXDIGIT HEXDIGIT HEXDIGIT HEXDIGIT )) //add for safety
                 // note: unicode escapes are processed earlier
                )
      '\''
    ;

fragment OCT_CHAR:
        (('0'|'1'|'2'|'3') OCTDIGIT OCTDIGIT) | (OCTDIGIT OCTDIGIT) | OCTDIGIT;

STRING_LITERAL
    : '"' ( ESC | ~('"'|'\\') )* '"'
    ;

fragment
ESC:   '\\' [nrtbf"': ];

WS: (' ' | '\t' | '\n' | '\r' | '\u000C' | '@') -> channel(HIDDEN);
INFORMAL_DESCRIPTION: '(*' ('*' ~')' | ~'*')* '*)';
SL_COMMENT: '//' .* '\n' -> channel(HIDDEN);
DOC_COMMENT: '/**' .* '*/' -> channel(HIDDEN);

fragment PRAGMA: '\\nowarn';
