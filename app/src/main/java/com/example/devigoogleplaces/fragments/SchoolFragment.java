package com.example.devigoogleplaces.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.devigoogleplaces.R;
import com.example.devigoogleplaces.bean.MyPlaces;
import com.example.devigoogleplaces.bean.Result;
import com.example.devigoogleplaces.utils.ConnectionDetector;
import com.example.devigoogleplaces.utils.OnResponseListner;
import com.example.devigoogleplaces.utils.Prefs;
import com.example.devigoogleplaces.utils.SnackBar;
import com.example.devigoogleplaces.utils.WebServices;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

public class SchoolFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, OnResponseListner {

    GoogleMap mmMap;
    private GoogleApiClient mGoogleApiClient;
    double lat;
    double lang;
    ConnectionDetector mDetector;
    int radius = 1000;
    String sensor = "true";
    String Key = "AIzaSyCVLkqzBHAslzk5vxGzXuGQtrKrzZXU4gk";
    MyPlaces myPlaces;
    TextView tv_place;
    private MarkerOptions place1, place2;

    FusedLocationProviderClient fusedLocationProviderClient;
    Geocoder geocoder;
    boolean GpsStatus;
    private static final int REQUEST_CODE = 101;
    String city = "";
    int AUTOCOMPLETE_REQUEST_CODE = 0;
    Location currentLocation;
    List<Address> addresses;
    PlacesClient placesClient;
    String lati;
    String longi;
    private Marker currentLocationMarker;
    private List<Result> results;
    Dialog dialogSuccess;
    MapView map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hospital, container, false);
        map = (MapView) view.findViewById(R.id.map);
        map.onCreate(savedInstanceState);

        map.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.getMapAsync(this);
        tv_place = view.findViewById(R.id.tv_place);
        tv_place.setCompoundDrawablesWithIntrinsicBounds(R.drawable.location, 0, 0, 0);
        setValues();
        return view;
    }

    private void setValues() {
        mDetector = new ConnectionDetector(getActivity());
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        //mMap Current Location Work
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (Prefs.getUserLat(getActivity()).equals("")) {
            fetchLastLocation();
        } else {
            try {
                lat = Double.parseDouble(Prefs.getUserLat(getActivity()));
                lang = Double.parseDouble(Prefs.getUserLang(getActivity()));
                addresses = geocoder.getFromLocation(lat, lang, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                tv_place.setText(address);
                String location = lat+","+lang;
                String type = "school";
                CallService(location,type);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        tv_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Places.initialize(getActivity().getApplicationContext(), "AIzaSyB_RBQ5Le6REjSn35XB_f7_ufQmzETVOYY");
                // Create a new Places client instance.
                placesClient = Places.createClient(getActivity());


                final List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields)
                        .build(getActivity());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
    }

    private void CallService(String location,String type) {
        if (this.mDetector.isConnectingToInternet()) {
            new WebServices(this).GoogleResponce("https://maps.googleapis.com/maps/api/place/nearbysearch/",
                    WebServices.ApiType.googleresponce,location,radius,type,sensor,Key);
        } else {
            SnackBar.makeText(getActivity(), "No internet connectivity", SnackBar.LENGTH_SHORT).show();
        }
        return;
    }


    @Override
    public void onResponse(Object response, WebServices.ApiType apiType, boolean isSucces) {

        if (apiType == WebServices.ApiType.googleresponce) {
            if (!isSucces) {
                SnackBar.makeText(getActivity(), getResources().getString(R.string.something_wrong), SnackBar.LENGTH_SHORT).show();
            } else {
                myPlaces = (MyPlaces) response;

                if (!isSucces || myPlaces == null) {
                    SnackBar.makeText(getActivity(), getResources().getString(R.string.something_wrong), SnackBar.LENGTH_SHORT).show();
                }  if (myPlaces == null) {
                    SnackBar.makeText(getActivity(), "No results available", SnackBar.LENGTH_SHORT).show();
                }else {
                    results = new ArrayList<>();
                    for(int i = 0 ; i < myPlaces.getResults().size() ; i++) {
                        String snpt = "";
                        if(myPlaces.getResults().get(i).getPhotos() != null){
                            snpt = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+myPlaces.getResults().get(i).getPhotos().get(0).getPhotoReference()
                                    +"&key=AIzaSyCVLkqzBHAslzk5vxGzXuGQtrKrzZXU4gk";
                        }
                        createMarker(myPlaces.getResults().get(i).getGeometry().getLocation().getLat(),
                                myPlaces.getResults().get(i).getGeometry().getLocation().getLng(), myPlaces.getResults().get(i).getName(),
                                snpt, R.drawable.student_icon, mmMap);

                    }

                }
            }
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                //getPlace(place.getId());
                lat = place.getLatLng().latitude;
                lang = place.getLatLng().longitude;
                try {
                    addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    Prefs.setUserLat(getActivity(), String.valueOf(lat));
                    Prefs.setUserLang(getActivity(), String.valueOf(lang));
                    tv_place.setText(address);
                    String location = lat+","+lang;
                    String type = "school";

                    LatLng latLng = new LatLng(lat, lang);
                    mmMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), 17.0f));
                    mmMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,0,0,0)));
                    mmMap.setMapType(1);
                    //mMap.setmMapStyle(mMapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.mMap_in_night));
                    if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                        mmMap.getUiSettings().setZoomControlsEnabled(true);
