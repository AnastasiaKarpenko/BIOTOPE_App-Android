package ws.tilda.anastasia.biotopeapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {
    private String mStreetAddress;
    private String mPostalCode;
    private String mAddressRegion;
    private String mAddressCountry;
    private String mAddressLocality;

    public String getStreetAddress() {
        return mStreetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        mStreetAddress = streetAddress;
    }

    public String getPostalCode() {
        return mPostalCode;
    }

    public void setPostalCode(String postalCode) {
        mPostalCode = postalCode;
    }

    public String getAddressRegion() {
        return mAddressRegion;
    }

    public void setAddressRegion(String addressRegion) {
        mAddressRegion = addressRegion;
    }

    public String getAddressCountry() {
        return mAddressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        mAddressCountry = addressCountry;
    }

    public String getAddressLocality() {
        return mAddressLocality;
    }

    public void setAddressLocality(String addressLocality) {
        mAddressLocality = addressLocality;
    }

    public String toString() {
        String address = mStreetAddress + ", " + mAddressRegion + ", " +
                 mAddressLocality + ", " + mAddressCountry;
        return address;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mStreetAddress);
        dest.writeString(this.mPostalCode);
        dest.writeString(this.mAddressRegion);
        dest.writeString(this.mAddressCountry);
        dest.writeString(this.mAddressLocality);
    }

    public Address() {
    }

    protected Address(Parcel in) {
        this.mStreetAddress = in.readString();
        this.mPostalCode = in.readString();
        this.mAddressRegion = in.readString();
        this.mAddressCountry = in.readString();
        this.mAddressLocality = in.readString();
    }

    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
