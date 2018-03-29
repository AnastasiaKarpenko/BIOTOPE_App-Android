package ws.tilda.anastasia.biotopeapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Plug implements Parcelable {
    private String mId;
    private double mCurrentInA;
    private double mPowerInKW;
    private String mCurrentType;
    private double mVoltageInV;
    private boolean mIsCableAvailable;
    private boolean mIsThreePhasedCurrentAvailable;
    private boolean mIsFastChargeCapable;
    private String mPlugType;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public double getCurrentInA() {
        return mCurrentInA;
    }

    public void setCurrentInA(double currentInA) {
        this.mCurrentInA = currentInA;
    }

    public double getPowerInKW() {
        return mPowerInKW;
    }

    public void setPowerInKW(double powerInKW) {
        this.mPowerInKW = powerInKW;
    }

    public String getCurrentType() {
        return mCurrentType;
    }

    public void setCurrentType(String currentType) {
        this.mCurrentType = currentType;
    }

    public double getVoltageInV() {
        return mVoltageInV;
    }

    public void setVoltageInV(double voltageInV) {
        this.mVoltageInV = voltageInV;
    }

    public boolean isCableAvailable() {
        return mIsCableAvailable;
    }

    public void setCableAvailable(boolean cableAvailable) {
        mIsCableAvailable = cableAvailable;
    }

    public boolean isThreePhasedCurrentAvailable() {
        return mIsThreePhasedCurrentAvailable;
    }

    public void setThreePhasedCurrentAvailable(boolean threePhasedCurrentAvailable) {
        mIsThreePhasedCurrentAvailable = threePhasedCurrentAvailable;
    }

    public boolean isFastChargeCapable() {
        return mIsFastChargeCapable;
    }

    public void setFastChargeCapable(boolean fastChargeCapable) {
        mIsFastChargeCapable = fastChargeCapable;
    }

    public String getPlugType() {
        return mPlugType;
    }

    public void setPlugType(String plugType) {
        mPlugType = plugType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeDouble(this.mCurrentInA);
        dest.writeDouble(this.mPowerInKW);
        dest.writeString(this.mCurrentType);
        dest.writeDouble(this.mVoltageInV);
        dest.writeByte(this.mIsCableAvailable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mIsThreePhasedCurrentAvailable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mIsFastChargeCapable ? (byte) 1 : (byte) 0);
        dest.writeString(this.mPlugType);
    }

    public Plug() {
    }

    protected Plug(Parcel in) {
        this.mId = in.readString();
        this.mCurrentInA = in.readDouble();
        this.mPowerInKW = in.readDouble();
        this.mCurrentType = in.readString();
        this.mVoltageInV = in.readDouble();
        this.mIsCableAvailable = in.readByte() != 0;
        this.mIsThreePhasedCurrentAvailable = in.readByte() != 0;
        this.mIsFastChargeCapable = in.readByte() != 0;
        this.mPlugType = in.readString();
    }

    public static final Parcelable.Creator<Plug> CREATOR = new Parcelable.Creator<Plug>() {
        @Override
        public Plug createFromParcel(Parcel source) {
            return new Plug(source);
        }

        @Override
        public Plug[] newArray(int size) {
            return new Plug[size];
        }
    };
}
