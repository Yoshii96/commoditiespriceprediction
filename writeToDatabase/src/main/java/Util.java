import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class Util {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, ParseException {
        InputStream is =  new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);

            System.out.println(jsonText);

            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(jsonText);
            JSONObject jsonObject = (JSONObject) obj;
            return jsonObject;
        } finally {
            is.close();
        }
    }
}
