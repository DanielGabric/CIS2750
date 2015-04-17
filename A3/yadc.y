/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 3
*/
%{
    #include <stdlib.h>
    #include <stdio.h>
    #include <string.h>
    #include "List.h"
    char * file;
    char * title;
    List * buttons = NULL;
    List * fields = NULL;
    List * fieldTypes = NULL;
    List * buttonListeners = NULL;
    int isGood = 1;
    int buttonLength=0;
    int fieldLength=0;
    int isButton =0;
    int isField=0;
    FILE * f;
    FILE * listeners;
    int hasFields=0;
    int hasButtons=0;

%}

%union
{
    char *sval;
    char * pval;
}

%token EQUALS
%token LBRACE
%token RBRACE
%token COMMA
%token SEMICOLON
%token <pval> TITLE
%token <pval> FIELDS
%token <pval> BUTTONS
%token <sval> __STRING__
%token <pval> RANDOMSTRING



%start program
%%
program        : statement
statement      : statement express
               | express
               ;



express        : TITLE EQUALS __STRING__ SEMICOLON          {
                                                                
                                                                if(title)
                                                                {
                                                                    
                                                                    yyerror("Title duplicated");
                                                                }
                                                                else
                                                                {
                                                                    char * edited = &$3[1];
                                                                    edited[strlen(edited)-1]='\0';
                                                                    
                                                                    title = edited;
                                                                }
                                                            }
               | FIELDS EQUALS LBRACE fieldlistitem SEMICOLON { hasFields=1; }
               | BUTTONS EQUALS LBRACE buttonlistitem SEMICOLON { hasButtons = 1; }
               | RANDOMSTRING EQUALS __STRING__ SEMICOLON   {
                                                                int c = searchList(buttons,$1);
                                                                int b = searchList(fields,$1);
                                                                if(c==-1&&b==-1)
                                                                {
                                                                    yyerror("Undeclared button/field values");
                                                                }

                                                                char * edited = &$3[1];
                                                                edited[strlen(edited)-1]='\0';
                                                                
                                                                if(c>=0)
                                                                {

                                                                    buttonListeners=addToL(buttonListeners, edited, c);
                                                                    fprintf(listeners,"%s\n",edited);
                                                                }
                                                                if(b>=0)
                                                                {
                                                                    fieldTypes=addToL(fieldTypes, edited, b);
                                                                }
                                                            }
               ;
fieldlistitem  : __STRING__ COMMA fieldlistitem {
                                                    char * edited = &$1[1];

                                                    edited[strlen(edited)-1]='\0';
                                                    if(searchList(buttons,edited)!=-1||searchList(fields,edited)!=-1)
                                                    {
                                                        
                                                        yyerror("Duplicate buttons/fields");
                                                    }
                                                    fields = addToL(fields,edited,fieldLength++);

                                                }
               | __STRING__ RBRACE              {

                                                    char * edited = &$1[1];
                                                    edited[strlen(edited)-1]='\0';
                                                    if(searchList(buttons,edited)!=-1||searchList(fields,edited)!=-1)
                                                    {
                                                        yyerror("Duplicate buttons/fields");
                                                    }
                                                    fields = addToL(fields,edited,fieldLength++);
                                                    
                                                }
               | RBRACE
               ;
buttonlistitem : __STRING__ COMMA buttonlistitem    {
                                                        char * edited = &$1[1];
                                                        edited[strlen(edited)-1]='\0';
                                                        if(searchList(buttons,edited)!=-1||searchList(fields,edited)!=-1)
                                                        {
                                                  
                                                            yyerror("Duplicate buttons/fields");
                                                        }
                                                        buttons = addToL(buttons,edited,buttonLength++);
                                                    }
               | __STRING__ RBRACE                  {
                                                        char * edited = &$1[1];
                                                        edited[strlen(edited)-1]='\0';
                                                        if(searchList(buttons,edited)!=-1||searchList(fields,edited)!=-1)
                                                        {
                                                            yyerror("Duplicate buttons/fields");
                                                        }
                                                        buttons = addToL(buttons,edited,buttonLength++);
                                                    }
               | RBRACE
               ;


%%

extern FILE *yyin;

int yyerror(char *s)
{
    fprintf(f, "%s.\n", s);
    fclose(f);

    exit(-1);
    return -1;
}

/*argv[1-6]=paths
  argv[7]=title*/
