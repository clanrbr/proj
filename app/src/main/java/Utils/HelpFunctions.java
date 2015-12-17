package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Ado on 11/30/2015.
 */
public class HelpFunctions {

    public static String getString(InputStream stream) throws IOException {
        if (stream == null) {
            return "";
        }
        BufferedReader reader =new BufferedReader(new InputStreamReader(stream));
        StringBuilder content = new StringBuilder();
        String newLine;
        do {
            newLine = reader.readLine();
            if (newLine != null) {
                content.append(newLine).append('\n');
            }
        } while (newLine != null);
        if (content.length() > 0) {
            // strip last newline
            content.setLength(content.length() - 1);
        }
        return content.toString();
    }
}
