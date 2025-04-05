package com.blackghost.fakegps.Models;

import org.osmdroid.util.GeoPoint;

public class LocationSuggestion {
    public final String name;
    public final GeoPoint point;

    public LocationSuggestion(String name, GeoPoint point) {
        this.name = name;
        this.point = point;
    }
}
