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

        loadMap();

        setWaypointPath();

        myLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    return;
                }

                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                LocationListener locationListener = new LocationListener() {

                    @Override
                    public void onLocationChanged(@NonNull Location location) {

                        Log.d("onLocationChanged", "Start");
                        GeoPoint currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());

                        if (myLocationMarker != null) {
                            mapView.getOverlays().remove(myLocationMarker);
                        }

                        myLocationMarker = new Marker(mapView);
                        myLocationMarker.setPosition(currentLocation);
                        myLocationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

                        mapView.getOverlays().add(myLocationMarker);
                        mapView.invalidate();

                        mapController.setCenter(currentLocation);
                        mapController.setZoom(20.0f);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) { }

                    @Override
                    public void onProviderEnabled(String provider) { }

                    @Override
                    public void onProviderDisabled(String provider) { }
                };

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 10, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation != null) {
                    GeoPoint currentLocation = new GeoPoint(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    mapView.getController().setCenter(currentLocation);
                    mapController.setZoom(15.0);

                    myLocationMarker = new Marker(mapView);
                    myLocationMarker.setPosition(currentLocation);
                    myLocationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

                    mapView.getOverlays().add(myLocationMarker);
                    mapView.invalidate();
                } else {
                    Toast.makeText(getContext(), "Unable to retrieve current location", Toast.LENGTH_SHORT).show();
                }
            }
        });




        return view;
    }

    private void loadMap(){

        mapView.setTileSource(TileSourceFactory.MAPNIK);

        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        mapController = mapView.getController();
        mapController.setZoom(9.5);

        GeoPoint startPoint = new GeoPoint(36.1699, -115.1398); // Las Vegas
        mapController.setCenter(startPoint);
    }



    private void setWaypointPath() {
        List<Location> waypoints = new ArrayList<>();

        Location point1 = new Location(LocationManager.GPS_PROVIDER);
        point1.setLatitude(37.7749);
        point1.setLongitude(-122.4194);
        point1.setAccuracy(5.0f);
        waypoints.add(point1);

        Location point2 = new Location(LocationManager.GPS_PROVIDER);
        point2.setLatitude(36.8749);
        point2.setLongitude(-122.4194);
        point2.setAccuracy(5.0f);
        waypoints.add(point2);

        Location point3 = new Location(LocationManager.GPS_PROVIDER);
        point3.setLatitude(35.9749);
        point3.setLongitude(-122.4194);
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