import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class JSONToSQLRecord {

    public static List<SQLRecord> parseJSONFile(String type, String pathToFile) {

        List<SQLRecord> sqlRecordList = null;

        JSONParser jsonParser = new JSONParser();

        try {
            Object obj = jsonParser.parse(new FileReader(pathToFile));

            JSONObject jsonObject = (JSONObject) obj;
            JSONArray jsonArray = (JSONArray) jsonObject.get("data");

            sqlRecordList = parseDataArray(type, jsonArray);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sqlRecordList;
    }

    public static List<SQLRecord> parseURLResponse(String type, String url) {

        List<SQLRecord> sqlRecordList = null;

        try {
            JSONObject jsonObject = Util.readJsonFromUrl(url);
            JSONArray jsonArray = (JSONArray) ((JSONObject) jsonObject.get("dataset_data")).get("data");

            sqlRecordList = parseDataArray(type, jsonArray);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sqlRecordList;
    }

    private static List<SQLRecord> parseDataArray(String type, JSONArray jsonArray) throws ParseException {



        List<SQLRecord> sqlRecordList = new ArrayList<>();

        for (Object o : jsonArray) {
            JSONArray array = (JSONArray) o;
            //System.out.println(array);
            Date date = Date.valueOf((String) array.get(0));
            Double priceAM = (array.get(1) == null) ? null : ((Number) array.get(1)).doubleValue();
            Double pricePM = (array.get(2) == null) ? null : ((Number) array.get(2)).doubleValue();
            SQLRecord sqlRecord = new SQLRecord(type, date, priceAM, pricePM);

            sqlRecordList.add(sqlRecord);
        }

        return sqlRecordList;
    }

}
