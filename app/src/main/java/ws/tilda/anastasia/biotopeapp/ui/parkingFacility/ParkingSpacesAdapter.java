package ws.tilda.anastasia.biotopeapp.ui.parkingFacility;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ws.tilda.anastasia.biotopeapp.R;
import ws.tilda.anastasia.biotopeapp.objects.ParkingFacility;
import ws.tilda.anastasia.biotopeapp.objects.ParkingSpace;

public class ParkingSpacesAdapter extends RecyclerView.Adapter<ParkingSpacesAdapter.ViewHolder> {
    private List<ParkingSpace> mParkingSpaces;
    private ParkingSpacesFragment.OnListFragmentInteractionListener mListener;
    private ParkingFacility mParkingFacility;

    public ParkingSpacesAdapter(List<ParkingSpace> parkingSpaces,
                                ParkingSpacesFragment.OnListFragmentInteractionListener listener, ParkingFacility pf) {
        mParkingSpaces = parkingSpaces;
        mListener = listener;
        mParkingFacility = pf;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parking_spaces_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mParkingSpace = mParkingSpaces.get(position);

        holder.mParkingSpaceId.setText(holder.mParkingSpace.getId());

        setEvChargerAvailable(holder);
        setAvailable(holder);
        setDisabled(holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mParkingSpace, mParkingFacility);
                }
            }
        });
    }

    private void setEvChargerAvailable(ViewHolder holder) {
        if((holder.mParkingSpace.isHasEvCharging())) {
            holder.mChargerAvailable.setText(R.string.yes_available);
        } else {
            holder.mChargerAvailable.setText(R.string.no_available);

        }
    }

    private void setAvailable(ViewHolder holder) {
        if((holder.mParkingSpace.isAvailable())) {
            holder.mAvailable.setText(R.string.yes_available);
        } else {
            holder.mAvailable.setText(R.string.no_available);

        }
    }

    private void setDisabled(ViewHolder holder) {
        if(holder.mParkingSpace.isCapableForDisabled() == true) {
            holder.mCapableDisabled.setText(R.string.yes_available);
        } else {
            holder.mCapableDisabled.setText(R.string.no_available);

        }
    }

    @Override
    public int getItemCount() {
        if (mParkingSpaces != null) {
            return mParkingSpaces.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mParkingSpaceId;
        TextView mChargerAvailable;
        TextView mAvailable;
        TextView mCapableDisabled;

        private ParkingSpace mParkingSpace;


        public ViewHolder(View view) {
            super(view);
            mParkingSpaceId = view.findViewById(R.id.parking_space_id);
            mChargerAvailable = view.findViewById(R.id.EV_charger);
            mAvailable = view.findViewById(R.id.available);
            mCapableDisabled = view.findViewById(R.id.valid_for_disabled);
        }
    }
}
