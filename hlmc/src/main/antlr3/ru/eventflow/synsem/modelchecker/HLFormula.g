grammar HLFormula;

options {
    language = Java;
    backtrack = true;
    memoize = true;
}

tokens {
    CONJ      = '&';
    DISJ      = '|';
    NEGATION  = '!';
    IMPL      = '->';
    LANGLE    = '<';
    RANGLE    = '>';
    LBRACKET  = '[';
    RBRACKET  = ']';
    INVERSION = '-';
    AT        = '@';
    EXISTS    = 'E';
    BIND      = 'B';
    FORALL    = 'A';
    LPAREN    = '(';
    RPAREN    = ')';
    TRUE      = 'T';
    FALSE     = 'F';
}

@lexer::header {
    package ru.eventflow.synsem.modelchecker;
}

@parser::header {
    package ru.eventflow.synsem.modelchecker;
}

@lexer::members {

    private List<RecognitionException> errors = new ArrayList<RecognitionException>();

    public List<RecognitionException> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    @Override
    public void reportError(RecognitionException e) {
        errors.add(e);
        displayRecognitionError(this.getTokenNames(), e);
    }
}

@parser::members {

    private List<RecognitionException> errors = new ArrayList<RecognitionException>();

    public List<RecognitionException> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    @Override
    public void reportError(RecognitionException e) {
        errors.add(e);
        displayRecognitionError(this.getTokenNames(), e);
    }
}


/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

expression returns [HLFormula value]
    : exp = implicativeExpression { $value = $exp.value; }
    ;

implicativeExpression returns [HLFormula value]
    : e1 = conjunctiveExpression (IMPL e2 = implicativeExpression)* {
        if ($e2.value == null) {
            $value = $e1.value;
        } else {
            $value = HLFormula_neg.newInstance(
                HLFormula_and.newInstance(
                    $e1.value,
                    HLFormula_neg.newInstance($e2.value)
                )
            );
        }
    }
    ;

conjunctiveExpression returns [HLFormula value]
    : e1 = disjunctiveExpression (CONJ e2 = conjunctiveExpression)* {
        if ($e2.value == null) {
            $value = $e1.value;
        } else {
            $value = HLFormula_and.newInstance($e1.value, $e2.value);
        }
    }
    ;

disjunctiveExpression returns [HLFormula value]
    : e1 = singleExpansionExpression (DISJ e2 = disjunctiveExpression)* {
        if ($e2.value == null) {
            $value = $e1.value;
        } else {
            // De Morgan's
            $value = HLFormula_neg.newInstance(HLFormula_and.newInstance(
                HLFormula_neg.newInstance($e1.value), HLFormula_neg.newInstance($e2.value)
            ));
        }
    }
    ;

singleExpansionExpression returns [HLFormula value]
    : exp = atom {
        $value = $exp.value;
    }
    | NEGATION<assoc=right> exp = expression {
        $value = HLFormula_neg.newInstance($exp.value);
    }
    | LANGLE id = IDENTIFIER RANGLE LPAREN exp = expression RPAREN {
        // <id>(exp)
        $value = HLFormula_diamond.newInstance($id.getText(), $exp.value);
    }
    | LBRACKET id = IDENTIFIER RBRACKET LPAREN exp = expression RPAREN {
        // [id](exp)
        $value = HLFormula_neg.newInstance(HLFormula_diamond.newInstance($id.getText(), HLFormula_neg.newInstance($exp.value)));
    }
    | LANGLE id = IDENTIFIER RANGLE INVERSION LPAREN exp = expression RPAREN {
        // <id>-(exp)
        $value = HLFormula_inverseDiamond.newInstance($id.getText(), $exp.value);
    }
    | LBRACKET id = IDENTIFIER RBRACKET INVERSION LPAREN exp = expression RPAREN {
        // [id]-(exp)
        $value = HLFormula_neg.newInstance(HLFormula_inverseDiamond.newInstance($id.getText(), HLFormula_neg.newInstance($exp.value)));
    }
    | EXISTS exp = expression {
        $value = HLFormula_exists.newInstance($exp.value);
    }
    | FORALL exp = expression {
        $value = HLFormula_neg.newInstance(HLFormula_exists.newInstance(HLFormula_neg.newInstance($exp.value)));
    }
    | AT id = IDENTIFIER LPAREN exp = expression RPAREN {
        // @id(exp)
        $value = HLFormula_atSymbol.newInstance($id.getText(), $exp.value);
    }
    | BIND id = IDENTIFIER LPAREN exp = expression RPAREN {
        // B id (exp)
  		$value = HLFormula_bindWVar.newInstance($id.getText(), $exp.value);
    }
    ;

atom returns [HLFormula value]
    : TRUE {
        $value = new HLFormula_true();
    }
    | FALSE {
        $value = new HLFormula_false();
    }
    | id = IDENTIFIER {
        $value = HLFormula_symbol.newInstance($id.getText());
    }
    | LPAREN exp = expression RPAREN  {
        $value = $exp.value; 
    }
    ;


/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

IDENTIFIER
//    : 'a'..'z' ( ( 'a'..'z'|'_'|'0'..'9' )+ )?
    : LETTER ( ( LETTER|'_'|'-'|'0'..'9' )+ )?
    ;
fragment LETTER
    : '\u0410'..'\u044f' // Russian
    | 'a'..'z'
    ;
WS
    : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+    { $channel = HIDDEN; }
    ;
COMMENT
    : '/*' ( options {greedy=false;} : . )* '*/' { $channel = HIDDEN; }
    ;
LINE_COMMENT
    : '//' ~('\n'|'\r')* '\r'? '\n' { $channel = HIDDEN; }
    ;
