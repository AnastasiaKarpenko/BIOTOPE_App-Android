package ws.tilda.anastasia.biotopeapp.ui.parkingFacility;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import ws.tilda.anastasia.biotopeapp.R;
import ws.tilda.anastasia.biotopeapp.objects.ParkingCapacity;
import ws.tilda.anastasia.biotopeapp.objects.ParkingFacility;

public class ParkingFacilityCapacityFragment extends Fragment {
    public static final String PARKING_FACILITY_EXTRA = "PARKING_FACILITY_EXTRA";
    private ParkingFacility mParkingFacility;
    private Context mContext;
    private RecyclerView mCapacityRecyclerView;
    private List<ParkingCapacity> mCapacityList;


    public static ParkingFacilityCapacityFragment newInstance(ParkingFacility parkingFacility) {
        Bundle args = new Bundle();
        args.putParcelable(PARKING_FACILITY_EXTRA, parkingFacility);

        ParkingFacilityCapacityFragment parkingFacilityCapacityFragment = new ParkingFacilityCapacityFragment();
        parkingFacilityCapacityFragment.setArguments(args);

        return parkingFacilityCapacityFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParkingFacility = getArguments().getParcelable(PARKING_FACILITY_EXTRA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_parking_facility_capacity, container, false);

        if (savedInstanceState != null && savedInstanceState.containsKey(PARKING_FACILITY_EXTRA)) {
            mParkingFacility = savedInstanceState.getParcelable(PARKING_FACILITY_EXTRA);
        }

        mCapacityRecyclerView = v.findViewById(R.id.capacity_recyclerView);
        mCapacityList = mParkingFacility.getCapacities();

        if (mCapacityList !=null && !mCapacityList.isEmpty()) {
            mCapacityRecyclerView.setAdapter(new CapacityAdapter(mCapacityList));
        }


        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(PARKING_FACILITY_EXTRA, mParkingFacility);
        super.onSaveInstanceState(outState);
    }

}
