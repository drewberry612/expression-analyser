grammar Compiler;

start:  prog ;

prog:   (line NEWLINE)*
    |   prog (line NEWLINE)+
    |   prog line+
    |   (function_def NEWLINE)*
    |   prog (function_def NEWLINE)+
    |   prog function_def+
    ;

function_def: 'def' RETURN_TYPE FUNCT (VAR)* (',' VAR)* '){' NEWLINE function_body '}' ;

function_body: line+ ;

line:   assign NEWLINE
    |   print NEWLINE
    |   function_call NEWLINE
    |   ifelse NEWLINE
    |   while NEWLINE
    |   for NEWLINE
    |   'return' expression NEWLINE
    ;

ifelse: if
      | if NEWLINE else
      | if NEWLINE (elif NEWLINE)+ else
      ;

if: 'if' condition '){' NEWLINE line+ '}' ;

elif: 'elif' condition '){' NEWLINE line+ '}' ;

else: 'else{' NEWLINE line+ '}' ;

while: 'while' condition '){' NEWLINE line+ '}' ;

for: 'for' VAR 'in range(' (NUM|VAR) '){' NEWLINE line+ '}' ;

condition:  '(' expression COND_OP expression
        |   '(' VAR ('==='|'!==') STRING
        |   '(' VAR
        |   '(!' VAR
        ;

assign: VAR ASSIGN_OP expression
      | VAR ASSIGN_OP function_call
      | VAR ASSIGN_OP STRING
      | VAR ASSIGN_OP BOOL
      ;

expression: expr ;

expr:	expr MULT expr      # EXPR1
    |   expr ADD expr       # EXPR2
    |   expr MOD expr       # EXPR3
    |	NUM ('^' NUM)*      # EXPR4
    |   VAR ('^' NUM)*      # EXPR5
    |   brackets            # EXPR6
    ;

brackets: '(' expr ')'
        | '(' expr ')' '^' NUM
        ;

print: 'print(' expression ')'
     | 'print(' STRING ')'
     ;

function_call: FUNCT (VAR|NUM)* (',' (VAR|NUM))* ')';

FUNCT: [a-z]+ '(' ;

RETURN_TYPE: 'string'
           | 'bool'
           | 'float'
           | 'void'
           ;

COND_OP: ('==='|'>='|'<='|'>'|'<'|'!==') ;

ASSIGN_OP:  ('='|'+='|'-=') ;

MULT: ('*'|'/') ;

ADD: ('+'|'-') ;

MOD: '%' ;

NEWLINE: [\r\n]+ ;

STRING: '"' [a-z]+ '"' ;

NUM: [0-9]+
   | [0-9]+ '.' [0-9]+
   ;

VAR: [a-z]+ ;

BOOL: 'True'
    | 'False' ;

WS: [ \t\r\n]+ -> skip;