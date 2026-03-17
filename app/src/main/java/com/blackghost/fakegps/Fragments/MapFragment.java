package com.blackghost.fakegps.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackghost.fakegps.Interfaces.MainActivityInterface;
import com.blackghost.fakegps.Managers.FakeGPSManager;
import com.blackghost.fakegps.Managers.MapManager;
import com.blackghost.fakegps.R;


public class MapFragment extends Fragment implements MainActivityInterface {


    private FakeGPSManager fakeGPSManager;
    private MapManager mapManager;

//    private MainActivityInterface activityInterface;

    public MapFragment(FakeGPSManager fakeGPSManager) {
        this.fakeGPSManager = fakeGPSManager;
    }

    public MapFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        activityInterface = (MainActivityInterface) context;
//
//        if (fakeGPSManager == null && context instanceof com.blackghost.fakegps.MainActivity) {
//            fakeGPSManager = ((com.blackghost.fakegps.MainActivity) context).getFakeGPSManager();
//        }
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MapManager.initialize(requireContext());

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapManager = new MapManager(requireContext(), view, savedInstanceState);

        return view;
    }

    @Override public void onStart() { super.onStart(); if (mapManager != null) mapManager.onStart(); }
    @Override public void onResume() { super.onResume(); if (mapManager != null) mapManager.onResume(); }
    @Override public void onPause() { super.onPause(); if (mapManager != null) mapManager.onPause(); }
    @Override public void onStop() { super.onStop(); if (mapManager != null) mapManager.onStop(); }
    @Override public void onSaveInstanceState(@NonNull Bundle outState) { super.onSaveInstanceState(outState); if (mapManager != null) mapManager.onSaveInstanceState(outState); }
    @Override public void onLowMemory() { super.onLowMemory(); if (mapManager != null) mapManager.onLowMemory(); }
    @Override public void onDestroyView() { super.onDestroyView(); if (mapManager != null) mapManager.onDestroy(); }


    @Override
    public void test() {

    }

    @Override
    public void setLocation() {

    }
}