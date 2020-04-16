// Grammar from the KeY-Project, cleaned and mergeds
parser grammar KeYJMLParser;

classlevel_comment:
    mods=modifiers
    ( list=classlevel_element mods=modifiers)*
    EOF
;

classlevel_element:
        class_invariant
    |   depends_clause
    |   method_specification
    |   field_or_method_declaration
    |   represents_clause
    |   history_constraint
    |   initially_clause
    |   class_axiom
    |   monitors_for_clause
    |   readable_if_clause
    |   writable_if_clause
    |   datagroup_clause
    |   set_statement    //RecodeR workaround
    |   assert_statement //RecodeR workaround
    |   assume_statement //RecodeR workaround
    |   nowarn_pragma
;

methodlevel_comment:
  (mods=modifiers list=methodlevel_element)* EOF
;


methodlevel_element:
        field_or_method_declaration
    |   set_statement
    |   merge_point_statement
    |   loop_specification
    |   assert_statement
    |   assume_statement
    |   nowarn_pragma
    |   debug_statement
    |   block_specification
    |   block_loop_specification
;

//-----------------------------------------------------------------------------
//modifiers
//-----------------------------------------------------------------------------

modifiers: modifier*;

modifier:
        abs=ABSTRACT           
    |   fin=FINAL              
    |   gho=GHOST              
    |   hel=HELPER             
    |   ins=INSTANCE           
    |   mod=MODEL              
    |   nnu=NON_NULL           
    |   nul=NULLABLE           
    |   nld=NULLABLE_BY_DEFAULT
    |   pri=PRIVATE            
    |   pro=PROTECTED          
    |   pub=PUBLIC             
    |   pur=PURE               
    |   stp=STRICTLY_PURE      
    |   spr=SPEC_PROTECTED     
    |   spu=SPEC_PUBLIC        
    |   sta=STATIC             
    |   tst=TWO_STATE          
    |   nst=NO_STATE           
    |   sjm=SPEC_JAVA_MATH     
    |   ssm=SPEC_SAVE_MATH     
    |   sbm=SPEC_BIGINT_MATH   
    |   cjm=CODE_JAVA_MATH     
    |   csm=CODE_SAVE_MATH     
    |   cbm=CODE_BIGINT_MATH   
;



//-----------------------------------------------------------------------------
//class invariants and alike
//-----------------------------------------------------------------------------

class_invariant: invariant_keyword expression;
axiom_name:
    AXIOM_NAME_BEGIN id=IDENT AXIOM_NAME_END;

invariant_keyword: INVARIANT | INVARIANT_RED;
class_axiom: AXIOM expression;
initially_clause: INITIALLY expression;
method_specification:
    (also_keyword)*
    spec_case
    (	(also_keyword)+ list=spec_case)*
;

also_keyword: ALSO |	FOR_EXAMPLE |IMPLIES_THAT;

spec_case: lightweight_spec_case | heavyweight_spec_case;

//-----------------------------------------------------------------------------
//lightweight specification cases
//-----------------------------------------------------------------------------

lightweight_spec_case: generic_spec_case;



//-----------------------------------------------------------------------------
//heavyweight specification cases
//-----------------------------------------------------------------------------

heavyweight_spec_case:
    (s=modifier  )?
    (
	    behavior_spec_case
	    |   break_behavior_spec_case
	    |   continue_behavior_spec_case
	    |   exceptional_behavior_spec_case
	|   normal_behavior_spec_case
	|   model_behavior_spec_case
	|   return_behavior_spec_case
    )
;



normal_behavior_keyword: NORMAL_BEHAVIOR | NORMAL_BEHAVIOUR;
behavior_keyword: BEHAVIOR | BEHAVIOUR;
model_behavior_keyword: MODEL_BEHAVIOR | MODEL_BEHAVIOUR;
exceptional_behavior_keyword: EXCEPTIONAL_BEHAVIOR |	EXCEPTIONAL_BEHAVIOUR;

normal_behavior_spec_case: normal_behavior_keyword generic_spec_case;
model_behavior_spec_case: model_behavior_keyword generic_spec_case;
exceptional_behavior_spec_case: exceptional_behavior_keyword generic_spec_case;
behavior_spec_case: behavior_keyword generic_spec_case;

