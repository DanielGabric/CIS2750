/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 3
*/
%{
#include "y.tab.h"
#include <stdio.h>
%}

whitespace   [\t\r ]+
newLine      [\n]
comment      #(.*)
l_brace      \{
r_brace      \}
comma        ,
parameter    ([a-zA-Z_][_a-zA-Z0-9]*)
_equals       =
semi_colon    ;
_string       \"[^\";]*\"


%%

{comment}    {}
{whitespace} {}
{newLine}    {}
{parameter}  {
	            yylval.pval = strdup(yytext);
                if(!strcmp(yytext,"fields"))
                {
                    return FIELDS;
                }
                else if(!strcmp(yytext,"title"))
                {
                    return TITLE;
                }
                else if(!strcmp(yytext,"buttons"))
                {
                    return BUTTONS;
                }
                else
                {
                    return RANDOMSTRING;
                }
             }
{_equals}    {return EQUALS;}
{l_brace}    {return LBRACE;}
{_string}    {yylval.sval = strdup(yytext);return __STRING__;}
{comma}      {return COMMA;}
{r_brace}    {return RBRACE;}
{semi_colon} {return SEMICOLON;}

%%

