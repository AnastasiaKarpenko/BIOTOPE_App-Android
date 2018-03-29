package ws.tilda.anastasia.biotopeapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ParkingFacility implements Parcelable {
    private String mId;
    private String mIsOwnedBy;
    private OpeningHoursSpecifications mOpeningHoursSpecifications;
    private GeoCoordinates mGeoCoordinates;
    private List<ParkingCapacity> mCapacities;
    private List<ParkingSpace> mParkingSpaces;

    public ParkingFacility() {
        mCapacities = new ArrayList<ParkingCapacity>();
        mParkingSpaces = new ArrayList<ParkingSpace>();
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getIsOwnedBy() {
        return mIsOwnedBy;
    }

    public void setIsOwnedBy(String isOwnedBy) {
        mIsOwnedBy = isOwnedBy;
    }

    public OpeningHoursSpecifications getOpeningHoursSpecifications() {
        return mOpeningHoursSpecifications;
    }

    public void setOpeningHoursSpecifications(OpeningHoursSpecifications openingHoursSpecifications) {
        mOpeningHoursSpecifications = openingHoursSpecifications;
    }

    public GeoCoordinates getGeoCoordinates() {
        return mGeoCoordinates;
    }

    public void setGeoCoordinates(GeoCoordinates geoCoordinates) {
        mGeoCoordinates = geoCoordinates;
    }

    public List<ParkingCapacity> getCapacities() {
        return mCapacities;
    }

    public void setCapacities(List<ParkingCapacity> capacities) {
        mCapacities = capacities;
    }

    public void addCapacity(ParkingCapacity capacity) {
        mCapacities.add(capacity);
    }

    public List<ParkingSpace> getParkingSpaces() {
        return mParkingSpaces;
    }

    public void setParkingSpaces(List<ParkingSpace> parkingSpaces) {
        mParkingSpaces = parkingSpaces;
    }

    public void addParkingSpace(ParkingSpace parkingSpace) {
        mParkingSpaces.add(parkingSpace);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mIsOwnedBy);
        dest.writeParcelable(this.mOpeningHoursSpecifications, flags);
        dest.writeParcelable(this.mGeoCoordinates, flags);
        dest.writeTypedList(this.mCapacities);
        dest.writeList(this.mParkingSpaces);
    }

    protected ParkingFacility(Parcel in) {
        this.mId = in.readString();
        this.mIsOwnedBy = in.readString();
        this.mOpeningHoursSpecifications = in.readParcelable(OpeningHoursSpecifications.class.getClassLoader());
        this.mGeoCoordinates = in.readParcelable(GeoCoordinates.class.getClassLoader());
        this.mCapacities = in.createTypedArrayList(ParkingCapacity.CREATOR);
        this.mParkingSpaces = new ArrayList<ParkingSpace>();
        in.readList(this.mParkingSpaces, ParkingSpace.class.getClassLoader());
    }

    public static final Parcelable.Creator<ParkingFacility> CREATOR = new Parcelable.Creator<ParkingFacility>() {
        @Override
        public ParkingFacility createFromParcel(Parcel source) {
            return new ParkingFacility(source);
        }

        @Override
        public ParkingFacility[] newArray(int size) {
            return new ParkingFacility[size];
        }
    };
}
