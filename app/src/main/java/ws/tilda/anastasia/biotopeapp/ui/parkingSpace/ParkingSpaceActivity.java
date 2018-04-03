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

import java.util.Locale;

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


        setViews();

        ParkingSpace parkingSpace = getIntent().getParcelableExtra(PARKING_SPACE_EXTRA);

        if (parkingSpace != null) {
            setDataToViews(parkingSpace);
        }

    }

    private void setDataToViews(ParkingSpace ps) {
        setId(ps);
        setAvailable(ps);
        setIfHasEvcharging(ps);
        setIsValidForDisabled(ps);
        setVehicleType(ps);
        setVehicleWidth(ps);
        setVehicleLength(ps);
        setVehicleHeight(ps);

        if (ps.getCharger() != null) {
            setChargerId(ps);
            setIsChargerAvailable(ps);
            setChargerBrand(ps);
            setChargerModel(ps);
            setCurrentType(ps);
            setVoltage(ps);
            setCurrentInA(ps);
            setPower(ps);
            setIsFastChargeCapable(ps);
            setIsThreePhasedCurrentAvailable(ps);
        } else {
            mChargerId.setText(R.string.not_available_string);
            mIsChargerAvailable.setText(R.string.not_available_string);
            mChargerBrand.setText(R.string.not_available_string);
            mChargerModel.setText(R.string.not_available_string);
            mCurrentType.setText(R.string.not_available_string);
            mVoltage.setText(R.string.not_available_string);
            mCurrent.setText(R.string.not_available_string);
            mPower.setText(R.string.not_available_string);
            mIsFastChargeCapable.setText(R.string.not_available_string);
            mIsThreePhasedCurrentAvailable.setText(R.string.not_available_string);
        }
    }

    private void setIsThreePhasedCurrentAvailable(ParkingSpace ps) {
        if (ps.getCharger().isThreePhasedCurrentAvailable()) {
            mIsThreePhasedCurrentAvailable.setText(R.string.yes_available);
        } else {
            mIsThreePhasedCurrentAvailable.setText(R.string.no_available);
        }
    }

    private void setIsFastChargeCapable(ParkingSpace ps) {
        if (ps.getCharger().isFastChargeCapable()) {
            mIsFastChargeCapable.setText(R.string.yes_available);
        } else {
            mIsFastChargeCapable.setText(R.string.no_available);
        }
    }

    private void setPower(ParkingSpace ps) {
        if (ps.getCharger().getPowerInKW() != 0) {
            mPower.setText(Double.toString(ps.getCharger().getPowerInKW()));
        } else {
            mPower.setText(R.string.not_available_string);
        }
    }

    private void setCurrentInA(ParkingSpace ps) {
        if (ps.getCharger().getCurrentInA() != 0) {
            mCurrent.setText(Double.toString(ps.getCharger().getCurrentInA()));
        } else {
            mCurrent.setText(R.string.not_available_string);
        }
    }

    private void setVoltage(ParkingSpace ps) {
        if (ps.getCharger().getVoltageInV() != 0) {
            mVoltage.setText(Double.toString(ps.getCharger().getVoltageInV()));
        } else {
            mVoltage.setText(R.string.not_available_string);
        }
    }

    private void setCurrentType(ParkingSpace ps) {
        if (ps.getCharger().getCurrentType() != null) {
            mCurrentType.setText(ps.getCharger().getCurrentType());
        } else {
            mCurrentType.setText(R.string.not_available_string);
        }
    }

    private void setChargerModel(ParkingSpace ps) {
        if (ps.getCharger().getModel() != null) {
            mChargerModel.setText(ps.getCharger().getModel());
        } else {
            mChargerModel.setText(R.string.not_available_string);
        }
    }

    private void setChargerBrand(ParkingSpace ps) {
        if (ps.getCharger().getBrand() != null) {
            mChargerBrand.setText(ps.getCharger().getBrand());
        } else {
            mChargerBrand.setText(R.string.not_available_string);
        }
    }

    private void setIsChargerAvailable(ParkingSpace ps) {
        if (ps.getCharger().isAvailable()) {
            mIsChargerAvailable.setText(R.string.yes_available);
        } else {
            mIsChargerAvailable.setText(R.string.no_available);
        }
    }

    private void setChargerId(ParkingSpace ps) {
        if (ps.getCharger().getId() != null) {
            mChargerId.setText(ps.getCharger().getId());
        } else {
            mChargerId.setText(R.string.not_available_string);
        }
    }

    private void setVehicleHeight(ParkingSpace ps) {
        if (Double.toString(ps.getVehicleHeightLimit()) != null) {
            mVehicleHeightLimit.setText(Double.toString(ps.getVehicleHeightLimit()));
        } else {
            mVehicleHeightLimit.setText(R.string.not_available_string);
        }
    }

    private void setVehicleLength(ParkingSpace ps) {
        if (Double.toString(ps.getVehicleLengthLimit()) != null) {
            mVehicleLengthLimit.setText(Double.toString(ps.getVehicleLengthLimit()));
        } else {
            mVehicleLengthLimit.setText(R.string.not_available_string);
        }
    }

    private void setVehicleWidth(ParkingSpace ps) {
        if (Double.toString(ps.getVehicleWidthLimit()) != null) {
            mVehicleWidthLimit.setText(Double.toString(ps.getVehicleWidthLimit()));
        } else {
            mVehicleWidthLimit.setText(R.string.not_available_string);
        }
    }

    private void setVehicleType(ParkingSpace ps) {
        if (ps.getValidForVehicle() != null) {
            mVehicleType.setText(ps.getValidForVehicle());
        } else {
            mVehicleType.setText(R.string.not_available_string);
        }
    }

    private void setIsValidForDisabled(ParkingSpace ps) {
        if (ps.isCapableForDisabled()) {
            mIsForDisabled.setText(R.string.yes_valid);
        } else {
            mIsForDisabled.setText(R.string.no_valid);
        }
    }

    private void setIfHasEvcharging(ParkingSpace ps) {
        if (ps.isHasEvCharging()) {
            mhasEvCharger.setText(R.string.yes_available);
        } else {
            mhasEvCharger.setText(R.string.no_available);
        }
    }

    private void setAvailable(ParkingSpace ps) {
        if (ps.isAvailable()) {
            mIsAvailable.setText(R.string.yes_available);
        } else {
            mIsAvailable.setText(R.string.no_available);
        }
    }

    private void setId(ParkingSpace ps) {
        if (ps.getId() != null) {
            mParkingSpaceId.setText(ps.getId());
        } else {
            mParkingSpaceId.setText(R.string.not_known);
        }
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
