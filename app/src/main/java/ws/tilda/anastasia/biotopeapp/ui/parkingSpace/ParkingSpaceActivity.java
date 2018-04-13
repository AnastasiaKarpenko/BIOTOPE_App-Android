package ws.tilda.anastasia.biotopeapp.ui.parkingSpace;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import ws.tilda.anastasia.biotopeapp.BiotopeApp;
import ws.tilda.anastasia.biotopeapp.R;
import ws.tilda.anastasia.biotopeapp.networking.ApiClient;
import ws.tilda.anastasia.biotopeapp.objects.Charger;
import ws.tilda.anastasia.biotopeapp.objects.ParkingFacility;
import ws.tilda.anastasia.biotopeapp.objects.ParkingSpace;
import ws.tilda.anastasia.biotopeapp.parsing.XmlParser;
import ws.tilda.anastasia.biotopeapp.ui.searchParkingFacility.SearchParkingMapFragment;

public class ParkingSpaceActivity extends AppCompatActivity {
    public static final String PARKING_SPACE_EXTRA = "PARKING_SPACE_EXTRA";
    private static final String PARKING_FACILITY_EXTRA = "PARKING_FACILITY_EXTRA";


    private TextView mParkingSpaceIdTv;
    private TextView mIsAvailableTv;
    private TextView mHasEvChargerTv;
    private TextView mIsForDisabledTv;
    private TextView mVehicleTypeTv;
    private TextView mVehicleWidthLimitTv;
    private TextView mVehicleHeightLimitTv;
    private TextView mVehicleLengthLimitTv;
    private TextView mChargerIdTv;
    private TextView mIsChargerAvailableTv;
    private TextView mChargerBrandTv;
    private TextView mChargerModelTv;
    private TextView mCurrentTypeTv;
    private TextView mVoltageTv;
    private TextView mCurrentTv;
    private TextView mPowerTv;
    private TextView mIsFastChargeCapableTv;
    private TextView mIsThreePhasedCurrentAvailableTv;
    private Button mStartChargingButton;

    public static final String TAG = "ParkingSpaceActivity";

