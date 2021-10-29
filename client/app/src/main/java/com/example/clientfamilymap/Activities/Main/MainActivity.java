package com.example.clientfamilymap.Activities.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.clientfamilymap.Activities.Main.Fragments.LoginFragment;
import com.example.clientfamilymap.Activities.Main.Fragments.Map.MapFragment;
import com.example.clientfamilymap.R;
import com.example.clientfamilymap.Activities.Search.SearchActivity;
import com.example.clientfamilymap.Activities.Settings.SettingsActivity;
import com.example.clientfamilymap.Utility.cache.DataCache;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener,
        MapFragment.LogoutListener {

    //Lifecycle ------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Iconify.with(new FontAwesomeModule());

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.mainFrameLayout);

        if (fragment == null) {
            DataCache dataCache = DataCache.getInstance();

            if (dataCache.getAuthToken() == null) {
                fragment = createLoginFragment();
            }
            else {
                fragment = createMapFragment();
            }

            fragmentManager.beginTransaction()
                    .add(R.id.mainFrameLayout, fragment)
                    .commit();
        }
        else {
            if (fragment instanceof LoginFragment) {
                ((LoginFragment) fragment).registerListener(this);
            }
        }
    }

    //Fragments ------------------------------------------------------------------------------------

    private Fragment createLoginFragment() {
        LoginFragment fragment = new LoginFragment();
        fragment.registerListener(this);
        return fragment;
    }

    private Fragment createMapFragment() {
        MapFragment fragment = new MapFragment();
        fragment.registerLogoutListener(this);
        return fragment;
    }

    @Override
    public void notifyDone() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = createMapFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.mainFrameLayout, fragment)
                .commit();
    }

    @Override
    public void notifyLogout() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = createLoginFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.mainFrameLayout, fragment)
                .commit();
    }


    //Menu -----------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem settingsMenuItem = menu.findItem(R.id.settingsMenuItem);
        settingsMenuItem.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_gear)
                .colorRes(R.color.white).actionBarSize());

        MenuItem searchMenuItem = menu.findItem(R.id.searchMenuItem);
        searchMenuItem.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_search)
                .colorRes(R.color.white).actionBarSize());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch(menu.getItemId()) {
            case R.id.settingsMenuItem:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.searchMenuItem:
                //send to Search Activity
                intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

}