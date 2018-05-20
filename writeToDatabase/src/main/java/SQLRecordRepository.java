import java.sql.Date;
import java.util.List;

public interface SQLRecordRepository {

    void addRecords(List<SQLRecord> sqlRecordsList);
    Date getLastDate();
}
