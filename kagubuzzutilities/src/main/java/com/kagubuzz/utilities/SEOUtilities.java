package com.kagubuzz.utilities;

public class SEOUtilities {
    
    /// <summary>
    /// Produces optional, URL-friendly version of a title, "like-this-one". 
    /// hand-tuned for speed, reflects performance refactoring contributed
    /// by John Gietzen (user otac0n) 
    /// </summary>
    
    public static String URLFriendly(String title)
    {
        if (title == null) return "";
    
        int maxlen = 80;
        int len = title.length();
        boolean prevdash = false;
        StringBuilder sb = new StringBuilder(len);
        char c;
    
        for (int i = 0; i < len; i++)
        {
            c = title.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9'))
            {
                sb.append(c);
                prevdash = false;
            }
            else if (c >= 'A' && c <= 'Z')
            {
                // tricky way to convert to lowercase
                sb.append((char)(c | 32));
                prevdash = false;
            }
            else if (c == ' ' || c == ',' || c == '.' || c == '/' || 
                c == '\\' || c == '-' || c == '_' || c == '=')
            {
                if (!prevdash && sb.length() > 0)
                {
                    sb.append('-');
                    prevdash = true;
                }
            }
            else if ((int)c >= 128)
            {
                int prevlen = sb.length();
                sb.append(RemapInternationalCharToAscii(c));
                if (prevlen != sb.length()) prevdash = false;
            }
            if (i == maxlen) break;
        }
    
        if (prevdash)
            return sb.toString().substring(0, sb.length() - 1);
        else
            return sb.toString();
    }

    public static String RemapInternationalCharToAscii(char c)
    {
        String s = String.valueOf(c).toLowerCase();
        
        if ("àåáâäãåą".contains(s))
        {
            return "a";
        }
        else if ("èéêëę".contains(s))
        {
            return "e";
        }
        else if ("ìíîïı".contains(s))
        {
            return "i";
        }
        else if ("òóôõöøőð".contains(s))
        {
            return "o";
        }
        else if ("ùúûüŭů".contains(s))
        {
            return "u";
        }
        else if ("çćčĉ".contains(s))
        {
            return "c";
        }
        else if ("żźž".contains(s))
        {
            return "z";
        }
        else if ("śşšŝ".contains(s))
        {
            return "s";
        }
        else if ("ñń".contains(s))
        {
            return "n";
        }
        else if ("ýÿ".contains(s))
        {
            return "y";
        }
        else if ("ğĝ".contains(s))
        {
            return "g";
        }
        else if (c == 'ř')
        {
            return "r";
        }
        else if (c == 'ł')
        {
            return "l";
        }
        else if (c == 'đ')
        {
            return "d";
        }
        else if (c == 'ß')
        {
            return "ss";
        }
        else if (c == 'Þ')
        {
            return "th";
        }
        else if (c == 'ĥ')
        {
            return "h";
        }
        else if (c == 'ĵ')
        {
            return "j";
        }
        else
        {
            return "";
        }
    }
}

