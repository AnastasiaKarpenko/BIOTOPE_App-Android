package ws.tilda.anastasia.biotopeapp.ui.parkingFacility;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ws.tilda.anastasia.biotopeapp.R;
import ws.tilda.anastasia.biotopeapp.objects.ParkingCapacity;


public class CapacityAdapter extends RecyclerView.Adapter<CapacityAdapter.ViewHolder> {
    private List<ParkingCapacity> mCapacities;

    public CapacityAdapter(List<ParkingCapacity> capacities) {
        mCapacities = capacities;
    }

    @Override
    public CapacityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_capacity_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CapacityAdapter.ViewHolder holder, int position) {
        if (mCapacities != null && !mCapacities.isEmpty()) {
            holder.mVehicleType.setText(mCapacities.get(position).getValidForVehicle());
            holder.mRealTimeValue.setText(mCapacities.get(position).getRealTimeValue());
            holder.mTotalNumber.setText(mCapacities.get(position).getMaximumValue());

            setValidForDisabled(holder, position);
        }
    }

    private void setValidForDisabled(ViewHolder holder, int position) {
        if (mCapacities.get(position).isValidForDisabled()) {
            holder.mValidForDisabled.setText(R.string.yes_valid);
        } else {
            holder.mValidForDisabled.setText(R.string.no_valid);
        }
    }

    @Override
    public int getItemCount() {
        if (mCapacities != null) {
            return mCapacities.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mVehicleType;
        TextView mRealTimeValue;
        TextView mTotalNumber;
        TextView mValidForDisabled;


        public ViewHolder(View view) {
            super(view);
            mVehicleType = view.findViewById(R.id.vehicle_type);
            mRealTimeValue = view.findViewById(R.id.real_time_value);
            mTotalNumber = view.findViewById(R.id.total_number);
            mValidForDisabled = view.findViewById(R.id.valid_for_disabled);
        }
    }

}
