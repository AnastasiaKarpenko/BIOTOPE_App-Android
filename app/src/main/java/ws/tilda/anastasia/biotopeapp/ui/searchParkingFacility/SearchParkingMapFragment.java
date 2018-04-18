package ws.tilda.anastasia.biotopeapp.ui.searchParkingFacility;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import ws.tilda.anastasia.biotopeapp.BiotopeApp;
import ws.tilda.anastasia.biotopeapp.R;
import ws.tilda.anastasia.biotopeapp.networking.ServiceGenerator;
import ws.tilda.anastasia.biotopeapp.objects.AvailableService;
import ws.tilda.anastasia.biotopeapp.objects.GeoCoordinates;
import ws.tilda.anastasia.biotopeapp.objects.ParkingFacility;
import ws.tilda.anastasia.biotopeapp.objects.ParkingService;
import ws.tilda.anastasia.biotopeapp.parsing.IotbnbParser;
import ws.tilda.anastasia.biotopeapp.parsing.XmlParser;
import ws.tilda.anastasia.biotopeapp.ui.parkingFacility.ParkingFacilityActivity;


public class SearchParkingMapFragment extends SupportMapFragment {
    public static final String TAG = "ChargerMapFragment";
    public static final int REQUEST_LOCATION_PERMISSIONS = 0;
    private static final String APIPATH_ps = "http://veivi.parkkis.com:8080";