//            mMap.getUiSettings().set
                        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                        Location location_ = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
                        if (location_ != null) {
                            mmMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), 17.0f));
                            mmMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(lat,lang)).zoom(17.0f).build()));
                        }
                        //createMarker(lat, lang, "", "", R.drawable.hospital_icon, mmMap);
                    }

                    CallService(location,type);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("AddAddress", "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.e("AddAddress", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void fetchLastLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                return;
            } else {
                Task<Location> task = fusedLocationProviderClient.getLastLocation();
                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            try {
                                currentLocation = location;
                                lat = currentLocation.getLatitude();
                                lang = currentLocation.getLongitude();
                                lati = String.valueOf(lat);
                                longi = String.valueOf(lang);
                                Prefs.setUserLat(getActivity(), lati);
                                Prefs.setUserLang(getActivity(), longi);
                                addresses = geocoder.getFromLocation(lat, lang, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                city = addresses.get(0).getLocality();
                                tv_place.setText(address);
                                String locations = lat+","+lang;
                                String type = "school";
                                CallService(locations,type);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

        } else {
            GPSStatus();
        }
    }

    public void GPSStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (GpsStatus) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        try {
                            currentLocation = location;
                            lat = currentLocation.getLatitude();
                            lang = currentLocation.getLongitude();
                            lati = String.valueOf(lat);
                            longi = String.valueOf(lang);
                            Prefs.setUserLat(getActivity(), lati);
                            Prefs.setUserLang(getActivity(), longi);
                            addresses = geocoder.getFromLocation(lat, lang, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            city = addresses.get(0).getLocality();
                            tv_place.setText(address);
                            String locations = lat+","+lang;
                            String type = "school";
                            CallService(locations,type);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            new AlertDialog.Builder(getActivity()).setTitle("Location Permission").setMessage("Please enable location permission!")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent1);
                            dialog.dismiss();
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {

        mmMap = googleMap;
        mmMap.setOnMarkerClickListener(this);
        LatLng latLng = new LatLng(lat, lang);
        mmMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), 17.0f));
        mmMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,0,0,0)));
        mmMap.setMapType(1);
        //mMap.setmMapStyle(mMapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.mMap_in_night));
        if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            mmMap.getUiSettings().setZoomControlsEnabled(true);
//            mMap.getUiSettings().set
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
            if (location != null) {
                mmMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17.0f));
                mmMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(17.0f).build()));
            }
            createMarker(lat, lang, "", "", R.drawable.student_icon, mmMap);
        }
    }
    protected Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID, GoogleMap map) {

        return map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromResource(iconResID)));

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        String url = marker.getSnippet();
        String name = marker.getTitle();
        double lat = marker.getPosition().latitude;
        double lon = marker.getPosition().longitude;
        Initializepopupmarks();
        initializdeletePopupMarks(url,name,lat,lon);
        return false;
    }

    private void Initializepopupmarks() {
        dialogSuccess = new Dialog(getActivity());
        dialogSuccess.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSuccess.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogSuccess.setContentView(R.layout.popup_promocode);
        dialogSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSuccess.setCancelable(false);
        dialogSuccess.setCanceledOnTouchOutside(false);
    }

    private void initializdeletePopupMarks(String url, final String name, final double lat, final double lon) {
        dialogSuccess.setContentView(R.layout.popup_promocode);
        dialogSuccess.setCancelable(false);
        dialogSuccess.setCanceledOnTouchOutside(false);
        dialogSuccess.show();

        final String title = name;
        final double latitt = lat;
        final double longit = lon;
        LinearLayout btn_submit_marks = dialogSuccess.findViewById(R.id.VL_psd_menu);
        Button btn_login = dialogSuccess.findViewById(R.id.btn_login);
        ImageView btn_image = dialogSuccess.findViewById(R.id.btn_image);
        TextView tv_name = dialogSuccess.findViewById(R.id.tv_name);
        int width = getResources().getDisplayMetrics().widthPixels - 100;
        int height = getResources().getDisplayMetrics().heightPixels - 250;
//        deletePopup.getWindow().setLayout(width, height);
        dialogSuccess.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogSuccess.getWindow().setGravity(Gravity.CENTER);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude = latitt;
                double longitude = longit;
                String label = title;
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(mapIntent);

            }
        });
        tv_name.setText(title);
        if (!url.equals("") ){
            Picasso.get()
                    .load(url)//download URL
                    .error(R.drawable.student_icon)//if failed
                    .into(btn_image);
        }

        dialogSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSuccess.setCancelable(true);
        dialogSuccess.setCanceledOnTouchOutside(true);
        dialogSuccess.show();
    }







}