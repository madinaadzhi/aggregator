package org.madi.productaggregator.web.geo.json;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class JsonReader {
    private static String readAll(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int cp;
        while ((cp = reader.read()) != -1) {
            stringBuilder.append((char) cp);
        }
        return stringBuilder.toString();
    }

    public static JSONObject read(String url) throws IOException, JSONException {
        InputStream inStream = new URL(url).openStream();
        try {
            final BufferedReader buffReader = new BufferedReader(new InputStreamReader(inStream, Charset.forName("UTF-8")));
            final String jsonText = readAll(buffReader);
            final JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            inStream.close();
        }
    }
}
