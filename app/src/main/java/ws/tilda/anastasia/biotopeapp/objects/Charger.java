package ws.tilda.anastasia.biotopeapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Charger implements Parcelable {
    private String mId;
    private String mBrand;
    private String mModel;
    private String mCurrentType;
    private boolean mIsThreePhasedCurrentAvailable;
    private boolean mIsAvailable;
    private double mVoltageInV;
    private double mCurrentInA;
    private double mPowerInKW;
    private boolean mIsFastChargeCapable;
    private List<Plug> mPlugs;

    public Charger() {
        mPlugs = new ArrayList<Plug>();
    }


    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getBrand() {
        return mBrand;
    }

    public void setBrand(String brand) {
        mBrand = brand;
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        mModel = model;
    }

    public String getCurrentType() {
        return mCurrentType;
    }

    public void setCurrentType(String currentType) {
        mCurrentType = currentType;
    }

    public boolean isThreePhasedCurrentAvailable() {
        return mIsThreePhasedCurrentAvailable;
    }

    public void setThreePhasedCurrentAvailable(boolean threePhasedCurrentAvailable) {
        mIsThreePhasedCurrentAvailable = threePhasedCurrentAvailable;
    }

    public boolean isAvailable() {
        return mIsAvailable;
    }

    public void setAvailable(boolean available) {
        mIsAvailable = available;
    }

    public double getVoltageInV() {
        return mVoltageInV;
    }

    public void setVoltageInV(double voltageInV) {
        mVoltageInV = voltageInV;
    }

    public double getCurrentInA() {
        return mCurrentInA;
    }

    public void setCurrentInA(double currentInA) {
        mCurrentInA = currentInA;
    }

    public double getPowerInKW() {
        return mPowerInKW;
    }

    public void setPowerInKW(double powerInKW) {
        mPowerInKW = powerInKW;
    }

    public boolean isFastChargeCapable() {
        return mIsFastChargeCapable;
    }

    public void setFastChargeCapable(boolean fastChargeCapable) {
        mIsFastChargeCapable = fastChargeCapable;
    }

    public List<Plug> getPlugs() {
        return mPlugs;
    }

    public void setPlugs(List<Plug> plugs) {
        mPlugs = plugs;
    }

    public void addPlug(Plug plug) {
        mPlugs.add(plug);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mBrand);
        dest.writeString(this.mModel);
        dest.writeString(this.mCurrentType);
        dest.writeByte(this.mIsThreePhasedCurrentAvailable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mIsAvailable ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.mVoltageInV);
        dest.writeDouble(this.mCurrentInA);
        dest.writeDouble(this.mPowerInKW);
        dest.writeByte(this.mIsFastChargeCapable ? (byte) 1 : (byte) 0);
        dest.writeList(this.mPlugs);
    }

    protected Charger(Parcel in) {
        this.mId = in.readString();
        this.mBrand = in.readString();
        this.mModel = in.readString();
        this.mCurrentType = in.readString();
        this.mIsThreePhasedCurrentAvailable = in.readByte() != 0;
        this.mIsAvailable = in.readByte() != 0;
        this.mVoltageInV = in.readDouble();
        this.mCurrentInA = in.readDouble();
        this.mPowerInKW = in.readDouble();
        this.mIsFastChargeCapable = in.readByte() != 0;
        this.mPlugs = new ArrayList<Plug>();
        in.readList(this.mPlugs, Plug.class.getClassLoader());
    }

    public static final Parcelable.Creator<Charger> CREATOR = new Parcelable.Creator<Charger>() {
        @Override
        public Charger createFromParcel(Parcel source) {
            return new Charger(source);
        }

        @Override
        public Charger[] newArray(int size) {
            return new Charger[size];
        }
    };
}
