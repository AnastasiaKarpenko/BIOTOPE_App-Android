package ws.tilda.anastasia.biotopeapp.objects;

public class AvailableService {
    private String mId;
    private String mServiceID;
    private String mType;
    private String mTitle;
    private double mPrice;
    private double mReputation;
    private String mInfoitemUrl;
    private String mOmiNodeUrl;
    private GeoCoordinates mGeoCoordinates;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getServiceID() {
        return mServiceID;
    }

    public void setServiceID(String serviceID) {
        mServiceID = serviceID;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public double getReputation() {
        return mReputation;
    }

    public void setReputation(double reputation) {
        mReputation = reputation;
    }

    public String getInfoitemUrl() {
        return mInfoitemUrl;
    }

    public void setInfoitemUrl(String infoitemUrl) {
        mInfoitemUrl = infoitemUrl;
    }

    public String getOmiNodeUrl() {
        return mOmiNodeUrl;
    }

    public void setOmiNodeUrl(String omiNodeUrl) {
        mOmiNodeUrl = omiNodeUrl;
    }

    public GeoCoordinates getGeoCoordinates() {
        return mGeoCoordinates;
    }

    public void setGeoCoordinates(GeoCoordinates geoCoordinates) {
        mGeoCoordinates = geoCoordinates;
    }
}
