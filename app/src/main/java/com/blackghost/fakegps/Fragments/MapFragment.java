package com.blackghost.fakegps.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackghost.fakegps.Managers.FakeGPSManager;
import com.blackghost.fakegps.R;


public class MapFragment extends Fragment {

    private FakeGPSManager fakeGPSManager;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        fakeGPSManager = new FakeGPSManager(getContext());

        fakeGPSManager.setupMockLocation(17.7749, -122.4194);

        return view;
    }
}