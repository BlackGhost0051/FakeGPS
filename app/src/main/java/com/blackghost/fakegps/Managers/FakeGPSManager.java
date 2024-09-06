package com.blackghost.fakegps.Managers;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.provider.ProviderProperties;
import android.os.SystemClock;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FakeGPSManager {

    private String test = "TEST";

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }


    private Context context;
    private LocationManager locationManager;
    private boolean isMockActive = false;
    private Timer timer;

    public FakeGPSManager(Context context){
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void initializeMockProvider(){
        if (!isMockActive) {
            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    locationManager.addTestProvider(
                            LocationManager.GPS_PROVIDER,
                            false, false, false, false, true, true, true,
                            ProviderProperties.POWER_USAGE_LOW, ProviderProperties.ACCURACY_FINE
                    );
                } else {
                    locationManager.addTestProvider(LocationManager.GPS_PROVIDER, false, false, false, false, true, true, true, 0, 5);
                }
                locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
                isMockActive = true;
                Log.d("FakeGPS", "Mock location provider started.");
            } catch (SecurityException e) {
                e.printStackTrace();
                Log.e("FakeGPS", "Failed to start mock provider", e);
            }
        }
    }

    public void disableMockProvider(){
        if (isMockActive) {
            try {
                stopTimer(); // Stop ongoing waypoint simulation if running
                locationManager.clearTestProviderEnabled(LocationManager.GPS_PROVIDER);
                locationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
                isMockActive = false;
                Log.d("FakeGPS", "Mock location provider stopped.");
            } catch (SecurityException e) {
                e.printStackTrace();
                Log.e("FakeGPS", "Failed to stop mock provider", e);
            }
        }
    }

    public void setLocation(double latitude, double longitude, float accuracy) {
        if (!isMockActive) {
            Log.e("FakeGPS", "Mock provider not active. Call initializeMockProvider() first.");
            return;
        }

        Location mockLocation = createMockLocation(latitude, longitude, accuracy);
        try {
            locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, mockLocation);
            Log.d("FakeGPS", "Single mock location set: " + latitude + ", " + longitude);
        } catch (SecurityException e) {
            e.printStackTrace();
            Log.e("FakeGPS", "Failed to set mock location", e);
        }
    }

    public void setWay(List<Location> waypoints, long intervalMillis) {
        if (!isMockActive) {
            Log.e("FakeGPS", "Mock provider not active. Call initializeMockProvider() first.");
            return;
        }

        stopTimer();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            int index = 0;

            @Override
            public void run() {
                if (index < waypoints.size() && isMockActive) {
                    Location waypoint = waypoints.get(index);
                    setLocation(waypoint.getLatitude(), waypoint.getLongitude(), waypoint.getAccuracy());
                    index++;
                } else if (index == waypoints.size()) {
                    // Stay at the last location !!!
                    Location lastWaypoint = waypoints.get(waypoints.size() - 1);
                    setLocation(lastWaypoint.getLatitude(), lastWaypoint.getLongitude(), lastWaypoint.getAccuracy());
                    Log.d("FakeGPS", "Staying at the last location: " + lastWaypoint.getLatitude() + ", " + lastWaypoint.getLongitude());
                    stopTimer();
                }
            }
        }, 0, intervalMillis);
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            Log.d("FakeGPS", "Timer stopped.");
        }
    }

    // Helper function to create a mock location
    private Location createMockLocation(double lat, double lon, float accuracy) {
        Location mockLocation = new Location(LocationManager.GPS_PROVIDER);
        mockLocation.setLatitude(lat);
        mockLocation.setLongitude(lon);
        mockLocation.setAccuracy(accuracy);
        mockLocation.setAltitude(0);
        mockLocation.setTime(System.currentTimeMillis());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                Method setElapsedRealtimeNanos = Location.class.getMethod("setElapsedRealtimeNanos", long.class);
                setElapsedRealtimeNanos.setAccessible(true);
                setElapsedRealtimeNanos.invoke(mockLocation, SystemClock.elapsedRealtimeNanos());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return mockLocation;
    }
}
