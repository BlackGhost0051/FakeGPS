package com.blackghost.fakegps.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blackghost.fakegps.Interfaces.MainActivityInterface;
import com.blackghost.fakegps.Managers.FakeGPSManager;
import com.blackghost.fakegps.R;


import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements MainActivityInterface {

    private MapView mapView;
    private MyLocationNewOverlay myLocationOverlay;

    private FakeGPSManager fakeGPSManager;

    private MainActivityInterface activityInterface;

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

        loadMap();

        activityInterface.setLocation();


        //fakeGPSManager.setLocation(36.1699, -115.1398, 5.0f, 2500); // Las Vegas



        return view;
    }

    private void loadMap(){

        mapView.setTileSource(TileSourceFactory.MAPNIK);

        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(9.5);

        GeoPoint startPoint = new GeoPoint(36.1699, -115.1398); // Las Vegas
        mapController.setCenter(startPoint);

        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getContext()), mapView);
        myLocationOverlay.enableMyLocation();

        mapView.getOverlays().add(myLocationOverlay);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            Toast.makeText(getActivity(), "N", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getActivity(), "Y", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void test() {

    }

    @Override
    public void setLocation() {

    }
}