package com.blackghost.fakegps.Managers;


import androidx.annotation.NonNull;

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



    public MapManager(MapView mapView){
        this.mapView = mapView;
        initMap();
    }

    private void initMap(){
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapLibreMap map) {
                mapLibreMap = map;

                mapLibreMap.getUiSettings().setRotateGesturesEnabled(rotateGestures);

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

}
