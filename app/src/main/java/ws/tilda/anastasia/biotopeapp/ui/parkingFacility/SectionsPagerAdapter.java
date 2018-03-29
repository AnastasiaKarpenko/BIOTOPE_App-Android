package ws.tilda.anastasia.biotopeapp.ui.parkingFacility;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ws.tilda.anastasia.biotopeapp.objects.ParkingFacility;


public class SectionsPagerAdapter extends FragmentPagerAdapter {
    public static final int TABS_QUANTITY = 3;
    private ParkingFacility mParkingFacility;

    SectionsPagerAdapter(FragmentManager fm, ParkingFacility parkingFacility) {
        super(fm);
        mParkingFacility = parkingFacility;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ParkingFacilityLocationFragment.newInstance(mParkingFacility);
            case 1:
                return ParkingFacilityCapacityFragment.newInstance(mParkingFacility);
            case 2:
                return ParkingSpacesFragment.newInstance(mParkingFacility);
        }
        return ParkingFacilityLocationFragment.newInstance(mParkingFacility);
    }

    @Override
    public int getCount() {
        return TABS_QUANTITY;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "LOCATION";
            case 1:
                return "CAPACITY";
            case 2:
                return "PARKING SPACES";
        }
        return null;
    }
}