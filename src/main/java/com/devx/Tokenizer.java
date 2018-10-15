package com.devx;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Tokenizer {
    private String text;
    private ArrayList<Token> tokenList = new ArrayList<>();
    private Set<String> keywords = new HashSet<>();
    private TokenType type;

    public Tokenizer(String path) throws Exception{
        try{
            text = readFile(path, Charset.defaultCharset());
        }catch (IOException exc){
            throw new Exception("The file can't be correctly read from path:"+path);
        }

        fillKeywordsSet();
    }

    private String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private void fillKeywordsSet(){
        keywords.add("int");
        keywords.add("byte");
        keywords.add("short");
        keywords.add("void");
        keywords.add("long");
        keywords.add("double");
        keywords.add("float");
        keywords.add("boolean");
        keywords.add("continue");
        keywords.add("break");
        keywords.add("if");
        keywords.add("while");
        keywords.add("do");
        keywords.add("for");
        keywords.add("else");
        keywords.add("case");
        keywords.add("catch");
        keywords.add("char");
        keywords.add("try");
        keywords.add("abstract");
        keywords.add("class");
        keywords.add("final");
        keywords.add("default");
        keywords.add("enum");
        keywords.add("extends");
        keywords.add("throw");
        keywords.add("throws");
        keywords.add("finally");
        keywords.add("goto");
        keywords.add("implements");
        keywords.add("import");
        keywords.add("instanceof");
        keywords.add("interface");
        keywords.add("new");
        keywords.add("package");
        keywords.add("private");
        keywords.add("public");
        keywords.add("protected");
        keywords.add("return");
        keywords.add("static");
        keywords.add("super");
        keywords.add("switch");
        keywords.add("synchronized");
        keywords.add("volatile");
        keywords.add("this");
        keywords.add("Override");
    }

    public void getTokens(){
        try{
            for(int index=0; index<text.length(); ++index){
                if(isWhiteSpaceCharacter(index))
                    continue;
                else if(isBlock(index))
                    index = getBlock(index);
                else if(isSemicolon(index))
                    index = getSemicolon(index);
                else if(isStartOfChar(index))
                    index = getCharacter(index);
                else if(isStartOfString(index))
                    index = getString(index);
                else if(isStartOfDigit(index))
                    index = getDigit(index);
                else if(isStartOfComment(index))
                    index = getComment(index);
                else  if(isStartOfOperator(index))
                    index = getOperator(index);
                else if(isStartOfIdentifierOrKeyword(index))
                    index = getIdentifierOrKeyWord(index);
            }
        }catch (StringIndexOutOfBoundsException exc){
            System.out.println("ERR");
        }

        printLexems();
    }

    private boolean isWhiteSpaceCharacter(int index){
        return Character.isSpaceChar(text.charAt(index));
    }

    private boolean isStartOfString(int index){
        return text.charAt(index)=='"';
    }

    private boolean isStartOfChar(int index){
        return  text.charAt(index)== '\'';
    }

    private boolean isStartOfDigit(int index){
        return Character.isDigit(text.charAt(index))
                ||(text.charAt(index)=='.' && Character.isDigit(text.charAt(index+1)));
    }

    private boolean isStartOfComment(int index){
        if(index+1<text.length())
            return text.charAt(index)=='/' && (text.charAt(index+1)=='/' || text.charAt(index+1)=='*');
        else return false;
    }

    private boolean isStartOfOperator(int index){
        return "+=!-&^*();,/.%[]~=<>".indexOf(text.charAt(index))!=-1;
    }

    private boolean isStartOfIdentifierOrKeyword(int index){
        char current = text.charAt(index);
        return (Character.isLetter(current)||current=='_'||current=='$');
    }

    private boolean isBlock(int index){
        return text.charAt(index)=='{' || text.charAt(index)=='}';
    }

    private boolean isSemicolon(int index){
        return text.charAt(index)==';';
    }

    private int getCharacter(int index){
        StringBuilder t = new StringBuilder();
        if(text.charAt(index+2)=='\''){
            tokenList.add(new Token(Character.toString(text.charAt(index+1)), type.CHARACTER));
            index += 2;//as character can be only one digit long (plus ')
        }
        else{
            for(index++; index<text.length(); ++index){
                if(text.charAt(index)=='\'')
                    break;
                else t.append(text.charAt(index));
            }
            tokenList.add(new Token(t.toString(), type.UNKNOWN));
        }

        return index;
    }

    private int getString(int index){
        StringBuilder t = new StringBuilder("");
        for(index++; index<text.length(); ++index){
            if(isWhiteSpaceCharacter(text.charAt(index)))
                continue;
            if(text.charAt(index)=='"')
                break;
            else t.append(text.charAt(index));
        }
        tokenList.add(new Token(t.toString(), type.STRING));
        return index;
    }

    private int getDigit(int index){
        StringBuilder t = new StringBuilder("");
        t.append(text.charAt(index++));
        for(; index<text.length(); ++index){
            if(isStartOfDigit(index)|| Character.isLetter(text.charAt(index)) || text.charAt(index)=='.' || text.charAt(index)=='x' || text.charAt(index)=='X'){
                t.append(text.charAt(index));
            }else{
                index--;
                break;
            }
        }

        String res = t.toString();
        if(res.matches("[0-9]+") || res.matches("[0-9]+.[0-9]+")|| res.matches("0(x|X)[0-9a-fA-F]+")|| res.matches(".[0-9]+")){
            tokenList.add(new Token(res, type.NUMBER));
        }else tokenList.add(new Token(res, type.UNKNOWN));

        return index;
    }

    private int getComment(int index){
        StringBuilder t = new StringBuilder("");
        index++;
        if(text.charAt(index)=='/'){
            index++;
            for(; index<text.length(); ++index){
                if(text.charAt(index)=='\n'){
                    //Trim right spaces
                    String rtrim = t.toString().replaceAll("\\s+$","");
                    tokenList.add(new Token(rtrim, type.COMMENT));
                    return index;
                }
                t.append(text.charAt(index));
            }
        }else{// we find *
            index++;
            for(; index<text.length(); ++index){
                if(text.charAt(index)=='*' && text.charAt(index+1)=='/'){
                    tokenList.add(new Token(t.toString(), type.COMMENT));
                    return ++index;
                }
                t.append(text.charAt(index));
            }
        }

        tokenList.add(new Token(t.toString(), type.UNKNOWN));
        return index;
    }

    private int getOperator(int index){
        StringBuilder t = new StringBuilder("");
        t.append(text.charAt(index++));
        if(index<text.length()){
            //case when operator consists of two characs
            if("+=!-&^*(),/.%[]~=<>".contains(Character.toString(text.charAt(index))) && (index+2<text.length()?text.charAt(index+1)!='/':true)){
                t.append(text.charAt(index));//++
            }
            else{
                String res = t.toString();
                tokenList.add(new Token(res, type.OPERATOR));
                index--;
                return index;
            }

            //case when operator consist of 3 characs
            if(index+1<text.length() && "<>=".contains(Character.toString(text.charAt(index+1)))&& (index+3<text.length()?text.charAt(index+2)!='/':true)){
                t.append(text.charAt(index++));
            }

            String res = t.toString();
            if(!(res.equals("++")||
                res.equals("--")||
                res.equals("==")||
                res.equals("!=")||
                res.equals("-=")||
                res.equals("*=")||
                res.equals("/=")||
                res.equals("%=")||
                res.equals("^=")||
                res.equals("&&")||
                res.equals("||")||
                res.equals("<<")||
                res.equals(">>")||
                res.equals("<<<")||
                res.equals("<<=")||
                res.equals(">>=")))
            {
                //tokenList.add(new Token(res, type.UNKNOWN));
                for (char c : res.toCharArray()){
                    tokenList.add(new Token(Character.toString(c), type.OPERATOR));
                }
                return index;
            }
        }
        String res = t.toString();
        tokenList.add(new Token(res, type.OPERATOR));
        return index;
    }

    private int getIdentifierOrKeyWord(int index){
        StringBuilder t = new StringBuilder();
        t.append(text.charAt(index));
        for(index++; index<text.length(); ++index){
            if(isStartOfIdentifierOrKeyword(index))
                t.append(text.charAt(index));
            else{
                --index;
                break;
            }
        }

        String res = t.toString();

        if(keywords.contains(res))
             tokenList.add(new Token(res, type.KEYWORD));
        else tokenList.add(new Token(res, type.IDENTIFIER));

        return index;
    }

    private int getBlock(int index){
        if(text.charAt(index)=='{')
            tokenList.add(new Token(Character.toString(text.charAt(index++)), type.BLOCK_BEGIN));
        else
            tokenList.add(new Token(Character.toString(text.charAt(index++)), type.BLOCK_END));
        return index;
    }

    private  int getSemicolon(int index){
        tokenList.add(new Token(Character.toString(text.charAt(index)), type.SEMICOLON));
        return index;
    }

    private void printLexems(){
        for(Token s:tokenList){
            System.out.println(s.getToken()+"  ::  "+s.getType());
        }
    }
}
