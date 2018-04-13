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
import ws.tilda.anastasia.biotopeapp.objects.ParkingFacility;
import ws.tilda.anastasia.biotopeapp.objects.ParkingSpace;

public class ParkingSpacesFragment extends Fragment {
    public static final String PARKING_FACILITY_EXTRA = "PARKING_FACILITY_EXTRA";
    public static final String PARKING_FACILITY_ID_EXTRA = "PARKING_FACILITY_ID_EXTRA";
    private ParkingFacility mParkingFacility;
    private Context mContext;
    private RecyclerView mParkingSpacesRecyclerView;
    private List<ParkingSpace> mParkingSpaceList;
    private OnListFragmentInteractionListener mListener;


    public static ParkingSpacesFragment newInstance(ParkingFacility parkingFacility) {
        Bundle args = new Bundle();
        args.putParcelable(PARKING_FACILITY_EXTRA, parkingFacility);

        ParkingSpacesFragment parkingSpacesFragment = new ParkingSpacesFragment();
        parkingSpacesFragment.setArguments(args);

        return parkingSpacesFragment;
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
        View v = inflater.inflate(R.layout.fragment_parking_spaces, container, false);

        if (savedInstanceState != null && savedInstanceState.containsKey(PARKING_FACILITY_EXTRA)) {
            mParkingFacility = savedInstanceState.getParcelable(PARKING_FACILITY_EXTRA);
        }

        mParkingSpacesRecyclerView = v.findViewById(R.id.parking_spaces_recyclerView);
        mParkingSpaceList = mParkingFacility.getParkingSpaces();

        if (mParkingSpaceList != null && !mParkingSpaceList.isEmpty()) {
            setupAdapter(mParkingSpaceList);
        }


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setupAdapter(List<ParkingSpace> parkingSpaces) {
        if (isAdded()) {
            mParkingSpacesRecyclerView.setAdapter(new ParkingSpacesAdapter(parkingSpaces, mListener, mParkingFacility));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(PARKING_FACILITY_EXTRA, mParkingFacility);
        super.onSaveInstanceState(outState);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(ParkingSpace parkingSpace, ParkingFacility parkingFacility);
    }

}
