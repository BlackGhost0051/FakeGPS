package com.blackghost.fakegps.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackghost.fakegps.Managers.FakeGPSManager;
import com.blackghost.fakegps.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback{

    private FakeGPSManager fakeGPSManager;

    private GoogleMap mMap;

    public MapFragment(FakeGPSManager fakeGPSManager) {
        this.fakeGPSManager = fakeGPSManager;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        fakeGPSManager.initializeMockProvider();


        fakeGPSManager.setLocation(36.1699, -115.1398, 5.0f); // Las Vegas
        // don't work if fragment destroyed | Need save location in FakeGPSManager | setGPS(lon lat) setLocation ???
        //setWaypointPath();


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync((OnMapReadyCallback) this);
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        mMap.setMyLocationEnabled(true);
    }


    private void setWaypointPath() {
        List<Location> waypoints = new ArrayList<>();

        Location point1 = new Location(LocationManager.GPS_PROVIDER);
        point1.setLatitude(37.7749); // San Francisco
        point1.setLongitude(-122.4194);
        point1.setAccuracy(5.0f);
        waypoints.add(point1);

        Location point2 = new Location(LocationManager.GPS_PROVIDER);
        point2.setLatitude(34.0522); // Los Angeles
        point2.setLongitude(-118.2437);
        point2.setAccuracy(5.0f);
        waypoints.add(point2);

        Location point3 = new Location(LocationManager.GPS_PROVIDER);
        point3.setLatitude(36.1699); // Las Vegas
        point3.setLongitude(-115.1398);
        point3.setAccuracy(5.0f);
        waypoints.add(point3);

        long intervalMillis = 5000;
        fakeGPSManager.setWay(waypoints, intervalMillis);
    }
}