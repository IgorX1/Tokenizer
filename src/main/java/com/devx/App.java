package com.devx;

import static com.devx.SpliterWithDelims.splitAndKeep;

public class App
{
    public static void main( String[] args )
    {
        String d = "2+3=9+=12-5*7";
        for (String s: splitAndKeep(d, "(\\+=|\\=|\\+|\\*|-)", 0)) System.out.println(s);
    }
}
