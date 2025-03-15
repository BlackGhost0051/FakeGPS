package com.blackghost.fakegps.Managers;


import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapManager {
    private MapView mapView;
    private MapController mapController;
    private Marker userMarker;


    public MapManager(MapView mapView){
        this.mapView = mapView;
        loadMap();
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

    public void userClickToLocationButton(GeoPoint location){
        updateUserMarker(location);

        mapController.setCenter(location);
        mapController.setZoom(18.0);
    }

    public void updateUserLocation(GeoPoint location){
        updateUserMarker(location);
    }

    private void updateUserMarker(GeoPoint location){
        if (userMarker != null) {
            mapView.getOverlays().remove(userMarker);
        }

        userMarker = new Marker(mapView);
        userMarker.setPosition(location);
        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        mapView.getOverlays().add(userMarker);
        mapView.invalidate();
    }
}
