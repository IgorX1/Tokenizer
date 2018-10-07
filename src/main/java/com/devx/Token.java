package com.devx;

public class Token {
    String token;
    TokenType type;

    public Token(String token ,TokenType type){
        this.token = token;
        this.type = type;
    }

    public String getToken(){
        return token;
    }

    public TokenType getType(){
        return type;
    }

    //TODO: add toString()

}
