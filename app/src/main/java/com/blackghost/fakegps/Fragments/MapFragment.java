package com.blackghost.fakegps.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.blackghost.fakegps.Interfaces.MainActivityInterface;
import com.blackghost.fakegps.Managers.FakeGPSManager;
import com.blackghost.fakegps.Managers.MapManager;
import com.blackghost.fakegps.R;


import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements MainActivityInterface {

    private MapView mapView;
    private IMapController mapController;
    private MyLocationNewOverlay myLocationOverlay;

    private FakeGPSManager fakeGPSManager;

    private MainActivityInterface activityInterface;

    private Marker myLocationMarker;
    private MapManager mapManager;


    private ImageButton myLocationBtn;

    public MapFragment(FakeGPSManager fakeGPSManager) {
        this.fakeGPSManager = fakeGPSManager;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityInterface = (MainActivityInterface) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(getActivity().getPackageName());
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = view.findViewById(R.id.map);
        myLocationBtn = view.findViewById(R.id.my_location_btn);

        mapManager = new MapManager(mapView);

        myLocationBtn.setOnClickListener(v -> getCurrentLocation());

        return view;
    }


    private void getCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            GeoPoint currentLocation = new GeoPoint(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            mapManager.userClickToLocationButton(currentLocation);
        } else {
            Toast.makeText(getContext(), "Unable to retrieve current location", Toast.LENGTH_SHORT).show();
        }

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d("onLocationChanged", "Updating location...");
                GeoPoint currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                mapManager.updateUserLocation(currentLocation);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 10, locationListener);
    }



    @Override
    public void test() {

    }

    @Override
    public void setLocation() {

    }
}