//-----------------------------------------------------------------------------
//generic specification cases
//-----------------------------------------------------------------------------

generic_spec_case:
    (abbrvs=spec_var_decls)?
    (requires=spec_header (requires_free=free_spec_header)?)
    ( result = generic_spec_body)?
    | result = generic_spec_body
;


spec_var_decls:
    (
            pz=old_clause
            
            |
	    FORALL expression
    )+
;


spec_header:
    (requires_clause)+
;


free_spec_header
	returns [ImmutableList<PositionedString> result
		 = ImmutableSLList.<PositionedString>nil()]
	throws SLTranslationException
:
    (requires_free_clause)+
;


requires_clause:
    requires_keyword expression 
;


requires_keyword
:
    REQUIRES | REQUIRES_RED | PRE | PRE_RED

;


requires_free_clause:
    REQUIRES_FREE expression 
;


generic_spec_body:
    simple_spec_body
    |
    (
      NEST_START
	    generic_spec_case_seq
	    NEST_END
    )
;


generic_spec_case_seq:
    generic_spec_case
    (
        (also_keyword)+
        list=generic_spec_case
    )*
;


simple_spec_body: (simple_spec_body_clause)+;

simple_spec_body_clause:
	    assignable_clause
	|   accessible_clause     
	|   ensures_clause        
	|   ensures_free_clause   
	|   signals_clause        
	|   signals_only_clause   
	|   diverges_clause       
	|   measured_by_clause    
	|   variant_function      
	|   name_clause           
	|   captures_clause
	|   when_clause
	|   working_space_clause
	|   duration_clause
	|   breaks_clause         
	|   continues_clause      
	|   returns_clause        
  |   separates_clause
  |   determines_clause
;



//-----------------------------------------------------------------------------
//simple specification body clauses
//-----------------------------------------------------------------------------


// old information flow annotations
separates_clause:
    separates_keyword expression 
;


separates_keyword
:
        RESPECTS
    |   SEPARATES
;


determines_clause:
    determines_keyword expression 
;


determines_keyword
:
        DETERMINES
;


assignable_clause:
    assignable_keyword expression 
;


assignable_keyword
:
	ASSIGNABLE
    |   ASSIGNABLE_RED
    |   ASSIGNS
    |   ASSIGNS_RED
    |   MODIFIABLE
    |   MODIFIABLE_RED
    |   MODIFIES
    |   MODIFIES_RED
;


accessible_clause:
    accessible_keyword expression 
;


accessible_keyword
:
        ACCESSIBLE
    |   ACCESSIBLE_REDUNDANTLY
;


measured_by_clause:
// TODO: this is confusing. why not keep 'measured_by'?
    measured_by_keyword expression 
;


measured_by_keyword
:
        MEASURED_BY
    |   MEASURED_BY_REDUNDANTLY
;


ensures_clause:
    ensures_keyword expression 
;


ensures_keyword
:
	ENSURES | ENSURES_RED | POST | POST_RED
;


ensures_free_clause:
    ENSURES_FREE expression 
;


signals_clause:
    signals_keyword expression 
;


signals_keyword
:
	SIGNALS
    |   SIGNALS_RED
    |   EXSURES
    |   EXSURES_RED
;


signals_only_clause:
    signals_only_keyword expression 
;


signals_only_keyword
:
	SIGNALS_ONLY
    |   SIGNALS_ONLY_RED
;


diverges_clause:
    diverges_keyword expression
;


diverges_keyword
:
	DIVERGES
    |   DIVERGES_RED
;


captures_clause throws SLTranslationException
:
    captures_keyword expression
    {
	raiseNotSupported("captures clauses");
    }
;


captures_keyword
:
	CAPTURES
    |   CAPTURES_RED
;


name_clause
	returns [PositionedString result = null]
	throws SLTranslationException
:
    spec=SPEC_NAME STRING_LITERAL SEMICOLON
;


when_clause throws SLTranslationException
:
    when_keyword expression
    {
	raiseNotSupported("when clauses");
    }
;


when_keyword
:
	WHEN
    |   WHEN_RED
;


working_space_clause throws SLTranslationException
:
    working_space_keyword expression
    {
	raiseNotSupported("working_space clauses");
    }
