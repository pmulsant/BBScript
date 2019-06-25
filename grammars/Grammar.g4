grammar Grammar;

prog : stat*;
stat : simpleStat # IsSimpleStat
    | ifStat # IsIfStat
    | whileStat # IsWhileStat
    | throwStat # IsThrowStat
    | returnStat # IsReturnStat
    | breakStat # IsBreakStat
    | continueStat # IsContinueStat;

simpleStat : expr ';';

expr : SUB ? INT # IntExpr
    | SUB ? FLOAT # FloatExpr
    | CHAR # CharExpr
    | BOOLEAN # BooleanExpr
    | STRING # StringExpr
    | REGEX # RegularExpr
    | 'null' # NullExpr
    | PROVIDED_VARIABLE_INDICATOR? complexId # Identifier

    | 'new' type '(' args ')' # Instantiation
    | 'new' type '(' args ')' runnableScope # InstantiationCallAndDef
    | expr link=(WEAK_LINK_OP | STRONG_LINK_OP) complexId # AttributeCall
    | expr WEAK_LINK_OP complexId '(' args ')' # MethodCall
    | expr WEAK_LINK_OP complexId '(' args ')' runnableScope # MethodCallAndDef
    | complexId '(' args ')' # FunctionCall
    | complexId '(' args ')' runnableScope # FunctionCallAndDef

    | NOT expr # Negation
    | op1=expr operator=(MUL|DIV) op2=expr # MulExpr
    | op1=expr operator=(ADD|SUB) op2=expr # AddExpr
    | op1=expr operator=(SUPERIOR_COMP|SUPERIOR_OR_EQUALS_COMP|INFERIOR_COMP|INFERIOR_OR_EQUALS_COMP) op2=expr # CompExpr
    | op1=expr operator=(EQUALS|NOT_EQUALS) op2=expr # EqualsExpr
    | op1=expr AND op2=expr # AndExpr
    | op1=expr OR op2=expr # OrExpr
    | '(' expr ')' # InnerExpr
    | <assoc=right> op1=expr '=' op2=expr # InitExpr

    | lambdaArg '->' lambdaProcess # LambdaExpr

    | additionalData '(' expr ')' # EnrichedExpr;
args : arg (',' arg)*;
arg : expr ('as' complexId)?;
type : POW* ID;
lambdaArg : complexId | '(' complexId (',' complexId)* ')';
lambdaProcess : runnableScope | expr;

additionalData : '@{' aData? (',' aData)* '}';
aData : ID ':' STRING;

ifStat : ifChunk elseIfChunk* elseChunk?;
ifChunk : 'if' '(' expr ')' runnableScope;
elseIfChunk : 'else' 'if' '(' expr ')' runnableScope;
elseChunk : 'else' runnableScope;

whileStat : 'while' '(' expr ')' runnableScope;

runnableScope : '{' stat* '}';

throwStat : 'throw' expr ';';

returnStat : 'return' expr? ';';

breakStat : 'break';

continueStat : 'continue';

complexId : ID ('<' type '>')?;

PROVIDED_VARIABLE_INDICATOR : '$';
REGEX : '/' 'a'+ '/';
WEAK_LINK_OP : '.';
STRONG_LINK_OP : '..';

ADD : '+';
SUB : '-';
MUL : '*';
DIV : '/';
POW : '^';
NOT : '!';
AND : '&&';
OR : '||';
SUPERIOR_COMP : '>';
SUPERIOR_OR_EQUALS_COMP : '>=';
INFERIOR_COMP : '<';
INFERIOR_OR_EQUALS_COMP : '<=';
EQUALS : '==';
NOT_EQUALS : '!=';
INT : (NON_ZERO_DIGIT DIGIT*| '0');
FLOAT : INT? '.' INT | INT '.' INT?;
CHAR : '\'' ( EscapeSequence | ~('\''|'\\') ) '\'';
fragment EscapeSequence : '\\' ('b'|'t'|'n'|'f'|'r'|'\''|'\\');
BOOLEAN : 'true'|'false';
STRING: '"' (ESC|.)*? '"' ;
fragment ESC : '\\"' | '\\\\' ;

ID : LETTER (LETTER | DIGIT)*;

fragment NON_ZERO_DIGIT : [1-9];
fragment DIGIT : [0-9];
fragment LETTER : [a-zA-Z_];

WS : [ \t\r\n] -> skip;