package ws.tilda.anastasia.biotopeapp.ui.parkingFacility;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import retrofit2.Call;
import ws.tilda.anastasia.biotopeapp.BiotopeApp;
import ws.tilda.anastasia.biotopeapp.R;
import ws.tilda.anastasia.biotopeapp.networking.ApiClient;
import ws.tilda.anastasia.biotopeapp.objects.ParkingFacility;
import ws.tilda.anastasia.biotopeapp.objects.ParkingService;
import ws.tilda.anastasia.biotopeapp.objects.ParkingSpace;
import ws.tilda.anastasia.biotopeapp.parsing.XmlParser;
import ws.tilda.anastasia.biotopeapp.ui.parkingSpace.ParkingSpaceActivity;


public class ParkingFacilityActivity extends AppCompatActivity implements ParkingSpacesFragment.OnListFragmentInteractionListener {
    private static final String PARKING_FACILITY_EXTRA = "PARKING_FACILITY_EXTRA";
    public static final String PARKING_SPACE_EXTRA = "PARKING_SPACE_EXTRA";
    public static final String TAG = "ParkingFacilityActivity";

    private XmlParser xmlParser = new XmlParser();
    private ParkingFacility mParkingfacility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_parking_facility);

        mParkingfacility = getIntent().getParcelableExtra(PARKING_FACILITY_EXTRA);
        String parkingFacilityId = mParkingfacility.getId();



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setTitle(parkingFacilityId);
            getSupportActionBar().setTitle("Parking Facility");

        }

        findParkingFacility(parkingFacilityId);

    }

    public void findParkingFacility(String id) {
        if (BiotopeApp.hasNetwork()) {
            new FetchParkingFacilityTask().execute(id);
        } else {
            Toast.makeText(this, R.string.no_network_connection_message, Toast.LENGTH_SHORT)
                    .show();
        }

    }

    public void updateUi(ParkingService ps) {
        if (!ps.getParkingFacilities().isEmpty()) {
            List<ParkingFacility> parkingFacilities = ps.getParkingFacilities();
            ParkingFacility pf = parkingFacilities.get(0);

            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), pf);
            ViewPager viewPager = findViewById(R.id.container);
            viewPager.setAdapter(sectionsPagerAdapter);
            TabLayout tabLayout = findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
        } else {
            Toast.makeText(this, R.string.no_info_available_toast, Toast.LENGTH_SHORT)
                    .show();
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

    @Override
    public void onListFragmentInteraction(ParkingSpace parkingSpace, ParkingFacility parkingFacility) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARKING_SPACE_EXTRA, parkingSpace);
        bundle.putParcelable(PARKING_FACILITY_EXTRA, parkingFacility);
        ParkingSpaceActivity.launch(this, parkingSpace, parkingFacility);

    }

    private ParkingService parseParkingService(InputStream stream) {
        ParkingService parkingService = new ParkingService();
        try {
            parkingService = xmlParser.parse(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parkingService;
    }

    private class FetchParkingFacilityTask extends AsyncTask<Object, Object, ParkingService> {
        private ParkingService parkingService = new ParkingService();
        private String response;


        @Override
        protected ParkingService doInBackground(Object... params) {
            String id = (String) params[0];

            Call<String> call = callingApi(id);
            InputStream stream = null;
            try {
                stream = new ByteArrayInputStream(getResponse(call).getBytes("UTF-8"));
                parkingService = parseParkingService(stream);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return parkingService;
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
                returnCode = xmlParser.parseReturnCode(stream);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return returnCode;
        }

        private Call<String> callingApi(String id) {
            ApiClient.RetrofitService retrofitService = ApiClient.getApi();
            return retrofitService.getResponse(getQueryFormattedString(id));

        }


        private String getQueryFormattedString(String id) {
            String formattedQuery = String.format(getString(R.string.query_find_parking_facility), id);
            return formattedQuery;
        }

        @Override
        protected void onPostExecute(ParkingService parkingService) {
            updateUi(parkingService);
        }
    }
}
