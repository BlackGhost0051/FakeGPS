package com.blackghost.fakegps.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blackghost.fakegps.R;


public class InfoFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        TextView tvGithub = view.findViewById(R.id.tv_github);
        TextView tvRepo = view.findViewById(R.id.tv_repo);

        tvGithub.setOnClickListener(v -> openUrl(getString(R.string.gitHubUrl)));
        tvRepo.setOnClickListener(v -> openUrl(getString(R.string.gitHubRepoUrl)));

        return view;
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
