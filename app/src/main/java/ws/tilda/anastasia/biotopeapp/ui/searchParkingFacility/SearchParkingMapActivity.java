package ws.tilda.anastasia.biotopeapp.ui.searchParkingFacility;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;

import ws.tilda.anastasia.biotopeapp.R;

public class SearchParkingMapActivity extends AppCompatActivity {

    private static final String APIPATH_ps = "http://veivi.parkkis.com:8080";
    public static final String APIPATH_iotbnb = "http://biotope.serval.uni.lu:8383/";

    public static final int REQUEST_ERROR = 0;
    private static final String TAG = "SearchParkingActivity";


    //Navigation Drawer variables
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private TextView mUserEmail;

    private Button mCarFilterButton;
    private Button mEvFilterButton;
    //    private Button mFastEvFilterButton;
    private Button mIotbnbFilterButton;

    private Location mLocation;
    public static final String LOCATION_KEY = "LOCATION_KEY";


    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_search_parking_map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        if (savedInstanceState != null) {
            if (savedInstanceState.getParcelable(LOCATION_KEY) == null) {
                return;
            } else {
                mLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }
        }

        initializingNavigDrawer();

        settingGooglePlacesSearchBar();

        mCarFilterButton = findViewById(R.id.filter_button_car);
        mEvFilterButton = findViewById(R.id.filter_button_ev);
//        mFastEvFilterButton = findViewById(R.id.filter_button_evfast);
        mIotbnbFilterButton = findViewById(R.id.filter_button_iotbnb);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = apiAvailability
                    .getErrorDialog(this, errorCode, REQUEST_ERROR,
                            new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    finish();
                                }
                            });

            errorDialog.show();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(LOCATION_KEY, mLocation);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void settingGooglePlacesSearchBar() {
        // Adding Google Places search bar to the activity
        SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_support_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng latLang = place.getLatLng();
                double latitude = latLang.latitude;
                double longitude = latLang.longitude;
                onFragmentInteraction(latitude, longitude);
                Log.i(TAG, "LatLong " + latitude + " " + longitude);

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }


    public void onFragmentInteraction(double latitude, double longitude) {
        mLocation = new Location("");
        mLocation.setLatitude(latitude);
        mLocation.setLongitude(longitude);

        findParking(mLocation);
    }

    private void findParking(final Location location) {
        final SearchParkingMapFragment parkingMapFragment = (SearchParkingMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mCarFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parkingMapFragment.findParkingLot(location, getString(R.string.query_find_carParkinglots), APIPATH_ps);
            }
        });

        mEvFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parkingMapFragment.findParkingLot(location, getString(R.string.query_find_evParkinglots), APIPATH_ps);
            }
        });

//        mFastEvFilterButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                parkingMapFragment.findParkingLot(location, getString(R.string.query_find_evFastChargeParkinglots), APIPATH_ps);
//            }
//        });

        mIotbnbFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parkingMapFragment.findNodeUri(location, getString(R.string.query_search_all_services_iotbnb),
                        APIPATH_iotbnb);
//                Toast.makeText(SearchParkingMapActivity.this, "Update needed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initializingNavigDrawer() {
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                mDrawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

//                    case R.id.account:
//                        Toast.makeText(getApplicationContext(), R.string.update_needed, Toast.LENGTH_SHORT).show();
//                        return true;
                    case R.id.logout:
//                        Toast.makeText(getApplicationContext(), R.string.update_needed, Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "onAuthStateChanged:signed_out");
//                        Intent intent = new Intent(getBaseContext(), EmailPasswordActivity.class);
//                        startActivity(intent);

//                        startActivity(new Intent(getBaseContext(), ParkingMapActivity.class));
//                        finish();

                    default:
                        Toast.makeText(getApplicationContext(), "Update needed", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        //Initializing Drawer Layout and ActionBarToggle
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(new ColorDrawable(Color.TRANSPARENT), GravityCompat.START);
        setupDrawer();
        mUserEmail = findViewById(R.id.email);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}