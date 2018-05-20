import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBStructureCreator {

    private String TABLE_DROP = "DROP TABLE IF EXISTS commodities;";

    private String TABLE_CREATE = "CREATE TABLE commodities (\n" +
            "  id SERIAL,\n" +
            "  type VARCHAR(10),\n" +
            "  date DATE,\n" +
            "  price_am DOUBLE PRECISION,\n" +
            "  price_pm DOUBLE PRECISION,\n" +
            "  PRIMARY KEY (id)\n" +
            ");";

    private Connection connection;

    public DBStructureCreator(DataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
    }

    public DBStructureCreator(Connection connection) {
        this.connection = connection;
    }

    public void createDBStructure() {


        try (Statement stat = connection.createStatement()) {
            boolean autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);



            stat.executeUpdate(TABLE_DROP);



            stat.executeUpdate(TABLE_CREATE);

            //System.out.println("aaaaa");


            connection.commit();


        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e2) {
                e.printStackTrace();
            }
        }
    }

}
