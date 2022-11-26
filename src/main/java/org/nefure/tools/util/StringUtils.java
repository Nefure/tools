package org.nefure.tools.util;

/**
 * @author nefure
 * @since 2022/11/25 22:39
 */
public class StringUtils {

    public static int LOW_TO_HIGH = 'A' - 'a';

    public static String smallHump(String str){
        str = str.toLowerCase();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++){
            char ch = str.charAt(i);
            if (ch != '_'){
                if (i != 0 && str.charAt(i -1) == '_'){
                    builder.append((char) (ch + LOW_TO_HIGH));
                }else {
                    builder.append(ch);
                }
            }
        }
        return builder.toString();
    }

    public static String bigHump(String str){
        return smallHump("_"+str);
    }

}
