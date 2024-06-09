grammar ExprLang;

start:  prog    ;
prog:   (expression NEWLINE)*
    |   (assign NEWLINE)*
    |   prog (expression NEWLINE)+
    |   prog (assign NEWLINE)+
    |   prog expression+
    |   prog assign+
    ;
assign: VAR '=' expression    ;
expression: expr    ;
expr:	expr MULT expr      # EXPR1
    |   expr ADD expr       # EXPR2
    |	NUM ('^')* NUM*     # EXPR3
    |   VAR ('^')* NUM*     # EXPR4
    |   brackets            # EXPR5
    ;
brackets:   '(' expr ')'
        |   '(' expr ')' '^' NUM
        ;

MULT: ('*'|'/')   ;
ADD : ('+'|'-')   ;
NEWLINE : [\r\n]+ ;
NUM     : [0-9]+
        | [0-9]+ '.' [0-9]+
        ;
VAR     : [a-z]+ ;
WS      : [ \t\r\n]+ -> skip;