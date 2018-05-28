import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class App {


    private final String url = "jdbc:postgresql://localhost:5432/commoditiespriceprediction";
    private final String user = "postgres";
    private final String passwd = "postgres";
    private static String pathToFile = "../data/GOLD.json";

    public static void main(String[] args) {


	//nedds to specify type of data
        List<SQLRecord> list = JSONToSQLRecord.parseJSONFile("gold", pathToFile);

        /*List<SQLRecord> list = JSONToSQLRecord.parseURLResponse("gold", "https://www.quandl.com/api/v1/datasets/WIKI/FB/data.json?api_key=P9LFjDdg6yB2qZK526Nz");
        for (SQLRecord s : list)
            System.out.println(s);
        */


        App data = new App();
        //creates connection to database

        Connection conn = data.connect();

        DBStructureCreator dbStructureCreator = new DBStructureCreator(conn);

        //creates db structure. Drops table first if exists, so each call clears database
        dbStructureCreator.createDBStructure();

        SQLRecordRepositoryImpl repository = new SQLRecordRepositoryImpl(conn);

        //Adds list of records to database
        repository.addRecords(list);

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public Connection connect () {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, passwd);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }
}
