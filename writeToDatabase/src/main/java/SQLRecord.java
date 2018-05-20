import java.sql.Date;

public class SQLRecord {

    private Long id;
    private String type;
    private Date date;
    private Double priceAM;
    private Double pricePM;

    public SQLRecord() {
    }

    public SQLRecord(String type, Date date, Double priceAM, Double pricePM) {
        this.type = type;
        this.date = date;
        this.priceAM = priceAM;
        this.pricePM = pricePM;
    }

    public SQLRecord(Long id, String type, Date date, Double priceAM, Double pricePM) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.priceAM = priceAM;
        this.pricePM = pricePM;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getPriceAM() {
        return priceAM;
    }

    public void setPriceAM(Double priceAM) {
        this.priceAM = priceAM;
    }

    public Double getPricePM() {
        return pricePM;
    }

    public void setPricePM(Double pricePM) {
        this.pricePM = pricePM;
    }

    @Override
    public String toString() {
        return "SQLRecord{" +
                "type='" + type + '\'' +
                ", date=" + date +
                ", priceAM=" + priceAM +
                ", pricePM=" + pricePM +
                '}';
    }
}
