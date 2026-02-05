package com.blackghost.fakegps.Managers;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import android.widget.Toast;

import com.blackghost.fakegps.R;

import java.util.List;

public class MapManager {
    private final MapView mapView;
    private final Context context;
    private final ImageButton myLocationBtn;
    private final EditText searchBar;
    private final LocationManager locationManager;

    private MapController mapController;
    private Marker myLocationMarker;


    public MapManager(MapView mapView, Context context, ImageButton myLocationBtn, EditText searchBar){
        this.mapView = mapView;
        this.context = context;
        this.myLocationBtn = myLocationBtn;
        this.searchBar = searchBar;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        loadMap();

        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                String locationName = searchBar.getText().toString().trim();
                if (!locationName.isEmpty()) {
                    searchLocation(locationName);
                }
                return true;
            }
            return false;
        });
    }

    private void loadMap(){
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        mapController = (MapController) mapView.getController();
        mapController.setZoom(9.5);

        GeoPoint startPoint = new GeoPoint(36.1699, -115.1398); // Las Vegas
        mapController.setCenter(startPoint);
    }

    private void searchLocation(String locationName) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        new Thread(() -> {
            Geocoder geocoder = new Geocoder(context);
            try {
                List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    GeoPoint geoPoint = new GeoPoint(address.getLatitude(), address.getLongitude());
                    mainHandler.post(() -> centerMap(geoPoint));
                } else {
                    mainHandler.post(() ->
                            Toast.makeText(context, R.string.toast_location_not_found, Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                Log.e("MapManager", "Geocoder error", e);
                mainHandler.post(() ->
                        Toast.makeText(context, R.string.toast_error_finding_location, Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void centerMap(GeoPoint point){
        mapController.setCenter(point);
    }

    public void userClickToLocationButton(GeoPoint location){
        updateUserMarker(location);

        mapController.setCenter(location);
        mapController.setZoom(18.0);
    }

    public void updateUserLocation(GeoPoint location){
        updateUserMarker(location);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateUserMarker(GeoPoint location){
        if (myLocationMarker != null) {
            mapView.getOverlays().remove(myLocationMarker);
        }

        myLocationMarker = new Marker(mapView);
        myLocationMarker.setPosition(location);
        myLocationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        myLocationMarker.setIcon(context.getResources().getDrawable(R.drawable.my_location_icon));

        mapView.getOverlays().add(myLocationMarker);
        mapView.invalidate();
    }

    public void getCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, R.string.toast_location_permission_required, Toast.LENGTH_SHORT).show();
            return;
        }

        myLocationBtn.setImageResource(R.drawable.location_searching_24);

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            GeoPoint currentLocation = new GeoPoint(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            userClickToLocationButton(currentLocation);
            myLocationBtn.setImageResource(R.drawable.my_location_24);
        } else {
            Toast.makeText(context, R.string.toast_unable_to_retrieve_location, Toast.LENGTH_SHORT).show();
            myLocationBtn.setImageResource(R.drawable.location_disabled_24);
        }

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d("MapManager", "Updating location...");
                GeoPoint currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                updateUserLocation(currentLocation);
                myLocationBtn.setImageResource(R.drawable.my_location_24);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {
                myLocationBtn.setImageResource(R.drawable.location_disabled_24);
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 10, locationListener);


    }
}
