package ws.tilda.anastasia.biotopeapp.ui.parkingFacility;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ws.tilda.anastasia.biotopeapp.R;
import ws.tilda.anastasia.biotopeapp.objects.GeoCoordinates;
import ws.tilda.anastasia.biotopeapp.objects.ParkingFacility;

public class ParkingFacilityLocationFragment extends Fragment {

    public static final String PARKING_FACILITY_EXTRA = "PARKING_FACILITY_EXTRA";
    private ParkingFacility mParkingFacility;
    private GoogleMap mMap;
    private Context mContext;

    private MapView mMapView;
    private TextView mParkingFacilityIdTv;
    private TextView mOwnerTv;
    private TextView mOpeningHoursTv;
    private TextView mAddressTv;


    public static ParkingFacilityLocationFragment newInstance(ParkingFacility parkingFacility) {
        Bundle args = new Bundle();
        args.putParcelable(PARKING_FACILITY_EXTRA, parkingFacility);

        ParkingFacilityLocationFragment parkingFacilityLocationFragment = new ParkingFacilityLocationFragment();
        parkingFacilityLocationFragment.setArguments(args);

        return parkingFacilityLocationFragment;
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
        View v = inflater.inflate(R.layout.fragment_parking_facility_location, container, false);

        if (savedInstanceState != null && savedInstanceState.containsKey(PARKING_FACILITY_EXTRA)) {
            mParkingFacility = savedInstanceState.getParcelable(PARKING_FACILITY_EXTRA);
        }

        mMapView = v.findViewById(R.id.mapView);
        mParkingFacilityIdTv = v.findViewById(R.id.parkingFacility_id);
        mOwnerTv = v.findViewById(R.id.parkingFacility_owner);
        mOpeningHoursTv = v.findViewById(R.id.parkingFacility_opening_hours);
        mAddressTv = v.findViewById(R.id.parkingFacility_address);


        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                fillParkingFacilityInfo(mParkingFacility);
                mMap.getUiSettings().setScrollGesturesEnabled(false);
            }
        });


        return v;
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    private void fillParkingFacilityInfo(ParkingFacility parkingFacility) {
        setParkingFacilityId(parkingFacility);

        setParkingFacilityOwner(parkingFacility);

        setParkingFacilityOpeningHours(parkingFacility);

        setParkingFacilityLocation(parkingFacility);


        enableMyLocation();
        MapsInitializer.initialize(mContext);
        addMarkerToMapView(parkingFacility);
        setZoom(parkingFacility);

    }

    private void setParkingFacilityLocation(ParkingFacility parkingFacility) {
        if (parkingFacility.getGeoCoordinates().getAddress() != null) {
            mAddressTv.setText(parkingFacility.getGeoCoordinates().getAddress().toString());
        } else {
            mAddressTv.setText(R.string.not_available_string);
        }
    }

    private void setParkingFacilityOpeningHours(ParkingFacility parkingFacility) {
        if (parkingFacility.getOpeningHoursSpecifications() != null) {
            mOpeningHoursTv.setText(String.format(getString(R.string.parkingFacility_openning_hours_string),
                    parkingFacility.getOpeningHoursSpecifications().getOpens(),
                    parkingFacility.getOpeningHoursSpecifications().getCloses()));
        } else {
            mOpeningHoursTv.setText(R.string.not_available_string);
        }
    }

    private void setParkingFacilityOwner(ParkingFacility parkingFacility) {
        if (parkingFacility.getIsOwnedBy() != null) {
            mOwnerTv.setText(parkingFacility.getIsOwnedBy());
        } else {
            mOwnerTv.setText(R.string.not_available_string);
        }
    }

    private void setParkingFacilityId(ParkingFacility parkingFacility) {
        if (parkingFacility.getId() != null) {
            mParkingFacilityIdTv.setText(parkingFacility.getId());
        } else {
            mParkingFacilityIdTv.setText(R.string.not_available_string);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(PARKING_FACILITY_EXTRA, mParkingFacility);
        super.onSaveInstanceState(outState);
    }

    private void setZoom(ParkingFacility parkingLot) {
        LatLng latLng = getParkingFacilityLatLng(parkingLot);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.moveCamera(cameraUpdate);
    }

    @NonNull
    private void addMarkerToMapView(ParkingFacility parkingLot) {
        LatLng latLng = getParkingFacilityLatLng(parkingLot);

        MarkerOptions itemMarker = new MarkerOptions().position(latLng);
        itemMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mMap.addMarker(itemMarker);
    }

    private void enableMyLocation() {
        //Necessary permissions check
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);
    }

    @NonNull
    private LatLng getParkingFacilityLatLng(ParkingFacility parkingLot) {
        GeoCoordinates position = parkingLot.getGeoCoordinates();
        return new LatLng(position.getLatitude(), position.getLongitude());
    }

}
