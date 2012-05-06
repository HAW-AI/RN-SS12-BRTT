package haw.ai.rn;

import java.io.InputStream;

public final class Utils {
    // from http://stackoverflow.com/questions/309424/in-java-how-do-i-read-convert-an-inputstream-to-a-string
    public static String readLine(InputStream is) {
        try {
            return new java.util.Scanner(is).useDelimiter("\n").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }
}