    private XmlParser mXmlParser = new XmlParser();
    private ParkingFacility mParkingFacility;
    private String mParkingFacilityId;
    private ParkingSpace mParkingSpace;
    private String mParkingSpaceId;
    private String mChargerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_space);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setViews();

        mParkingFacility = getIntent().getParcelableExtra(PARKING_FACILITY_EXTRA);
        if(!mParkingFacility.equals(null)) {
            mParkingFacilityId = mParkingFacility.getId();
        } else {
            Toast.makeText(this, R.string.smth_got_wrong_toast, Toast.LENGTH_SHORT).show();
        }
        mParkingSpace = getIntent().getParcelableExtra(PARKING_SPACE_EXTRA);
        if(!mParkingSpace.equals(null)) {
            mParkingSpaceId = mParkingSpace.getId();
            mChargerId = mParkingSpace.getCharger().getId();
        } else {
            Toast.makeText(this, R.string.smth_got_wrong_toast, Toast.LENGTH_SHORT).show();
        }



        mStartChargingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCharging(mParkingFacilityId, mParkingSpaceId, mChargerId);
            }
        });

        if (mParkingSpace != null) {
            setDataToViews(mParkingSpace);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mParkingSpace.getId());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setDataToViews(ParkingSpace ps) {
        Charger charger = ps.getCharger();
        setId(ps);
        setAvailable(ps);
        setIfHasEvcharging(ps);
        setIsValidForDisabled(ps);
        setVehicleTypeTv(ps);
        setVehicleWidth(ps);
        setVehicleLength(ps);
        setVehicleHeight(ps);

        if (charger != null) {
            setChargerDataToViews(charger);
        } else {
            setNotAvailableToChargerViews();
        }
    }

    private void setNotAvailableToChargerViews() {
        mChargerIdTv.setText(R.string.not_available_string);
        mIsChargerAvailableTv.setText(R.string.not_available_string);
        mChargerBrandTv.setText(R.string.not_available_string);
        mChargerModelTv.setText(R.string.not_available_string);
        mCurrentTypeTv.setText(R.string.not_available_string);
        mVoltageTv.setText(R.string.not_available_string);
        mCurrentTv.setText(R.string.not_available_string);
        mPowerTv.setText(R.string.not_available_string);
        mIsFastChargeCapableTv.setText(R.string.not_available_string);
        mIsThreePhasedCurrentAvailableTv.setText(R.string.not_available_string);
        mStartChargingButton.setEnabled(false);
    }

    private void setChargerDataToViews(Charger charger) {
        setChargerIdTv(charger);
        setIsChargerAvailableTv(charger);
        setChargerBrandTv(charger);
        setChargerModelTv(charger);
        setCurrentTypeTv(charger);
        setVoltageTv(charger);
        setCurrentInA(charger);
        setPowerTv(charger);
        setIsFastChargeCapableTv(charger);
        setIsThreePhasedCurrentAvailableTv(charger);
        setChargingButton(charger);
    }

    private void setChargingButton(Charger charger) {
        if (charger.isAvailable()) {
            mStartChargingButton.setEnabled(true);
        } else if (!charger.isAvailable()) {
            mStartChargingButton.setEnabled(false);
        }
    }

    private void updateUi(String returnCode) {
        if (returnCode.equals("200")) {
            Log.d(TAG, "Return code is: " + returnCode);
            Toast.makeText(this, R.string.charging_enabled_toast, Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "Return code is: " + returnCode);
            Toast.makeText(this, R.string.smth_got_wrong_toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void setIsThreePhasedCurrentAvailableTv(Charger charger) {
        if (charger.isThreePhasedCurrentAvailable()) {
            mIsThreePhasedCurrentAvailableTv.setText(R.string.yes_available);
        } else {
            mIsThreePhasedCurrentAvailableTv.setText(R.string.no_available);
        }
    }

    private void setIsFastChargeCapableTv(Charger charger) {
        if (charger.isFastChargeCapable()) {
            mIsFastChargeCapableTv.setText(R.string.yes_available);
        } else {
            mIsFastChargeCapableTv.setText(R.string.no_available);
        }
    }

    private void setPowerTv(Charger charger) {
        if (charger.getPowerInKW() != 0) {
            mPowerTv.setText(Double.toString(charger.getPowerInKW()));
        } else {
            mPowerTv.setText(R.string.not_available_string);
        }
    }

    private void setCurrentInA(Charger charger) {
        if (charger.getCurrentInA() != 0) {
            mCurrentTv.setText(Double.toString(charger.getCurrentInA()));
        } else {
            mCurrentTv.setText(R.string.not_available_string);
        }
    }

    private void setVoltageTv(Charger charger) {
        if (charger.getVoltageInV() != 0) {
            mVoltageTv.setText(Double.toString(charger.getVoltageInV()));
        } else {
            mVoltageTv.setText(R.string.not_available_string);
        }
    }

    private void setCurrentTypeTv(Charger charger) {
        if (charger.getCurrentType() != null) {
            mCurrentTypeTv.setText(charger.getCurrentType());
        } else {
            mCurrentTypeTv.setText(R.string.not_available_string);
        }
    }

    private void setChargerModelTv(Charger charger) {
        if (charger.getModel() != null) {
            mChargerModelTv.setText(charger.getModel());
        } else {
            mChargerModelTv.setText(R.string.not_available_string);
        }
    }

    private void setChargerBrandTv(Charger charger) {
        if (charger.getBrand() != null) {
            mChargerBrandTv.setText(charger.getBrand());
        } else {
            mChargerBrandTv.setText(R.string.not_available_string);
        }
    }

    private void setIsChargerAvailableTv(Charger charger) {
        if (charger.isAvailable()) {
            mIsChargerAvailableTv.setText(R.string.yes_available);
        } else {
            mIsChargerAvailableTv.setText(R.string.no_available);
        }
    }

    private void setChargerIdTv(Charger charger) {
        if (charger.getId() != null) {
            mChargerIdTv.setText(charger.getId());
        } else {
            mChargerIdTv.setText(R.string.not_available_string);
        }
    }

    private void setVehicleHeight(ParkingSpace ps) {
        if (Double.toString(ps.getVehicleHeightLimit()) != null) {
            mVehicleHeightLimitTv.setText(Double.toString(ps.getVehicleHeightLimit()));
        } else {
            mVehicleHeightLimitTv.setText(R.string.not_available_string);
        }
    }

    private void setVehicleLength(ParkingSpace ps) {
        if (Double.toString(ps.getVehicleLengthLimit()) != null) {
            mVehicleLengthLimitTv.setText(Double.toString(ps.getVehicleLengthLimit()));
        } else {
            mVehicleLengthLimitTv.setText(R.string.not_available_string);
        }
    }

    private void setVehicleWidth(ParkingSpace ps) {
        if (Double.toString(ps.getVehicleWidthLimit()) != null) {
            mVehicleWidthLimitTv.setText(Double.toString(ps.getVehicleWidthLimit()));
        } else {
            mVehicleWidthLimitTv.setText(R.string.not_available_string);
        }
    }

    private void setVehicleTypeTv(ParkingSpace ps) {
        if (ps.getValidForVehicle() != null) {
            mVehicleTypeTv.setText(ps.getValidForVehicle());
        } else {
            mVehicleTypeTv.setText(R.string.not_available_string);
        }
    }

    private void setIsValidForDisabled(ParkingSpace ps) {
        if (ps.isCapableForDisabled()) {
            mIsForDisabledTv.setText(R.string.yes_valid);
        } else {
            mIsForDisabledTv.setText(R.string.no_valid);
        }
    }

    private void setIfHasEvcharging(ParkingSpace ps) {
        if (ps.isHasEvCharging()) {
            mHasEvChargerTv.setText(R.string.yes_available);
        } else {
            mHasEvChargerTv.setText(R.string.no_available);
        }
    }

    private void setAvailable(ParkingSpace ps) {
        if (ps.isAvailable()) {
            mIsAvailableTv.setText(R.string.yes_available);
        } else {
            mIsAvailableTv.setText(R.string.no_available);
        }
    }

    private void setId(ParkingSpace ps) {
        if (ps.getId() != null) {
            mParkingSpaceIdTv.setText(ps.getId());
        } else {
            mParkingSpaceIdTv.setText(R.string.not_known);
        }
    }

    private void setViews() {
        mParkingSpaceIdTv = findViewById(R.id.parking_space_id);
        mIsAvailableTv = findViewById(R.id.available);
        mHasEvChargerTv = findViewById(R.id.EV_charger);
        mIsForDisabledTv = findViewById(R.id.valid_for_disabled);
        mVehicleTypeTv = findViewById(R.id.vehicle_type);
        mVehicleWidthLimitTv = findViewById(R.id.vehicle_width_limit);
        mVehicleHeightLimitTv = findViewById(R.id.vehicle_height_limit);
        mVehicleLengthLimitTv = findViewById(R.id.vehicle_length_limit);
        mChargerIdTv = findViewById(R.id.charger_id);
        mIsChargerAvailableTv = findViewById(R.id.charger_available);
        mChargerBrandTv = findViewById(R.id.charger_brand);
        mChargerModelTv = findViewById(R.id.charger_model);
        mCurrentTypeTv = findViewById(R.id.current_type);
        mVoltageTv = findViewById(R.id.voltage);
        mCurrentTv = findViewById(R.id.current);
        mPowerTv = findViewById(R.id.power);
        mIsFastChargeCapableTv = findViewById(R.id.fast_charge);
        mIsThreePhasedCurrentAvailableTv = findViewById(R.id.three_phased_current);
        mStartChargingButton = findViewById(R.id.start_charging_button);
    }

    public static void launch(Context context, ParkingSpace parkingSpace, ParkingFacility parkingFacility) {
        Intent intent = new Intent(context, ParkingSpaceActivity.class);
        intent.putExtra(PARKING_SPACE_EXTRA, parkingSpace);
        intent.putExtra(PARKING_FACILITY_EXTRA, parkingFacility);

        context.startActivity(intent);
    }

    public void startCharging(String parkingFacilityId, String parkingSpaceId, String chargerId) {
        if (BiotopeApp.hasNetwork()) {
            new StartChargingTask().execute(parkingFacilityId, parkingSpaceId, chargerId);
        } else {
            Toast.makeText(this, R.string.no_network_connection_message, Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private class StartChargingTask extends AsyncTask<Object, Object, String> {
        private String returnCode = null;
        private String response;


        @Override
        protected String doInBackground(Object... params) {
            String parkingFacilityid = (String) params[0];
            String parkingSpaceId = (String) params[1];
            String chargerId = (String) params[2];


            Call<String> call = callingApi(parkingFacilityid, parkingSpaceId, chargerId);
            InputStream stream = null;
            try {
                stream = new ByteArrayInputStream(getResponse(call).getBytes("UTF-8"));
                returnCode = getReturnCode(stream);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return returnCode;
        }

        private String getResponse(Call<String> call) {
            try {
                String getResponse = call.execute().body();
                if (getResponse == null) {
                    Log.e(TAG, "Response is null");
                } else {
                    response = getResponse;
                }
            } catch (IOException e) {
                e.getMessage();
            }

            return response;
        }

        private String getReturnCode(InputStream stream) {
            String returnCode = null;
            try {
                returnCode = mXmlParser.parseReturnCode(stream);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return returnCode;
        }

        private Call<String> callingApi(String parkingFacilityId, String parkingSpaceId, String chargerId) {
            ApiClient.RetrofitService retrofitService = ApiClient.getApi();
            return retrofitService.getResponse(getQueryFormattedString(parkingFacilityId, parkingSpaceId, chargerId));

        }


        private String getQueryFormattedString(String parkingFacilityId, String parkingSpaceId, String chargerId) {
            String formattedQuery = String.format(getString(R.string.query_start_charging),
                    parkingSpaceId,
                    parkingFacilityId,
                    chargerId);
            return formattedQuery;
        }

        @Override
        protected void onPostExecute(String returnCode) {
            updateUi(returnCode);
        }
    }


}
