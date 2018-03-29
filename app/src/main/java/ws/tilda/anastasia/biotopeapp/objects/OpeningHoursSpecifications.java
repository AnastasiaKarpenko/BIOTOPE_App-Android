package ws.tilda.anastasia.biotopeapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class OpeningHoursSpecifications implements Parcelable {
    private String mOpens;
    private String mCloses;
    private String mDayOfWeek;

    public String getOpens() {
        return mOpens;
    }

    public void setOpens(String opens) {
        mOpens = opens;
    }

    public String getCloses() {
        return mCloses;
    }

    public void setCloses(String closes) {
        mCloses = closes;
    }

    public String getDayOfWeek() {
        return mDayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        mDayOfWeek = dayOfWeek;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mOpens);
        dest.writeString(this.mCloses);
        dest.writeString(this.mDayOfWeek);
    }

    public OpeningHoursSpecifications() {
    }

    protected OpeningHoursSpecifications(Parcel in) {
        this.mOpens = in.readString();
        this.mCloses = in.readString();
        this.mDayOfWeek = in.readString();
    }

    public static final Parcelable.Creator<OpeningHoursSpecifications> CREATOR = new Parcelable.Creator<OpeningHoursSpecifications>() {
        @Override
        public OpeningHoursSpecifications createFromParcel(Parcel source) {
            return new OpeningHoursSpecifications(source);
        }

        @Override
        public OpeningHoursSpecifications[] newArray(int size) {
            return new OpeningHoursSpecifications[size];
        }
    };
}
