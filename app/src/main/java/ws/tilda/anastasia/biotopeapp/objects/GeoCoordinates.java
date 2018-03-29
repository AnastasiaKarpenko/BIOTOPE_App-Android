package ws.tilda.anastasia.biotopeapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class GeoCoordinates implements Parcelable {
    private double mLongitude;
    private double mLatitude;
    private Address mAddress;

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public Address getAddress() {
        return mAddress;
    }

    public void setAddress(Address address) {
        mAddress = address;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.mLongitude);
        dest.writeDouble(this.mLatitude);
        dest.writeParcelable(this.mAddress, flags);
    }

    public GeoCoordinates() {
    }

    protected GeoCoordinates(Parcel in) {
        this.mLongitude = in.readDouble();
        this.mLatitude = in.readDouble();
        this.mAddress = in.readParcelable(Address.class.getClassLoader());
    }

    public static final Parcelable.Creator<GeoCoordinates> CREATOR = new Parcelable.Creator<GeoCoordinates>() {
        @Override
        public GeoCoordinates createFromParcel(Parcel source) {
            return new GeoCoordinates(source);
        }

        @Override
        public GeoCoordinates[] newArray(int size) {
            return new GeoCoordinates[size];
        }
    };
}