;


working_space_keyword
:
	WORKING_SPACE
    |   WORKING_SPACE_RED
;


duration_clause: duration_keyword expression;
duration_keyword: DURATION | DURATION_RED;

old_clause:
	OLD mods=modifiers
	typetype
	IDENT
	init=INITIALISER
;

/*type:
    identToken=IDENT 
    (t=EMPTYBRACKETS )*
;*/

field_or_method_declaration:
    typetype IDENT
    (   methodDecl=method_declaration
      | fieldDecl=field_declaration
    )
;


//-----------------------------------------------------------------------------
//field declarations
//-----------------------------------------------------------------------------

field_declaration:
    (t=EMPTYBRACKETS )*
    (
	    init=initialiser  
	|   semi=SEMICOLON    
    )
;



//-----------------------------------------------------------------------------
//method declarations
//-----------------------------------------------------------------------------

method_declaration:
    params=param_list   
    (
	    body=BODY  	    
	|   semi=SEMICOLON
    )
;

param_list:
        t=LPAREN 
        (
            param=param_decl 
            (
                t=COMMA
                param=param_decl
                
            )*
        )?
        t=RPAREN 
    ;

param_decl
    :
        (
             t=(NON_NULL | NULLABLE)
        )?
        t=IDENT
        (
          ( AXIOM_NAME_BEGIN // That is "["
            AXION_NAME_END // That is "]"
          | EMPTYBRACKETS )
                
        )*
        t=IDENT
    ;


//-----------------------------------------------------------------------------
//represents clauses
//-----------------------------------------------------------------------------


represents_clause:
    represents_keyword expression
;


represents_keyword:
        REPRESENTS
    |   REPRESENTS_RED
;



//-----------------------------------------------------------------------------
//classlevel depends clauses (custom extension of JML)
//-----------------------------------------------------------------------------

depends_clause: accessible_keyword expression;



//-----------------------------------------------------------------------------
//unsupported classlevel stuff
//-----------------------------------------------------------------------------

history_constraint:
    constraint_keyword expression
;


constraint_keyword
:
        CONSTRAINT
    |   CONSTRAINT_RED
;



monitors_for_clause:
    MONITORS_FOR expression
;


readable_if_clause:
    READABLE expression
;


writable_if_clause: WRITABLE expression;


datagroup_clause:
    in_group_clause | maps_into_clause
;


in_group_clause  throws SLTranslationException
:
    in_keyword expression
    {
	raiseNotSupported("in-group clauses");
    }
;


in_keyword
:
	IN
    | 	IN_RED
;


maps_into_clause: maps_keyword expression;
maps_keyword: MAPS | MAPS_RED;

nowarn_pragma: NOWARN expression;
debug_statement: DEBUG expression;


//-----------------------------------------------------------------------------
//set statements
//-----------------------------------------------------------------------------

set_statement: SET expression;

//-----------------------------------------------------------------------------
//merge point statement
//-----------------------------------------------------------------------------

merge_point_statement:
    MERGE_POINT
    (MERGE_PROC   (mpr = STRING_LITERAL))?
    (MERGE_PARAMS (mpa = BODY))?
    SEMICOLON
;



//-----------------------------------------------------------------------------
//loop specifications
//-----------------------------------------------------------------------------

loop_specification
:
    (loop_invariant
    | 	loop_invariant_free       )
    (
            loop_invariant
        |   loop_invariant_free
        |   loop_separates_clause
        |   loop_determines_clause
        |   assignable_clause
        |   variant_function
    )*
;


loop_invariant:
    maintaining_keyword expression ;

loop_invariant_free:
    LOOP_INVARIANT_FREE expression 
;

maintaining_keyword
:
        MAINTAINING
    |   MAINTAINING_REDUNDANTLY
    |   LOOP_INVARIANT
    |   LOOP_INVARIANT_REDUNDANTLY
;


variant_function:
    decreasing_keyword expression 
;


decreasing_keyword
:
        DECREASING
    |   DECREASING_REDUNDANTLY
    |   DECREASES
    |   DECREASES_REDUNDANTLY
    |   LOOP_VARIANT
    |   LOOP_VARIANT_RED
;


