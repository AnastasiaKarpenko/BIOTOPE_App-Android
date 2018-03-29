package ws.tilda.anastasia.biotopeapp.ui.parkingSpace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ws.tilda.anastasia.biotopeapp.R;
import ws.tilda.anastasia.biotopeapp.objects.ParkingSpace;

public class ParkingSpaceActivity extends AppCompatActivity {
    public static final String PARKING_SPACE_EXTRA = "PARKING_SPACE_EXTRA";

    private TextView mParkingSpaceId;
    private TextView mIsAvailable;
    private TextView mhasEvCharger;
    private TextView mIsForDisabled;
    private TextView mVehicleType;
    private TextView mVehicleWidthLimit;
    private TextView mVehicleHeightLimit;
    private TextView mVehicleLengthLimit;
    private TextView mChargerId;
    private TextView mIsChargerAvailable;
    private TextView mChargerBrand;
    private TextView mChargerModel;
    private TextView mCurrentType;
    private TextView mVoltage;
    private TextView mCurrent;
    private TextView mPower;
    private TextView mIsFastChargeCapable;
    private TextView mIsThreePhasedCurrentAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_space);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setViews();

        ParkingSpace parkingSpace = getIntent().getParcelableExtra(PARKING_SPACE_EXTRA);

        if (parkingSpace != null) {
            setDataToViews(parkingSpace);
        } else {

        }

    }

    private void setDataToViews(ParkingSpace ps) {
        // link data to views
    }

    private void setViews() {
        mParkingSpaceId = findViewById(R.id.parking_space_id);
        mIsAvailable = findViewById(R.id.available);
        mhasEvCharger = findViewById(R.id.EV_charger);
        mIsForDisabled = findViewById(R.id.valid_for_disabled);
        mVehicleType = findViewById(R.id.vehicle_type);
        mVehicleWidthLimit = findViewById(R.id.vehicle_width_limit);
        mVehicleHeightLimit = findViewById(R.id.vehicle_height_limit);
        mVehicleLengthLimit = findViewById(R.id.vehicle_length_limit);
        mChargerId = findViewById(R.id.charger_id);
        mIsChargerAvailable = findViewById(R.id.charger_available);
        mChargerBrand = findViewById(R.id.charger_brand);
        mChargerModel = findViewById(R.id.charger_model);
        mCurrentType = findViewById(R.id.current_type);
        mVoltage = findViewById(R.id.voltage);
        mCurrent = findViewById(R.id.current);
        mPower = findViewById(R.id.power);
        mIsFastChargeCapable = findViewById(R.id.fast_charge);
        mIsThreePhasedCurrentAvailable = findViewById(R.id.three_phased_current);
    }

    public static void launch(Context context, ParkingSpace parkingSpace) {
        Intent intent = new Intent(context, ParkingSpaceActivity.class);
        intent.putExtra(PARKING_SPACE_EXTRA, parkingSpace);
        context.startActivity(intent);
    }
}
