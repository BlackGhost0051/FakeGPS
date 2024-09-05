package com.blackghost.fakegps.Managers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.provider.ProviderProperties;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.lang.reflect.Method;

public class FakeGPSManager {
    private Context context;
    private LocationManager locationManager;

    public FakeGPSManager(Context context){
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }


    public void setupMockLocation(double lat, double lon){
        if( locationManager == null){
            Log.e("FakeGPSManager", "LocationManager is null");
            Toast.makeText(context, "LocationManager is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "Location permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.S){
                locationManager.addTestProvider(
                        LocationManager.GPS_PROVIDER,
                        false,false,false,false,true,true,true,
                        ProviderProperties.POWER_USAGE_LOW, ProviderProperties.ACCURACY_FINE
                );
            } else {
                locationManager.addTestProvider(LocationManager.GPS_PROVIDER,
                        false, false, false, false, true, true, true, 0, 5);
            }
            locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);


            Location mockLocation = new Location(LocationManager.GPS_PROVIDER);
            mockLocation.setLatitude(lat);
            mockLocation.setLongitude(lon);
            mockLocation.setAltitude(0);
            mockLocation.setAccuracy(5);
            mockLocation.setTime(System.currentTimeMillis());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                try {
                    Method setElapsedRealtimeNanos = Location.class.getMethod("setElapsedRealtimeNanos", long.class);
                    setElapsedRealtimeNanos.invoke(mockLocation, SystemClock.elapsedRealtimeNanos());
                } catch (Exception e) {
                    Log.e("FakeGPSManager", "Failed to set elapsed real time", e);
                }
            }

            locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, mockLocation);
            Toast.makeText(context, "Mock location set to " + lat + ", " + lon, Toast.LENGTH_SHORT).show();


        } catch (SecurityException e) {
            Log.e("FakeGPSManager", "SecurityException: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            Log.e("FakeGPSManager", "Incomplete mock location: missing data", e);
        }
    }
}
