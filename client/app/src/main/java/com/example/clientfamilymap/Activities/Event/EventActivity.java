package com.example.clientfamilymap.Activities.Event;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.clientfamilymap.Activities.Main.MainActivity;
import com.example.clientfamilymap.Activities.Main.Fragments.Map.MapFragment;
import com.example.clientfamilymap.R;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class EventActivity extends AppCompatActivity {
    public static final String EVENT_ID_KEY = "ReceivedEventID";
    public static final String PERSON_ID_KEY = "ReceivedPersonID";

    //Menu -----------------------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }
        return true;
    }

    //Lifecycle ------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Iconify.with(new FontAwesomeModule());

        String eventId = getIntent().getStringExtra(EVENT_ID_KEY);
        String personId = getIntent().getStringExtra(PERSON_ID_KEY);


        FragmentManager fragmentManager = getSupportFragmentManager();
        MapFragment fragment = (MapFragment) fragmentManager.findFragmentById(R.id.mainFrameLayout);
        if (fragment == null) {
            fragment = createMapFragment(eventId, personId);
            fragmentManager.beginTransaction()
                    .add(R.id.mainFrameLayout, fragment)
                    .commit();
        }
    }

    private MapFragment createMapFragment(String eventId, String personId) {
        MapFragment mapFragment = new MapFragment();

        Bundle arguments = new Bundle();
        arguments.putString(MapFragment.EVENT_ID_KEY, eventId);
        arguments.putString(MapFragment.PERSON_ID_KEY, personId);

        mapFragment.setArguments(arguments);

        return mapFragment;
    }

}
