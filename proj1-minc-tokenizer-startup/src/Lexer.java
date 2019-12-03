import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Worked with Seong Min An
 */

public class Lexer
{
    private static final char EOF        =  0;

    private Parser         yyparser; // parent parser object
    private java.io.Reader reader;   // input stream
    public int             lineno;   // line number
    public String          tokenname;
    private char[] buffer1, buffer2;
    private int idx = 0;

    private List<Character> opList = Arrays.asList('+', '-', '*', '/');

    public Lexer(java.io.Reader reader, Parser yyparser) throws java.io.IOException
    {
        this.reader   = reader;
        this.yyparser = yyparser;
        lineno = 1;
        tokenname = new String();
    }

    public char NextChar() throws IOException
    {
        // http://tutorials.jenkov.com/java-io/readers-writers.html
        int data = reader.read();

        if(data == -1)
        {
            return EOF;
        }
        return (char)data;
    }

    public char[] GetBuffer() throws IOException{
        char[] buffer = new char[10];
        char c;
        int count = 0;
        while (count < 9){
            buffer[count] = NextChar();
            count++;
        }

        return buffer;
    }

    public int Fail()
    {
        return -1;
    }

    public int yylex() throws java.io.IOException
    {
        int state = 0;
        String lexeme = new String();
        char c;

        while(true)
        {

            if (buffer1 == null) buffer1 = GetBuffer();
            if (buffer2 == null) buffer2 = GetBuffer();

            c = buffer1[idx];
            if (c == EOF){
                buffer1 = buffer2;
                buffer2 = GetBuffer();
                idx = 0;
                c = buffer1[idx];
            }

            switch(state)
            {

                case 0:
                    if (c == ' ' || c == '\t' || c == '\r'){
                        idx++;
                        break;
                    }

                    if (c == '\n'){
                        lineno++;
                        idx++;
                        break;
                    }

                    if (c == '+' || c == '-' || c == '*' || c == '/'){
                        lexeme += c;
                        state = 10;
                        idx++;
                        break;
                    }

                    if (c == '=') {
                        lexeme += c;
                        state = 1;
                        idx++;
                        break;
                    }

                    if (c == '<'){
                        lexeme += c;
                        state = 2;
                        idx++;
                        break;
                    }

                    if (c == '>'){
                        lexeme += c;
                        state = 2;
                        idx++;
                        break;
                    }

                    if (c == '!'){
                        lexeme += c;
                        state = 3;
                        idx++;
                        break;
                    }

                    if (c == '{'){
                        lexeme += c;
                        state = 13;
                        idx++;
                        break;
                    }

                    if (c == '}'){
                        lexeme += c;
                        state = 14;
                        idx++;
                        break;
                    }

                    if (c == '('){
                        lexeme += c;
                        state = 15;
                        idx++;
                        break;
                    }

                    if (c == ')'){
                        lexeme += c;
                        state = 16;
                        idx++;
                        break;
                    }

                    if (c == ';'){
                        lexeme += c;
                        state = 17;
                        idx++;
                        break;
                    }

                    if (c == ','){
                        lexeme += c;
                        state = 18;
                        idx++;
                        break;
                    }

                    if (Character.isDigit(c)){
                        lexeme += c;
                        state = 4;
                        idx++;
                        break;
                    }

                    if (Character.isLetter(c) || c == '_'){
                        lexeme += c;
                        state = 7;
                        idx++;
                        break;
                    }

                    if(c == EOF) {
                        state = 99;
                        break;
                    }

                    return Fail();

                case 1:
                    if (c == '='){
                        lexeme += c;
                        state = 11;
                        idx++;
                        break;
                    }
                    state = 12;
                    break;

                case 2:
                    if (c == '='){
                        lexeme += c;
                        state = 11;
                        idx++;
                        break;
                    }

                    state = 11;
                    break;

                case 3:
                    if (c == '='){
                        lexeme += c;
                        state = 11;
                        idx++;
                        break;
                    }

                    return Fail();

                case 4:
                    if (Character.isDigit(c)){
                        lexeme += c;
                        idx++;
                        break;
                    }
                    if (c == '.'){
                        lexeme += c;
                        state = 5;
                        idx++;
                        break;
                    }
                    if (c == '+' || c == '-' || c == '*' || c == '/' || c == ';' || c == '<' || c == '>' || c == '=' || c == '!' || c == ',' || c == ')' || c == ' ' || c == '\t' || c == '\r' || c == '\n'){
                        state = 19;
                        break;
                    }

                    return Fail();

                case 5:
                    if (Character.isDigit(c)){
                        lexeme += c;
                        state = 6;
                        idx++;
                        break;
                    }

                    return Fail();

                case 6:
                    if (Character.isDigit(c)){
                        lexeme += c;
                        idx++;
                        break;
                    }

                    if (c == '+' || c == '-' || c == '*' || c == '/' || c == ';' || c == '<' || c == '>' || c == '=' || c == '!' || c == ','  || c == ')' || c == ' ' || c == '\t' || c == '\r' || c == '\n'){
                        state = 19;
                        break;
                    }

                    return Fail();

                case 7:
                    if (Character.isLetter(c) || Character.isDigit(c) || c == '_'){
                        lexeme += c;
                        idx++;
                        break;
                    }
                    else{
                        if (lexeme.equals("main")){
                            state = 21;
                            break;
                        }
                        if (lexeme.equals("print")){
                            state = 22;
                            break;
                        }
                        if (lexeme.equals("if")){
                            state = 23;
                            break;
                        }
                        if (lexeme.equals("else")){
                            state = 24;
                            break;
                        }

                        if (c == '+' || c == '-' || c == '*' || c == '/' || c == ';' || c == '<' || c == '>' || c == '=' || c == '!' || c == ',' || c == '(' || c == ')' || c == ' ' || c == '\t' || c == '\r' || c == '\n'){
                            state = 20;
                            break;
                        }

                        return Fail();
                    }

                case 10:
                    yyparser.yylval = new ParserVal((Object) lexeme);
                    tokenname = "OP";
                    return Parser.OP;

                case 11:
                    yyparser.yylval = new ParserVal((Object) lexeme);
                    tokenname = "RELOP";
                    return Parser.RELOP;

                case 12:
                    yyparser.yylval = new ParserVal((Object) lexeme);
                    tokenname = "ASSIGN";
                    return Parser.ASSIGN;

                case 13:
                    yyparser.yylval = new ParserVal((Object) lexeme);
                    tokenname = "BEGIN";
                    return Parser.BEGIN;

                case 14:
                    yyparser.yylval = new ParserVal((Object) lexeme);
                    tokenname = "END";
                    return Parser.END;

                case 15:
                    yyparser.yylval = new ParserVal((Object) lexeme);
                    tokenname = "LPAREN";
                    return Parser.LPAREN;

                case 16:
                    yyparser.yylval = new ParserVal((Object) lexeme);
                    tokenname = "RPAREN";
                    return Parser.RPAREN;

                case 17:
                    yyparser.yylval = new ParserVal((Object) lexeme);
                    tokenname = "SEMI";
                    return Parser.SEMI;

                case 18:
                    yyparser.yylval = new ParserVal((Object) lexeme);
                    tokenname = "COMMA";
                    return Parser.COMMA;

                case 19:
                    yyparser.yylval = new ParserVal((Object) lexeme);
                    tokenname = "NUM";
                    return Parser.NUM;


                case 20:
                    yyparser.yylval = new ParserVal((Object)lexeme);
                    tokenname = "ID";
                    return Parser.ID;

                case 21:
                    yyparser.yylval = new ParserVal((Object) lexeme);
                    tokenname = "MAIN";
                    return Parser.MAIN;

                case 22:
                    yyparser.yylval = new ParserVal((Object) lexeme);
                    tokenname = "PRINT";
                    return Parser.PRINT;

                case 23:
                    yyparser.yylval = new ParserVal((Object) lexeme);
                    tokenname = "IF";
                    return Parser.IF;

                case 24:
                    yyparser.yylval = new ParserVal((Object) lexeme);
                    tokenname = "ELSE";
                    return Parser.ELSE;

                case 99:
                    return EOF;                                     // return end-of-file symbol
            }
        }
    }
}
