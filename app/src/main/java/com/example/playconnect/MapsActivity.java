package com.example.playconnect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class MapsActivity extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    MapView mMapView;
    private GoogleMap mMap;
    public SendMessage sendMessage;
    LocationManager locationManager;
    LocationListener locationListener;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_maps, container, false);
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    public void updateMap(Location location) {

        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14));
        mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));

    }





    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        StringBuilder sbValue = new StringBuilder(sbMethod());
        PlacesTask placesTask = new PlacesTask();
        placesTask.execute(sbValue.toString());
        mMap.setOnMarkerClickListener(this);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateMap(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastknownlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastknownlocation != null) {
                    updateMap(lastknownlocation);
                }
            }
        }
    }

    public StringBuilder sbMethod() {
        StringBuilder sb = null;
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        Location lastknownlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastknownlocation != null) {
            //use your current location here
            double mLatitude = lastknownlocation.getLatitude();
            double mLongitude = lastknownlocation.getLongitude();

            sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb.append("location=" + mLatitude + "," + mLongitude);
            sb.append("&radius=2000");
            sb.append("&types=" + "playground");
            sb.append("&sensor=true");

            sb.append("&key=AIzaSyBeyijrAR_Vf1WekbJtCiZ-XUi_SbBJxyM");

            Log.d("Map", "url: " + sb.toString());


        } else {
            Toast.makeText(getContext(), "Sorry your location could not be traced", Toast.LENGTH_LONG).show();
        }

        return sb;
    }


    private class PlacesTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) {
            String data = null;

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuffer sb = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                data = sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParserTask
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            Place_JSON placeJson = new Place_JSON();

            try {
                jObject = new JSONObject(jsonData[0]);

                places = placeJson.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            Log.d("Map", "list size: " + list.size());
            // Clears all the existing markers;


            for (int i = 0; i < list.size(); i++) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.draggable(false);


                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);

                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                String name = hmPlace.get("place_name");

                Log.d("Map", "place: " + name);

                // Getting vicinity
                String vicinity = hmPlace.get("vicinity");

                LatLng latLng = new LatLng(lat, lng);

                // Setting the position for the marker
                markerOptions.position(latLng);

                markerOptions.title(name + " : " + vicinity);

                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

                // Placing a marker on the touched position
                Marker m = mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

            }
        }

        public class Place_JSON {

            /**
             * Receives a JSONObject and returns a list
             */
            public List<HashMap<String, String>> parse(JSONObject jObject) {

                JSONArray jPlaces = null;
                try {
                    /** Retrieves all the elements in the 'places' array */
                    jPlaces = jObject.getJSONArray("results");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /** Invoking getPlaces with the array of json object
                 * where each json object represent a place
                 */
                return getPlaces(jPlaces);
            }

            private List<HashMap<String, String>> getPlaces(JSONArray jPlaces) {
                int placesCount = jPlaces.length();
                List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> place = null;

                /** Taking each place, parses and adds to list object */
                for (int i = 0; i < placesCount; i++) {
                    try {
                        /** Call getPlace with place JSON object to parse the place */
                        place = getPlace((JSONObject) jPlaces.get(i));
                        placesList.add(place);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return placesList;
            }

            /**
             * Parsing the Place JSON object
             */
            private HashMap<String, String> getPlace(JSONObject jPlace) {

                HashMap<String, String> place = new HashMap<String, String>();
                String placeName = "-NA-";
                String vicinity = "-NA-";
                String latitude = "";
                String longitude = "";
                String reference = "";

                try {
                    // Extracting Place name, if available
                    if (!jPlace.isNull("name")) {
                        placeName = jPlace.getString("name");
                    }

                    // Extracting Place Vicinity, if available
                    if (!jPlace.isNull("vicinity")) {
                        vicinity = jPlace.getString("vicinity");
                    }

                    latitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
                    longitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");
                    reference = jPlace.getString("reference");

                    place.put("place_name", placeName);
                    place.put("vicinity", vicinity);
                    place.put("lat", latitude);
                    place.put("lng", longitude);
                    place.put("reference", reference);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return place;
            }
        }

    }
    interface SendMessage {
        void sendData(String message);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            sendMessage = (SendMessage) getActivity();
        }
        catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1); //1 num of possible location returned
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();

            String title = address + "-" + city + "-" + state;
            Log.i("ADDRESS", address);

            Intent intent = new Intent(getContext(), MapsActivity.class);
            intent.putExtra("addressLine", address);
            getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
            getFragmentManager().popBackStack();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
