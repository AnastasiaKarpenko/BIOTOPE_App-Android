package ws.tilda.anastasia.biotopeapp.ui.parkingFacility;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;

import ws.tilda.anastasia.biotopeapp.R;
import ws.tilda.anastasia.biotopeapp.objects.ParkingFacility;
import ws.tilda.anastasia.biotopeapp.objects.ParkingSpace;
import ws.tilda.anastasia.biotopeapp.ui.parkingSpace.ParkingSpaceActivity;


public class ParkingFacilityActivity extends AppCompatActivity implements ParkingSpacesFragment.OnListFragmentInteractionListener {
    private static final String PARKING_FACILITY_EXTRA = "PARKING_FACILITY_EXTRA";
    public static final String PARKING_SPACE_EXTRA = "PARKING_SPACE_EXTRA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_parking_facility);

        ParkingFacility parkingFacility = getIntent().getParcelableExtra(PARKING_FACILITY_EXTRA);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(parkingFacility.getId());
        }


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), parkingFacility);

        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
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
    public void onListFragmentInteraction(ParkingSpace parkingSpace) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARKING_SPACE_EXTRA, parkingSpace);
        ParkingSpaceActivity.launch(this, parkingSpace);

    }

}
