// Download by http://www.codefans.net
// Source File Name:   Local.java

package com.nokia.mid.appl.boun;

import java.io.DataInputStream;
import java.io.InputStream;

public class Local
{

    private static Local loc = null;
    public static final short QHJ_BOUN_INSTRUCTIONS_PART_1 = 0;
    public static final short QHJ_BOUN_INSTRUCTIONS_PART_2 = 1;
    public static final short QHJ_BOUN_INSTRUCTIONS_PART_3 = 2;
    public static final short QHJ_BOUN_INSTRUCTIONS_PART_4 = 3;
    public static final short QHJ_BOUN_INSTRUCTIONS_PART_5 = 4;
    public static final short QHJ_BOUN_INSTRUCTIONS_PART_6 = 5;
    public static final short QTJ_BOUN_BACK = 6;
    public static final short QTJ_BOUN_CONGRATULATIONS = 7;
    public static final short QTJ_BOUN_CONTINUE = 8;
    public static final short QTJ_BOUN_EXIT = 9;
    public static final short QTJ_BOUN_GAME_NAME = 10;
    public static final short QTJ_BOUN_GAME_OVER = 11;
    public static final short QTJ_BOUN_HIGH_SCORES = 12;
    public static final short QTJ_BOUN_INSTRUCTIONS = 13;
    public static final short QTJ_BOUN_LEVEL = 14;
    public static final short QTJ_BOUN_LEVEL_COMPLETED = 15;
    public static final short QTJ_BOUN_NEW_GAME = 16;
    public static final short QTJ_BOUN_NEW_HIGH_SCORE = 17;
    public static final short QTJ_BOUN_NEXT = 18;
    public static final short QTJ_BOUN_OK = 19;
    public static final short QTJ_BOUN_PAUSE = 20;
    public static final String phoneLang = System.getProperty("microedition.locale");

    private Local()
    {
    }

    private static String replace(String s, String s1, String s2)
    {
        int i = s.indexOf(s1);
        return i < 0 ? s : s.substring(0, i) + s2 + s.substring(i + s1.length());
    }

    public static synchronized String getText(int i)
    {
        return getText(i, null);
    }

    public static synchronized String getText(int i, String as[])
    {
        try
        {
            if(loc == null)
                loc = new Local();
            InputStream inputstream = loc.getClass().getResourceAsStream("/lang." + phoneLang);
            if(inputstream == null)
                inputstream = loc.getClass().getResourceAsStream("/lang.xx");
            if(inputstream == null)
                return "NoLang";
            DataInputStream datainputstream = new DataInputStream(inputstream);
            datainputstream.skipBytes(i * 2);
            short word0 = datainputstream.readShort();
            datainputstream.skipBytes(word0 - i * 2 - 2);
            String s = datainputstream.readUTF();
            datainputstream.close();
            if(as != null)
                if(as.length == 1)
                {
                    s = replace(s, "%U", as[0]);
                } else
                {
                    for(int j = 0; j < as.length; j++)
                        s = replace(s, "%" + j + "U", as[j]);

                }
            return s;
        }
        catch(Exception exception)
        {
            return "Err";
        }
    }

}
