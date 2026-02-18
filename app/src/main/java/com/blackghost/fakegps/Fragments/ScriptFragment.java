package com.blackghost.fakegps.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackghost.fakegps.Managers.FakeGPSManager;
import com.blackghost.fakegps.R;

public class ScriptFragment extends Fragment {

    FakeGPSManager fakeGPSManager;

    public ScriptFragment(FakeGPSManager fakeGPSManager) {
        this.fakeGPSManager = fakeGPSManager;
    }

    public ScriptFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (fakeGPSManager == null && context instanceof com.blackghost.fakegps.MainActivity) {
            fakeGPSManager = ((com.blackghost.fakegps.MainActivity) context).getFakeGPSManager();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_script, container, false);
    }
}