package ws.tilda.anastasia.biotopeapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class ParkingSpace implements Parcelable {
    private String mId;
    private String mValidForVehicle;
    private boolean mIsAvailable;
    private double mVehicleWidthLimit;
    private double mVehicleHeightLimit;
    private double mVehicleLengthLimit;
    private boolean mCapableForDisabled;
    private boolean mHasEvCharging;
    private Charger charger;

    public ParkingSpace() {
        mHasEvCharging = false;
        mCapableForDisabled = false;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getValidForVehicle() {
        return mValidForVehicle;
    }

    public void setValidForVehicle(String validForVehicle) {
        mValidForVehicle = validForVehicle;
    }

    public boolean isAvailable() {
        return mIsAvailable;
    }

    public void setAvailable(boolean available) {
        mIsAvailable = available;
    }

    public double getVehicleWidthLimit() {
        return mVehicleWidthLimit;
    }

    public void setVehicleWidthLimit(double vehicleWidthLimit) {
        mVehicleWidthLimit = vehicleWidthLimit;
    }

    public double getVehicleHeightLimit() {
        return mVehicleHeightLimit;
    }

    public void setVehicleHeightLimit(double vehicleHeightLimit) {
        mVehicleHeightLimit = vehicleHeightLimit;
    }

    public double getVehicleLengthLimit() {
        return mVehicleLengthLimit;
    }

    public void setVehicleLengthLimit(double vehicleLengthLimit) {
        mVehicleLengthLimit = vehicleLengthLimit;
    }

    public boolean isCapableForDisabled() {
        return mCapableForDisabled;
    }

    public void setCapableForDisabled(boolean capableForDisabled) {
        mCapableForDisabled = capableForDisabled;
    }

    public boolean isHasEvCharging() {
        return mHasEvCharging;
    }

    public void setHasEvCharging(boolean hasEvCharging) {
        mHasEvCharging = hasEvCharging;
    }

    public Charger getCharger() {
        return charger;
    }

    public void setCharger(Charger charger) {
        this.charger = charger;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mValidForVehicle);
        dest.writeByte(this.mIsAvailable ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.mVehicleWidthLimit);
        dest.writeDouble(this.mVehicleHeightLimit);
        dest.writeDouble(this.mVehicleLengthLimit);
        dest.writeByte(this.mCapableForDisabled ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mHasEvCharging ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.charger, flags);
    }

    protected ParkingSpace(Parcel in) {
        this.mId = in.readString();
        this.mValidForVehicle = in.readString();
        this.mIsAvailable = in.readByte() != 0;
        this.mVehicleWidthLimit = in.readDouble();
        this.mVehicleHeightLimit = in.readDouble();
        this.mVehicleLengthLimit = in.readDouble();
        this.mCapableForDisabled = in.readByte() != 0;
        this.mHasEvCharging = in.readByte() != 0;
        this.charger = in.readParcelable(Charger.class.getClassLoader());
    }

    public static final Parcelable.Creator<ParkingSpace> CREATOR = new Parcelable.Creator<ParkingSpace>() {
        @Override
        public ParkingSpace createFromParcel(Parcel source) {
            return new ParkingSpace(source);
        }

        @Override
        public ParkingSpace[] newArray(int size) {
            return new ParkingSpace[size];
        }
    };
}
