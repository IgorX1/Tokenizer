package com.devx;

public class App
{
    public static void main( String[] args )
    {
        try {
            System.out.println("TOKENIZER by Igor Konobas (TTP-31, 2018)");
            Tokenizer tokenizer = new Tokenizer("code.txt");
            tokenizer.getTokens();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