int main(int argc, char * argv[])
{
   
    int c = -1;
    
    f = fopen(argv[4],"w");
    char * pathToJavaFile = argv[1];
    char * pathToInterface = argv[2];
    char * pathToException = argv[3];

    listeners = fopen(argv[5],"w");
    if(!f)
    {
        return -1;
    }

    yyin = fopen(argv[6],"r");

    FILE * javaFile = fopen(pathToJavaFile,"w");
    if(!yyin||!isGood||!javaFile)
    {
        yyerror("Error in opening file");
        
        return 0;
    }

    while(!feof(yyin))
    {
        
        yyparse();
    }

    if(!hasFields||!hasButtons)
    {
        yyerror("fields or buttons lists are not defined");
    }

    int i;
    List * next=NULL;
    char allButtons[buttonLength][256];
    char allFields[fieldLength][256];
    char allFieldTypes[fieldLength][256];
    char allButtonListeners[buttonLength][256];
    next = buttons;
    while(next!=NULL)
    { 
        strcpy(allButtons[next->position],next->data);
        buttons = buttons->next;
        free(next);
        next = buttons;
    }
    next = fields;
    while((next!=NULL))
    { 
       
        strcpy(allFields[next->position],next->data);
        fields=fields->next;
        free(next);
        next = fields;
    }
    next = buttonListeners;
    while((next!=NULL))
    { 
        strcpy(allButtonListeners[next->position],next->data);
        buttonListeners=buttonListeners->next;
        free(next);
        next = buttonListeners;
    }
    next = fieldTypes;
    while((next!=NULL))
    { 
       
        strcpy(allFieldTypes[next->position],next->data);
        
        fieldTypes = fieldTypes->next;
        free(next);
        next = fieldTypes;
    }



    fprintf(javaFile,"import javax.swing.*;\n");
    fprintf(javaFile,"import javax.swing.text.*;\n");
    fprintf(javaFile,"import java.awt.*;\n");
    fprintf(javaFile,"import java.awt.event.*;\n");
    fprintf(javaFile,"public class %s extends JFrame implements %sFieldEdit {\n",argv[7],argv[7]);
    /*Variable declarations*/
    fprintf(javaFile,"    private JTextArea status;\n");
    fprintf(javaFile,"    private JScrollPane scroll;\n");

    List * moreData=NULL;
    /*STILL NEED TO ADD GET/SET METHODS*/

    for(i=0;i<fieldLength;++i)
    {

        char * name = allFields[i];
        char * name1 = allFieldTypes[i];
        if(strcmp(name1,"string")&&strcmp(name1,"integer")&&strcmp(name1,"float")){
            yyerror("Not string/integer/float");
            
            return 0;
        }
            
        fprintf(javaFile,"    private JTextField text%s;\n",name);
        fprintf(javaFile,"    private JLabel lbl%s;\n",name);
        fprintf(javaFile,"    public String getDC%s() throws IllegalFieldValueException{\n",name);
       
        if(!strcmp(name1,"integer")){
              
                //fprintf(javaFile,"    private int "+name+";\n");
            fprintf(javaFile,"        try{\n");
            fprintf(javaFile,"            Integer.parseInt(text%s.getText());\n",name);
            fprintf(javaFile,"        }catch(NumberFormatException nfe){\n");
            fprintf(javaFile,"            appendToStatusArea(\"Error, field %s does not have integer value!\");\n",name);
            fprintf(javaFile,"            text%s.setText(\"ERROR\");\n",name);
            fprintf(javaFile,"            throw new IllegalFieldValueException(\"Error, field %s does not have integer value!\");\n",name);
            fprintf(javaFile,"        }\n");
            fprintf(javaFile,"        return text%s.getText();\n",name);
        }else if(!strcmp(name1,"string")){
                    //fprintf(javaFile,"    private String "+name+";");
            fprintf(javaFile,"        return text%s.getText();\n",name);
            
        }else if(!strcmp(name1,"float")){

                //fprintf(javaFile,"    private float "+name+";");
            fprintf(javaFile,"        try{\n");
            fprintf(javaFile,"            Float.parseFloat(text%s.getText());\n",name);
            fprintf(javaFile,"        }catch(NumberFormatException nfe){\n");
            fprintf(javaFile,"            appendToStatusArea(\"Error, field %s does not have float value!\");\n",name);
            fprintf(javaFile,"            text%s.setText(\"ERROR\");\n",name);
            fprintf(javaFile,"            throw new IllegalFieldValueException(\"Error, field %s does not have float value!\");\n",name);
            fprintf(javaFile,"        }\n");
            fprintf(javaFile,"        return text%s.getText();\n",name);
            
        }
        fprintf(javaFile,"    }\n");
         /*I don't know what to change yet?*/
        fprintf(javaFile,"    public void setDC%s(String message){\n",name);
        fprintf(javaFile,"        text%s.setText(message);\n",name);
        fprintf(javaFile,"    }\n");
    }
    fprintf(javaFile,"    \n\n");
    c=-1;
    List * someData=NULL;
    for(i=0;i<buttonLength;++i)
    {
       
        char * name = allButtons[i];
        /*actionListeners.add(buttonListeners[someData->position]);*/
        fprintf(javaFile,"    private JButton btn%s;\n",name);
    }
    ////////////////////////////////////////*End of variable declarations*//////////////////////////////////////////////////
        
    /*Append to text area*/
    fprintf(javaFile,"    public void appendToStatusArea(String message){\n");
    fprintf(javaFile,"        status.append(message+\"\\n\");\n");
    fprintf(javaFile,"    }\n");
    /*End of method*/
        
    fprintf(javaFile,"    public %s(){\n",argv[7]);//contructor
    fprintf(javaFile,"        super();\n");
    /*Add this.*/fprintf(javaFile,"        setTitle(\"%s\");\n",title);
    fprintf(javaFile,"        status = new JTextArea();\n");//body of constructor starting HEREEE
    fprintf(javaFile,"        scroll = new JScrollPane(status);\n");
    fprintf(javaFile,"        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);\n");
    fprintf(javaFile,"        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);\n\n");
    fprintf(javaFile,"        \n");


    fprintf(javaFile,"        JPanel northPanel = new JPanel(new BorderLayout());\n");
    fprintf(javaFile,"        GridLayout grid1 = new GridLayout(%d,2);\n",fieldLength);
    fprintf(javaFile,"        GridLayout grid2 = new GridLayout(%d,4);\n",(buttonLength%4+buttonLength)/4);
    fprintf(javaFile,"        JPanel nnPanel = new JPanel(grid1);\n");
    fprintf(javaFile,"        JPanel nsPanel = new JPanel(grid2);\n");
    c=-1;
    List * data = NULL;

    for(i=0;i<fieldLength;++i)
    {

        char * r = allFields[i];
  
        fprintf(javaFile,"        lbl%s = new JLabel(\"%s\");\n",r,r);
        fprintf(javaFile,"        text%s = new JTextField();\n",r);
        fprintf(javaFile,"        nnPanel.add(lbl%s);\n",r);
        fprintf(javaFile,"        nnPanel.add(text%s);\n",r);
    }
    
    c=-1;

    for(i=0;i<buttonLength;++i)
    {
      
        char * r = allButtons[i];
        fprintf(javaFile,"        btn%s = new JButton(\"%s\");\n",r,r);
        fprintf(javaFile,"        btn%s.addActionListener(new %s(this));\n",r,allButtonListeners[i]);
        fprintf(javaFile,"        nsPanel.add(btn%s);\n",r);
    }
    fprintf(javaFile,"        northPanel.add(nnPanel,BorderLayout.NORTH);\n");
    fprintf(javaFile,"        northPanel.add(nsPanel,BorderLayout.SOUTH);\n");
    fprintf(javaFile,"        add(northPanel,BorderLayout.NORTH);\n");
    fprintf(javaFile,"        JLabel label1 = new JLabel(\"Status\");\n");
    fprintf(javaFile,"        label1.setHorizontalAlignment(SwingConstants.CENTER);\n");
    fprintf(javaFile,"        label1.setVerticalAlignment(SwingConstants.CENTER);\n");
    fprintf(javaFile,"        label1.setMaximumSize(new Dimension(5,0));\n");
    fprintf(javaFile,"        JPanel southPanel = new JPanel(new BorderLayout());\n");
    fprintf(javaFile,"        southPanel.add(label1,BorderLayout.NORTH);\n");
    fprintf(javaFile,"        southPanel.add(scroll,BorderLayout.CENTER);\n");
    fprintf(javaFile,"        add(southPanel,BorderLayout.CENTER);\n");
    fprintf(javaFile,"        setSize(500,600);\n");
    fprintf(javaFile,"        setVisible(true);\n");
    fprintf(javaFile,"        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n");
    fprintf(javaFile,"    }\n\n\n");//closing brace for constructor
    fprintf(javaFile,"    ");
    fprintf(javaFile,"    public static void main(String [] args){\n");
    fprintf(javaFile,"        new %s();\n",argv[7]);
    fprintf(javaFile,"    }\n");
    fprintf(javaFile,"}\n");//closing brace for class

    fclose(javaFile);
    javaFile = fopen(pathToInterface,"w");
    if(!javaFile)
    {
        yyerror("Java file can not open");

        return 0;
    }

    //INTERFACE, CHANGE TO DIFFERENT TEXTFILE
    fprintf(javaFile,"public interface %sFieldEdit{\n",argv[7]);
    fprintf(javaFile,"    public void appendToStatusArea(String message);\n");
    List * r = NULL;
    c=-1;
    for(i=0;i<fieldLength;++i)
    {
        
        fprintf(javaFile,"    public String getDC%s()throws IllegalFieldValueException; \n",allFields[i]);
        fprintf(javaFile,"    public void setDC%s(String toSet);\n",allFields[i]);
    }
    fprintf(javaFile,"}\n");

    fclose(javaFile);
    javaFile = fopen(pathToException,"w");
    if(!javaFile)
    {
        yyerror("Java file can not open");
        return 0;
    }

    fprintf(javaFile,"public class IllegalFieldValueException extends Exception{\n");
    fprintf(javaFile,"    public IllegalFieldValueException(String str){\n");
    fprintf(javaFile,"        super(str);\n");
    fprintf(javaFile,"    }\n");
    fprintf(javaFile,"}\n");


    if(f)
       fclose(f);
    fclose(javaFile);
    fclose(listeners);


    
    
    return 0;
}