// old information flow annotations
loop_separates_clause:
    separates_keyword expression 
;


loop_determines_clause:
    determines_keyword expression 
;



//-----------------------------------------------------------------------------
//unsupported methodlevel stuff
//-----------------------------------------------------------------------------


assume_statement: assume_keyword expression;
assume_keyword: ASSUME | ASSUME_REDUNDANTLY;



//-----------------------------------------------------------------------------
//expressions
//-----------------------------------------------------------------------------

initialiser: EQUALITY expression;


//-----------------------------------------------------------------------------
//block specifications
//-----------------------------------------------------------------------------

block_specification: method_specification;

block_loop_specification:
    (
    loop_contract_keyword spec_case
    (
	(also_keyword)+ loop_contract_keyword list=spec_case)*)
;

loop_contract_keyword : LOOP_CONTRACT;


assert_statement:
      assert_keyword expression
    | UNREACHABLE SEMICOLON
;


assert_keyword:
	ASSERT | ASSERT_REDUNDANTLY
;

breaks_clause: breaks_keyword expression;
breaks_keyword: BREAKS;


continues_clause: continues_keyword expression;
continues_keyword: CONTINUES;


returns_clause: returns_keyword expression;
returns_keyword: RETURNS;


break_behavior_spec_case: break_behavior_keyword generic_spec_case;

break_behavior_keyword:	BREAK_BEHAVIOR | BREAK_BEHAVIOUR;


continue_behavior_spec_case:
    continue_behavior_keyword
    generic_spec_case
;

continue_behavior_keyword:
	CONTINUE_BEHAVIOR | CONTINUE_BEHAVIOUR
;


return_behavior_spec_case:
    return_behavior_keyword
    generic_spec_case;

return_behavior_keyword: RETURN_BEHAVIOR | RETURN_BEHAVIOUR;

top:
    (   accessibleclause 
    |   assignableclause 
    |   breaksclause 
    |   continuesclause 
    |   dependsclause 
    |   ensuresclause 
    |   ensuresfreeclause 
    |   representsclause 
    |   axiomsclause 
    |   requiresclause 
    |   requiresfreeclause 
    |   decreasesclause 
    |   separatesclause  // old information flow syntax
    |   determinesclause  // new information flow syntax
    |   loopseparatesclause  // old information flow syntax
    |   loopdeterminesclause  // new information flow syntax
    |   returnsclause 
    |   signalsclause 
    |   signalsonlyclause 
    |   termexpression 
    |   mergeparamsspec 
    )
    (SEMI)? EOF
    ;

accessibleclause: acc=ACCESSIBLE storeRefUnion;


assignableclause:
    ass=ASSIGNABLE
    ( storeRefUnion
    | STRICTLY_NOTHING
    )
;


dependsclause:
    dep=DEPENDS lhs=expression
    COLON rhs=storeRefUnion
    (MEASURED_BY mby=expression)? SEMI
;

decreasesclause:
  dec=DECREASES termexpression (COMMA t=termexpression)*
;

requiresclause:     req=REQUIRES predornot;

requiresfreeclause: req=REQUIRES_FREE predornot;

ensuresclause:      ens=ENSURES predornot;

ensuresfreeclause:  ens=ENSURES_FREE predornot;

axiomsclause:       axm=MODEL_METHOD_AXIOM termexpression;

representsclause:
    rep=REPRESENTS lhs=expression
    (
      ( (LARROW | EQUAL_SINGLE)
        ( rhs=expression | t=storeRefUnion )
      )
      | SUCH_THAT predicate
    )
;


separatesclause:
    SEPARATES (NOTHING | sep = infflowspeclist)
    (   (DECLASSIFIES (NOTHING | tmp = infflowspeclist )) |
        (ERASES (NOTHING | tmp = infflowspeclist )) |
        (NEW_OBJECTS (NOTHING | tmp = infflowspeclist ))
    )*
;


loopseparatesclause 
:
    LOOP_SEPARATES (NOTHING | tmp = infflowspeclist )
    (   (NEW_OBJECTS (NOTHING | tmp = infflowspeclist ))
    )*
;


