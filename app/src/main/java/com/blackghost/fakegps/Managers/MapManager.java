package com.blackghost.fakegps.Managers;


import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.blackghost.fakegps.R;

import org.maplibre.android.MapLibre;
import org.maplibre.android.camera.CameraPosition;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.MapView;
import org.maplibre.android.maps.OnMapReadyCallback;
import org.maplibre.android.maps.Style;

public class MapManager {

    private final MapView mapView;
    private MapLibreMap mapLibreMap;

    private final boolean rotateGestures = false;
    private final LatLng startPosition = new LatLng(20.0, 0);
    private final double startZoomLevel = 2.0;
    private final String tilesURL = "https://tiles.openfreemap.org/styles/liberty";

    public static void initialize(Context context) {
        MapLibre.getInstance(context);
    }

    public MapManager(Context context, View view, Bundle savedInstanceState){
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        initMap();
    }

    private void initMap(){
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapLibreMap map) {
                mapLibreMap = map;

                mapLibreMap.getUiSettings().setRotateGesturesEnabled(rotateGestures);

                mapLibreMap.getUiSettings().setCompassEnabled(false);
                mapLibreMap.getUiSettings().setLogoEnabled(false);
                mapLibreMap.getUiSettings().setAttributionEnabled(false);

                CameraPosition initialPosition = new CameraPosition.Builder()
                        .target(startPosition)
                        .zoom(startZoomLevel)
                        .build();
                mapLibreMap.setCameraPosition(initialPosition);


                map.setStyle(tilesURL, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                    }
                });
            }
        });
    }


    public double getZoomLevel(){
        return mapLibreMap.getZoom();
    }
    public void setZoomLevel(double zoomLevel){
//        mapLibreMap.setZoom(); // need 3 arguments
    }


    public void onStart() {
        if (mapView != null) mapView.onStart();
    }

    public void onResume() {
        if (mapView != null) mapView.onResume();
    }

    public void onPause() {
        if (mapView != null) mapView.onPause();
    }

    public void onStop() {
        if (mapView != null) mapView.onStop();
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mapView != null) mapView.onSaveInstanceState(outState);
    }

    public void onLowMemory() {
        if (mapView != null) mapView.onLowMemory();
    }

    public void onDestroy() {
        if (mapView != null) mapView.onDestroy();
    }
}
