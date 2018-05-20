import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class SQLRecordRepositoryImpl implements SQLRecordRepository {

    private String MULTIPLE_INSERT = "INSERT INTO commodities (type, date, price_am, price_pm) VALUES (?, ?, ?, ?);";
    private String LAST_DATE = "SELECT max(date) FROM commodities;";

    private Connection connection;

    public SQLRecordRepositoryImpl(DataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
    }

    public SQLRecordRepositoryImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void addRecords(List<SQLRecord> sqlRecordsList) {
        try (PreparedStatement ps = connection.prepareStatement(MULTIPLE_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            boolean autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            for (SQLRecord sqlR : sqlRecordsList) {
                ps.setString(1, sqlR.getType());
                ps.setDate(2, sqlR.getDate());
                if (sqlR.getPriceAM() != null)
                    ps.setDouble(3, sqlR.getPriceAM());
                else
                    ps.setNull(3, Types.DOUBLE);
                if (sqlR.getPricePM() != null)
                    ps.setDouble(4, sqlR.getPricePM());
                else
                    ps.setNull(4, Types.DOUBLE);
                ps.addBatch();
            }

            ps.executeBatch();
            connection.commit();

            connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Date getLastDate() {
        Date date = null;

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(LAST_DATE);

            while (rs.next()) {
                date = rs.getDate(1);
                System.out.println(date);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return date;
    }

}