determinesclause:
    DETERMINES (NOTHING | det=infflowspeclist)
    BY (NOTHING | ITSELF | by=infflowspeclist)
    (   (DECLASSIFIES (NOTHING | tmp = infflowspeclist)) |
        (ERASES (NOTHING | tmp = infflowspeclist)) |
        (NEW_OBJECTS (NOTHING | tmp = infflowspeclist))
    )*
;

loopdeterminesclause 
:
    LOOP_DETERMINES (NOTHING | det = infflowspeclist)
    BY ITSELF
    (   (NEW_OBJECTS (NOTHING | tmp = infflowspeclist ))
    )*
;


infflowspeclist 
:
    term=termexpression
    (COMMA term=termexpression)*
;


signalsclause 
:
    sig=SIGNALS LPAREN excType=referencetype (id=IDENT )? RPAREN
    (result = predornot)?
;


signalsonlyclause 
:
    sigo=SIGNALS_ONLY
    (   NOTHING
      | rtype = referencetype 
        (COMMA rtype = referencetype )*
    )
    
    ;

mergeparamsspec 
:
    MERGE_PARAMS
    LBRACE
        (latticetype = IDENT)
        COLON
        LPAREN
            (phType = typespec)
            (phName = IDENT)
    RPAREN
    RARROW
        LBRACE
            (abstrPred = predicate)
            (COMMA (abstrPred = predicate))*
        RBRACE
    RBRACE
;

termexpression: exp=expression;

breaksclause:
    breaks=BREAKS LPAREN (id=IDENT )? RPAREN
    (pred = predornot)?
;

continuesclause:
    continues=CONTINUES LPAREN (id=IDENT )? RPAREN
    (pred = predornot)?
;

returnsclause: rtrns=RETURNS (result = predornot)?;
storeRefUnion: list = storeRefList;
storeRefList: t = storeref (COMMA t = storeref  )*;
storeRefIntersect: list = storeRefList;

storeref:
      NOTHING
    | EVERYTHING
    | NOT_SPECIFIED
    | storeRefExpr;


createLocset
:
    (LOCSET | SINGLETON) LPAREN list=exprList RPAREN
    {
        result = translator.translate("create locset", Term.class, list, services);
    }
    ;


exprList
:   expr = expression
    (COMMA expr = expression  )*;


storeRefExpr 
:
    expr=expression
    {
        result = translator.translate("store_ref_expr", Term.class, expr, services);
    }
    ;

predornot:
        predicate
    |   n=NOT_SPECIFIED
    |   SAME
;

predicate: expr=expression;
expression: conditionalexpr;

conditionalexpr:
  equivalenceexpr (QUESTIONMARK a=conditionalexpr COLON b=conditionalexpr)?
;


equivalenceexpr: impliesexpr (eq=EQV_ANTIV right=impliesexpr)*;

/*
 * Note: According to JML Manual 12.6.3 forward implication has to be parsed right-associatively
 * and backward implication left-associatively.
 */
impliesexpr:
    logicalorexpr
    ( IMPLIES impliesforwardexpr | (IMPLIESBACKWARD logicalorexpr)+)?
;

impliesforwardexpr: logicalorexpr (IMPLIES expr=impliesforwardexpr)?;
logicalorexpr:      logicalandexpr (LOGICALOR expr=logicalandexpr)*;
logicalandexpr:     inclusiveorexpr (LOGICALAND expr=inclusiveorexpr)*
;

inclusiveorexpr: exclusiveorexpr (INCLUSIVEOR expr=exclusiveorexpr)*;


exclusiveorexpr: andexpr (XOR expr=andexpr)*;
andexpr: equalityexpr (AND expr=equalityexpr)*;

equalityexpr: relationalexpr (eq=EQ_NEQ right=relationalexpr)*;

relationalexpr:
    shiftexpr
    ( lt=LT right=shiftexpr ( LT right2=shiftexpr )?
    | gt=GT right=shiftexpr
    | leq=LEQ right=shiftexpr
    | geq=GEQ right=shiftexpr
    | llt=LOCKSET_LT sub=postfixexpr
    | lleq=LOCKSET_LEQ sub=postfixexpr
    | io=INSTANCEOF rtype=typespec
    | st=ST right=shiftexpr
    )?
;

