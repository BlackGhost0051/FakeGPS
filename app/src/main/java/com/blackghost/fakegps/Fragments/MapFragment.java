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
import com.blackghost.fakegps.Managers.MapManager;
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
    private MapManager mapManager;


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

        mapManager = new MapManager(mapView, getContext(), myLocationBtn);

        myLocationBtn.setOnClickListener(v -> mapManager.getCurrentLocation());

        return view;
    }


    @Override
    public void test() {

    }

    @Override
    public void setLocation() {

    }
}