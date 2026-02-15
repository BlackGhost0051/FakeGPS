package com.blackghost.fakegps;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blackghost.fakegps.Fragments.InfoFragment;
import com.blackghost.fakegps.Fragments.MapFragment;
import com.blackghost.fakegps.Fragments.ScriptFragment;
import com.blackghost.fakegps.Fragments.SettingsFragment;
import com.blackghost.fakegps.Interfaces.MainActivityInterface;
import com.blackghost.fakegps.Managers.FakeGPSManager;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements MainActivityInterface {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private FakeGPSManager fakeGPSManager;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fakeGPSManager.disableMockProvider();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkTheme = prefs.getBoolean("dark_theme", false);
        AppCompatDelegate.setDefaultNightMode(
                darkTheme ? AppCompatDelegate.MODE_NIGHT_YES
                          : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));

        fakeGPSManager = new FakeGPSManager(this);
        fakeGPSManager.initializeMockProvider();

        if (savedInstanceState == null) {
            fragmentR(new MapFragment(fakeGPSManager));
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.map) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    toolbar.setTitle(R.string.menu_map);
                    fragmentR(new MapFragment(fakeGPSManager));
                    return true;
                }
                else if (item.getItemId() == R.id.script) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    toolbar.setTitle(R.string.menu_script);
                    fragmentR(new ScriptFragment(fakeGPSManager));
                    return true;
                }
                else if (item.getItemId() == R.id.settings) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    toolbar.setTitle(R.string.menu_settings);
                    fragmentR(new SettingsFragment());
                    return true;
                }
                else if (item.getItemId() == R.id.info) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    toolbar.setTitle(R.string.menu_info);
                    fragmentR(new InfoFragment());
                    return true;
                }
                return false;
            }
        });

    }

    public FakeGPSManager getFakeGPSManager() {
        return fakeGPSManager;
    }

    private void fragmentR(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void test() {
        Toast.makeText(this, R.string.toast_test, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setLocation() {
        fakeGPSManager.setLocation(36.1699, -115.1398, 5.0f, 2500);
        Toast.makeText(this, R.string.toast_set_location, Toast.LENGTH_SHORT).show();
    }
}