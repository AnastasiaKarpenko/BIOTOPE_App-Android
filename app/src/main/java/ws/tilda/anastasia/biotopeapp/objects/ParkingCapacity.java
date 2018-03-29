package ws.tilda.anastasia.biotopeapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class ParkingCapacity implements Parcelable {
    private String mId;
    private int mRealTimeValue;
    private int mMaximumValue;
    private String mValidForVehicle;
    private boolean mIsValidForDisabled;

    public ParkingCapacity() {
        mIsValidForDisabled = false;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getRealTimeValue() {
        return mRealTimeValue;
    }

    public void setRealTimeValue(int realTimeValue) {
        mRealTimeValue = realTimeValue;
    }

    public int getMaximumValue() {
        return mMaximumValue;
    }

    public void setMaximumValue(int maximumValue) {
        mMaximumValue = maximumValue;
    }

    public String getValidForVehicle() {
        return mValidForVehicle;
    }

    public void setValidForVehicle(String validForVehicle) {
        mValidForVehicle = validForVehicle;
    }

    public boolean isValidForDisabled() {
        return mIsValidForDisabled;
    }

    public void setValidForDisabled(boolean validForDisabled) {
        mIsValidForDisabled = validForDisabled;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeInt(this.mRealTimeValue);
        dest.writeInt(this.mMaximumValue);
        dest.writeString(this.mValidForVehicle);
        dest.writeByte(this.mIsValidForDisabled ? (byte) 1 : (byte) 0);
    }

    protected ParkingCapacity(Parcel in) {
        this.mId = in.readString();
        this.mRealTimeValue = in.readInt();
        this.mMaximumValue = in.readInt();
        this.mValidForVehicle = in.readString();
        this.mIsValidForDisabled = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ParkingCapacity> CREATOR = new Parcelable.Creator<ParkingCapacity>() {
        @Override
        public ParkingCapacity createFromParcel(Parcel source) {
            return new ParkingCapacity(source);
        }

        @Override
        public ParkingCapacity[] newArray(int size) {
            return new ParkingCapacity[size];
        }
    };
}