    private static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };

    public static final String PARKING_FACILITY_EXTRA = "PARKING_FACILITY_EXTRA";

    private GoogleApiClient mClient;
    private GoogleMap mMap;

    private XmlParser xmlParser;
    private IotbnbParser iotbnbParser;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        xmlParser = new XmlParser();
        iotbnbParser = new IotbnbParser();

        mClient = getGoogleApiClient();

        getMapAsync();
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().invalidateOptionsMenu();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterForContextMenu(getView());

        mClient.disconnect();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSIONS:
                if (hasLocationPermission()) {

                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void getMapAsync() {
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        ParkingFacility parkingLot = (ParkingFacility) marker.getTag();
                        Intent intent = setIntent(parkingLot);

                        startActivity(intent);

                    }

                    @NonNull
                    private Intent setIntent(ParkingFacility parkingLot) {
                        Intent intent = new Intent(getActivity(), ParkingFacilityActivity.class);
                        intent.putExtra(PARKING_FACILITY_EXTRA, parkingLot);
                        return intent;
                    }
                });
            }
        });
    }

    @NonNull
    private GoogleApiClient getGoogleApiClient() {
        return new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        getActivity().invalidateOptionsMenu();
                        if (hasLocationPermission()) {
                            LocationRequest request = LocationRequest.create();
                            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                            request.setNumUpdates(1);
                            request.setInterval(0);

                            requestLocationUpdate(request);

                        } else {
                            requestPermissions(LOCATION_PERMISSIONS, REQUEST_LOCATION_PERMISSIONS);
                        }

                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .build();
    }

    private void requestLocationUpdate(LocationRequest request) {
        // Checking permissions, necessary to request location update
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        LocationServices.FusedLocationApi
                .requestLocationUpdates(mClient, request, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.i(TAG, "Got a fix: " + location);

                        CameraUpdate center =
                                CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),
                                        location.getLongitude()));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);

                        mMap.moveCamera(center);
                        mMap.animateCamera(zoom);
                    }
                });
    }

    public void findParkingLot(Location location, String query, String apiPath) {
        if (BiotopeApp.hasNetwork()) {
            new SearchParkingTask().execute(location, query, apiPath);
        } else {
            Toast.makeText(getContext(), R.string.no_network_connection_message, Toast.LENGTH_SHORT)
                    .show();
        }

    }

    public void findNodeUri(Location location, String query, String apiPath) {
        if (BiotopeApp.hasNetwork()) {
            new SearchIotbnbTask().execute(location, query, apiPath);
        } else {
            Toast.makeText(getContext(), R.string.no_network_connection_message, Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private boolean hasLocationPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSIONS[0]);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void updateUI(ParkingService ps) {
        if (mMap == null) {
            return;
        }
        mMap.clear();

        enableMyLocation();

        List<ParkingFacility> parkingLots = ps.getParkingFacilities();
        if (!parkingLots.isEmpty()) {
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            addAllMarkersToMap(parkingLots, boundsBuilder);
        } else {
            Toast.makeText(getContext(), R.string.no_available_parking_lot_toast,
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void enableMyLocation() {
        // Checking permissions, necessary to setMyLocationEnabled
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);
    }

    private void addAllMarkersToMap(List<ParkingFacility> parkingLots, LatLngBounds.Builder boundsBuilder) {
        for (ParkingFacility parkingLot : parkingLots) {
            GeoCoordinates position = parkingLot.getGeoCoordinates();
            int numberOfSpotsAvailable = 2;

            LatLng itemPoint = new LatLng(position.getLatitude(), position.getLongitude());


            Marker itemMarker = addMarkerToMap(itemPoint, getMarkerColor(numberOfSpotsAvailable));

//            String snippetTitle = getResources().getString(R.string.map_parking_snippet_title,
//                    parkingLot.getId());
            String snippetTitle = getString(R.string.EV_parking_lot_snippet_title);
            itemMarker.setTitle(snippetTitle);

            String snippetText = getResources().getString(R.string.click_to_see_details);
            itemMarker.setSnippet(snippetText);

            itemMarker.setTag(parkingLot);

            boundsBuilder.include(itemPoint);

            setZoom(boundsBuilder);
        }


    }

    private void setZoom(LatLngBounds.Builder boundsBuilder) {
        LatLngBounds bounds = boundsBuilder.build();
        int margin = 20;
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
        mMap.moveCamera(update);
    }


    private float getMarkerColor(int numberOfSpotsAvailable) {
        if (numberOfSpotsAvailable == 0) {
            return BitmapDescriptorFactory.HUE_RED;
        }
        return BitmapDescriptorFactory.HUE_GREEN;
    }

    private Marker addMarkerToMap(LatLng itemPoint, float colorMask) {
        MarkerOptions itemMarker = new MarkerOptions().position(itemPoint);
        itemMarker.icon(BitmapDescriptorFactory.defaultMarker(colorMask));

        return mMap.addMarker(itemMarker);
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

    private String parseNodeUri(InputStream stream) {
        String nodeUri = null;
        try {
            List<AvailableService> services = iotbnbParser.parse(stream).getAvailableServices();
            nodeUri = services.get(94).getOmiNodeUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nodeUri;
    }

    private class SearchParkingTask extends AsyncTask<Object, Object, ParkingService> {
        private ParkingService parkingService = new ParkingService();
        private String response;
        private String query;
        private String apiPath;


        @Override
        protected ParkingService doInBackground(Object... params) {
            Location location = (Location) params[0];
            query = (String) params[1];
            apiPath = (String) params[2];

            Call<String> call = callingApi(location, apiPath);
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


        private Call<String> callingApi(Location location, String apiPath) {
//            ApiClient.RetrofitService retrofitService = ApiClient.getApi();
            ServiceGenerator.changeApiBaseUrl(apiPath);
            ServiceGenerator.RetrofitService retrofitService = ServiceGenerator
                    .createService(ServiceGenerator.RetrofitService.class);

            return retrofitService.getResponse(getQueryFormattedString(location, query));

        }


        private String getQueryFormattedString(Location location, String query) {
            float desiredLatitude = (float) location.getLatitude();
            float desiredLongitude = (float) location.getLongitude();
            String formattedQuery = String.format(Locale.US,
                    query,
                    desiredLatitude,
                    desiredLongitude);

            return formattedQuery;
        }

        @Override
        protected void onPostExecute(ParkingService parkingService) {
            updateUI(parkingService);
        }
    }

    private class SearchIotbnbTask extends AsyncTask<Object, Object, String> {
        private String nodeUri = null;
        private String response;
        private String query;
        private String apiPath;
        private Location location;

        @Override
        protected String doInBackground(Object... params) {
            location = (Location) params[0];
            query = (String) params[1];
            apiPath = (String) params[2];

            Call<String> call = callingApi(location);
            InputStream stream = null;
            try {
                stream = new ByteArrayInputStream(getResponse(call).getBytes("UTF-8"));
                nodeUri = parseNodeUri(stream);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return nodeUri;
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


        private Call<String> callingApi(Location location) {
            ServiceGenerator.RetrofitService retrofitService = ServiceGenerator
                    .createService(ServiceGenerator.RetrofitService.class);
            return retrofitService.getResponse(getQueryFormattedString(location, query));
        }


        private String getQueryFormattedString(Location location, String query) {
            float desiredLatitude = (float) location.getLatitude();
            float desiredLongitude = (float) location.getLongitude();
            String formattedQuery = String.format(Locale.US,
                    query,
                    desiredLatitude,
                    desiredLongitude);

            return formattedQuery;
        }

        @Override
        protected void onPostExecute(String nodeUri) {
            findParkingLot(location, getString(R.string.query_find_evParkinglots), nodeUri);
            Toast.makeText(getContext(), nodeUri, Toast.LENGTH_SHORT).show();
        }
    }

}