shiftexpr:
    additiveexpr
    ( sr=SHIFTRIGHT e=additiveexpr
    | sl=SHIFTLEFT e=additiveexpr
    | usr=UNSIGNEDSHIFTRIGHT e=additiveexpr
    )*
;

additiveexpr:
    multexpr
    (
    plus=PLUS e=multexpr
    |
    minus=MINUS e=multexpr
    )*
;


multexpr 
:
    unaryexpr
    (
    MULT e=unaryexpr
    |
    DIV e=unaryexpr
    |
    MOD e=unaryexpr
    )*
;


unaryexpr:
      PLUS unaryexpr
    | MINUS DECLITERAL
    | MINUS unaryexpr
    | castexpr
    | unaryexprnotplusminus
;

castexpr: LPAREN rtype=typespec RPAREN unaryexpr;

unaryexprnotplusminus:
      NOT e=unaryexpr
    | BITWISENOT e=unaryexpr
    | postfixexpr
;

postfixexpr: primaryexpr (primarysuffix)*;

primaryexpr:
      constant
    | id=IDENT
    | inv=INV
    | TRUE
    | FALSE
    | NULL
    | jmlprimary
    | THIS
    | new_expr
    | array_initializer
;

transactionUpdated:
   tk=TRANSACTIONUPDATED LPAREN expr=expression RPAREN
;

primarysuffix:
  ( DOT
    ( id=IDENT
    | tr=TRANSIENT
    | THIS
    | INV
    | MULT
    )
  | l=LPAREN (params=expressionlist)? RPAREN
  | lbrack=LBRACKET specarrayrefexpr RBRACKET
  )
;


specarrayrefexpr:
    ( rangeFrom=expression (DOTDOT rangeTo=expression)? )
    | MULT
;

new_expr:
    NEW typ=type (LPAREN ( params=expressionlist )? RPAREN
                 | array_dimensions (array_initializer)? )
;
array_dimensions: array_dimension+;

array_dimension:
    LBRACKET (length=expression)? RBRACKET
;

array_initializer 
:
    LBRACE init=expressionlist RBRACE
;

expressionlist:
    expr=expression (COMMA expr=expression)*
;

constant: javaliteral;

javaliteral:
      integerliteral
    | l=STRING_LITERAL
    | c=CHAR_LITERAL
;

integerliteral
:
   HEXLITERAL #hexliteral
 | DECLITERAL #decliteral
 | OCTLITERAL #octliteral
 | BINLITERAL #binliteral
;

jmlprimary:
      RESULT
    | EXCEPTION
    | infinite_union_expr
    | specquantifiedexpression
    | bsumterm
    | seqdefterm
    | oldexpression
    | beforeexpression
    | transactionUpdated
    | BACKUP LPAREN expression RPAREN
    | PERMISSION LPAREN expression RPAREN
    | NONNULLELEMENTS LPAREN expression RPAREN
    | desc=INFORMAL_DESCRIPTION
    | escape=DL_ESCAPE ( LPAREN ( list=expressionlist )? RPAREN )?
    | mapEmpty=MAPEMPTY
    | tk=mapExpression LPAREN ( list=expressionlist )? RPAREN
    | s2m=SEQ2MAP LPAREN ( list=expressionlist )? RPAREN
    | NOT_MODIFIED LPAREN t=storeRefUnion RPAREN
    | na=NOT_ASSIGNED LPAREN t=storeRefUnion RPAREN
    // TODO: add \only_*
    | FRESH LPAREN list=expressionlist RPAREN
    | REACH LPAREN storeref COMMA e1=expression COMMA e2=expression (COMMA e3=expression)? RPAREN
    | REACHLOCS LPAREN storeref COMMA e1=expression (COMMA e3=expression)? RPAREN
    | duration=DURATION LPAREN expression RPAREN
    | space=SPACE LPAREN expression RPAREN
    | wspace=WORKINGSPACE LPAREN expression RPAREN
    | max=MAX LPAREN expression RPAREN
    | TYPEOF LPAREN expression RPAREN
    | ELEMTYPE LPAREN expression RPAREN
    | TYPE_SMALL LPAREN typ=typespec RPAREN
    | lockset=LOCKSET
    | IS_INITIALIZED LPAREN referencetype RPAREN
    | INVARIANT_FOR LPAREN expression RPAREN
    | STATIC_INVARIANT_FOR LPAREN referencetype RPAREN
    | LPAREN lblneg=LBLNEG IDENT expression RPAREN
    | LPAREN lblpos=LBLPOS IDENT expression RPAREN
    | INDEX
    | VALUES
    | STRING_EQUAL LPAREN e1=expression COMMA e2=expression RPAREN
    | EMPTYSET
    | createLocset
    | (UNION | UNION_2) LPAREN t=storeRefUnion RPAREN
    | INTERSECT LPAREN storeRefIntersect RPAREN
    | SETMINUS LPAREN storeref COMMA t2=storeref RPAREN
    | ALLFIELDS LPAREN expression RPAREN
    | ALLOBJECTS LPAREN storeref RPAREN
    | UNIONINF
      LPAREN (nullable=boundvarmodifiers)? declVars=quantifiedvardecls SEMI
      (predicate? SEMI)? storeref RPAREN
    | pd=DISJOINT LPAREN tlist=storeRefList RPAREN
    | SUBSET LPAREN storeref COMMA t2=storeref RPAREN
    | NEWELEMSFRESH LPAREN storeref RPAREN
    | sequence
    | LPAREN expression RPAREN
