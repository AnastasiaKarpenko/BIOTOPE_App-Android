package ws.tilda.anastasia.biotopeapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ParkingService implements Parcelable {
    private List<ParkingFacility> mParkingFacilities;

    public ParkingService() {
        mParkingFacilities = new ArrayList<>();
    }

    public List<ParkingFacility> getParkingFacilities() {
        return mParkingFacilities;
    }

    public void setParkingFacilities(List<ParkingFacility> parkingFacilities) {
        mParkingFacilities = parkingFacilities;
    }

    public void addParkingFacility(ParkingFacility parkingFacility) {
        mParkingFacilities.add(parkingFacility);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mParkingFacilities);
    }

    protected ParkingService(Parcel in) {
        this.mParkingFacilities = in.createTypedArrayList(ParkingFacility.CREATOR);
    }

    public static final Parcelable.Creator<ParkingService> CREATOR = new Parcelable.Creator<ParkingService>() {
        @Override
        public ParkingService createFromParcel(Parcel source) {
            return new ParkingService(source);
        }

        @Override
        public ParkingService[] newArray(int size) {
            return new ParkingService[size];
        }
    };
}
