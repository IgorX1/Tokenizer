package com.devx;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpliterWithDelims {
    static String[] splitAndKeep(String input, String regex, int offset) {
        ArrayList<String> res = new ArrayList<String>();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        int pos = 0;
        while (m.find()) {
            res.add(input.substring(pos, m.end() - offset));
            pos = m.end() - offset;
        }
        if(pos < input.length()) res.add(input.substring(pos));
        return res.toArray(new String[res.size()]);
    }

    /**
     * Splits a String according to a regex, keeping the splitter at the end of each substring
     * @param input The input String
     * @param regex The regular expression upon which to split the input
     * @return An array of Strings
     */
    static String[] splitAndKeep(String input, String regex) {
        return splitAndKeep(input, regex, 1);
    }
}
