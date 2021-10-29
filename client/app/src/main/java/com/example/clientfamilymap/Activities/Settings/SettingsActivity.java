package com.example.clientfamilymap.Activities.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clientfamilymap.Activities.Main.MainActivity;
import com.example.clientfamilymap.R;
import com.example.clientfamilymap.Utility.cache.SettingsCache;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    //Members --------------------------------------------------------------------------------------

    private final SettingsCache settingsCache = SettingsCache.getInstance();

    private SwitchMaterial lifeStorySwitch;
    private SwitchMaterial familyTreeSwitch;
    private SwitchMaterial spouseSwitch;
    private SwitchMaterial fathersSideSwitch;
    private SwitchMaterial mothersSideSwitch;
    private SwitchMaterial maleEventsSwitch;
    private SwitchMaterial femaleEventsSwitch;
    private RelativeLayout logoutBar;

    //Lifecycle ------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialize();
    }


    //Up Button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }
        return true;
    }


    //Initialization -------------------------------------------------------------------------------

    public void initialize() {
        initializeSwitches();
        initializeOnClickListeners();
    }

    public void initializeSwitches() {
        lifeStorySwitch = findViewById(R.id.life_story_line_toggle);
        familyTreeSwitch = findViewById(R.id.family_tree_line_toggle);
        spouseSwitch = findViewById(R.id.spouse_line_toggle);
        fathersSideSwitch = findViewById(R.id.fathers_side_toggle);
        mothersSideSwitch = findViewById(R.id.mothers_side_toggle);
        maleEventsSwitch = findViewById(R.id.male_events_toggle);
        femaleEventsSwitch = findViewById(R.id.female_events_toggle);

        lifeStorySwitch.setChecked(settingsCache.isShowLifeStory());
        familyTreeSwitch.setChecked(settingsCache.isShowFamilyTree());
        spouseSwitch.setChecked(settingsCache.isShowSpouse());
        fathersSideSwitch.setChecked(settingsCache.isShowFathersSide());
        mothersSideSwitch.setChecked(settingsCache.isShowMothersSide());
        maleEventsSwitch.setChecked(settingsCache.isShowMaleEvents());
        femaleEventsSwitch.setChecked(settingsCache.isShowFemaleEvents());
    }

    //Listeners ------------------------------------------------------------------------------------

    private void initializeOnClickListeners() {
        initializeToggleListener();
        initializeLogoutListener();
    }

    private void initializeToggleListener() {
        View.OnClickListener toggleListener = v -> {
            settingsCache.setShowLifeStory(lifeStorySwitch.isChecked());
            settingsCache.setShowFamilyTree(familyTreeSwitch.isChecked());
            settingsCache.setShowSpouse(spouseSwitch.isChecked());
            settingsCache.setShowFathersSide(fathersSideSwitch.isChecked());
            settingsCache.setShowMothersSide(mothersSideSwitch.isChecked());
            settingsCache.setShowMaleEvents(maleEventsSwitch.isChecked());
            settingsCache.setShowFemaleEvents(femaleEventsSwitch.isChecked());
        };

        lifeStorySwitch.setOnClickListener(toggleListener);
        familyTreeSwitch.setOnClickListener(toggleListener);
        spouseSwitch.setOnClickListener(toggleListener);
        fathersSideSwitch.setOnClickListener(toggleListener);
        mothersSideSwitch.setOnClickListener(toggleListener);
        maleEventsSwitch.setOnClickListener(toggleListener);
        femaleEventsSwitch.setOnClickListener(toggleListener);
    }


    private void initializeLogoutListener() {
        logoutBar = findViewById(R.id.logout_bar);

        logoutBar.setOnClickListener(v -> {
            //Logout
            settingsCache.setLogout(true);

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
