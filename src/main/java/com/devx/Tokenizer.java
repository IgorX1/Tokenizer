package com.devx;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
            text = readFile("code.txt", Charset.defaultCharset());
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
    }

    public void getTokens(){
        for(int index=0; index<text.length(); ++index){
            if(isWhiteSpaceCharacter(index))
                continue;
            else if(isStartOfChar(index))
                index = getCharacter(index);
            else if(isStartOfString(index))
                index = getString(index);
            else if(isStartOfDigit(index))
                index = getDigit(index);
            else  if(isStartOfOperator(index))
                index = getOperator(index);
            else if(isStartOfComment(index))
                index = getComment(index);
            else if(isStartOfIdentifierOrKeyword(index))
                index = getIdentifierOrKeyWord(index);
        }
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
        return Character.isDigit(text.charAt(index));
    }

    private boolean isStartOfComment(int index){
        return text.charAt(index)=='/';
    }

    private boolean isStartOfOperator(int index){
        return "+=!-&^*();,/.%[]~=<>".indexOf(text.charAt(index))!=-1;
    }

    private boolean isStartOfIdentifierOrKeyword(int index){
        char current = text.charAt(index);
        return (Character.isLetter(current)||current=='_'||current=='$');
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
        for(index++; index<text.length() && !isWhiteSpaceCharacter(text.charAt(index)); ++index){
            /*if(isStartOfDigit(text.charAt(index)) || text.charAt(index)=='.' || text.charAt(index)=='x' || text.charAt(index)=='X'){
                t.append(text.charAt(index));
            }else{
                index--;
                break;
            }*/
            t.append(text.charAt(index));
        }

        String res = t.toString();
        if(res.matches("[0-9]+") || res.matches("[0-9]+.[0-9]+")|| res.matches("0(x|X)[0-9]+")){
            tokenList.add(new Token(res, type.NUMBER));
        }else tokenList.add(new Token(res, type.UNKNOWN));

        return index;
    }

    //TODO: implement it
    private int getComment(int index){
        StringBuilder t = new StringBuilder("");
        throw new NotImplementedException();
    }

    private int getOperator(int index){
        StringBuilder t = new StringBuilder("");
        t.append(text.charAt(index++));
        if(index<text.length()){
            if("+=!-&^*();,/.%[]~=<>".contains(Character.toString(text.charAt(index)))){
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
                res.equals(">>")))
            {
                tokenList.add(new Token(res, type.UNKNOWN));
                return index;
            }
        }
        String res = t.toString();
        tokenList.add(new Token(res, type.OPERATOR));
        return index;
    }

    private int getIdentifierOrKeyWord(int index){
        StringBuilder t = new StringBuilder(text.charAt(index));
        for(index++; index<text.length(); ++index){

        }
    }
}