;

sequence:
        SEQEMPTY
    |   seqdefterm
    |   (SEQSINGLETON | SEQ) LPAREN list=exprList RPAREN
    |   SEQSUB LPAREN e1=expression COMMA e2=expression COMMA e3=expression RPAREN
    |   SEQREVERSE LPAREN e1=expression RPAREN
    |   SEQREPLACE LPAREN e1=expression COMMA e2=expression COMMA e3=expression RPAREN
    |   (tk2= SEQCONCAT)
        LPAREN e1=expression COMMA e2=expression RPAREN
;

mapExpression:
  ( MAP_GET
  | MAP_OVERRIDE
  | MAP_UPDATE
  | MAP_REMOVE
  | IN_DOMAIN
  | DOMAIN_IMPLIES_CREATED
  | MAP_SIZE
  | MAP_SINGLETON
  | IS_FINITE
  )
;

quantifier:
    FORALL
  | EXISTS
  | MIN
  | MAX
  | NUM_OF
  | PRODUCT
  | SUM
;

infinite_union_expr 
  :
    LPAREN
    UNIONINF
    (nullable=boundvarmodifiers)?
    declVars=quantifiedvardecls SEMI
    (t2=predicate SEMI | SEMI )?
    t=storeref
    RPAREN
    ;

specquantifiedexpression:
    LPAREN
    (
      q=quantifier
      (nullable=boundvarmodifiers)?
      declVars=quantifiedvardecls SEMI
      (p=predicate SEMI | SEMI)?
      expr=expression
    )
    RPAREN
;

oldexpression:
      PRE LPAREN expression RPAREN
    | OLD LPAREN expression (COMMA id=IDENT)? RPAREN
;

beforeexpression:
    ( BEFORE LPAREN expression RPAREN )
;

bsumterm:
    LPAREN
    q=BSUM decls=quantifiedvardecls
    SEMI
    (
        a=expression SEMI  b=expression SEMI t=expression
    )
    RPAREN
;

seqdefterm:
    LPAREN
    q=SEQDEF decls=quantifiedvardecls
    SEMI
    (
        a=expression SEMI  b=expression SEMI t=expression
    )
    RPAREN
;

quantifiedvardecls:
    t=typespec v=quantifiedvariabledeclarator
    (COMMA v=quantifiedvariabledeclarator)*
;

boundvarmodifiers: NON_NULL | NULLABLE;
typespec: t=type (dim=dims)?;

dims
:
    (LBRACKET RBRACKET  )+
    ;

type:
      builtintype
    | referencetype
    | TYPE
;

referencetype: IDENT;

builtintype
:
    BYTE
  | SHORT
  | INT
  | LONG
  | BOOLEAN
  | VOID
  | BIGINT
  | REAL
  | LOCSET
  | SEQ
  | FREE
;

name:  id=IDENT (DOT sub+=IDENT)*;
quantifiedvariabledeclarator: id=IDENT (dim=dims)?;
