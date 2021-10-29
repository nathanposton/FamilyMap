package com.example.clientfamilymap.Activities.Main.Fragments.Map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.clientfamilymap.Activities.Person.PersonActivity;
import com.example.clientfamilymap.R;
import com.example.clientfamilymap.Activities.Search.SearchActivity;
import com.example.clientfamilymap.Activities.Settings.SettingsActivity;
import com.example.clientfamilymap.Utility.cache.DataCache;
import com.example.clientfamilymap.Utility.cache.SearchCache;
import com.example.clientfamilymap.Utility.cache.SettingsCache;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import Model.Event;
import Model.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    //Members --------------------------------------------------------------------------------------
    public static final String EVENT_ID_KEY = "ReceivedEventID";
    public static final String PERSON_ID_KEY = "ReceivedPersonID";
    public static final String FEMALE = "f";

    private GoogleMap map;
    private View view;

    DataCache dataCache = DataCache.getInstance();
    SettingsCache settingsCache = SettingsCache.getInstance();

    private Drawable defaultEventIcon;
    private Drawable maleIcon;
    private Drawable femaleIcon;

    private Map<String, Marker> markerMap = new TreeMap<>();
    private List<Polyline> polylines = new ArrayList<>();
    private Map<String, Float> colorMap = new TreeMap<>();
    private List<Float> colorArray = new ArrayList<>();

    private String currentPersonId;
    private String currentEventId;
    private LatLng selectedEventLatLng;

    //Lifecycle ------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        initialize();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (settingsCache.isLogout()) {
            clearApp();
            listener.notifyLogout();
        }
        else {
            clearPolylines();
            resetMapInfo();
            populateMap();
        }
    }

    //Map ------------------------------------------------------------------------------------------

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this); //Use this if getting error "map not ready"

        populateMap();

        //On Map Marker Select:
        map.setOnMarkerClickListener(marker -> {
            currentPersonId = marker.getTitle();

            populatePolylines(marker.getTag(), marker.getTitle());

            map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

            updateMapInfo(marker.getTag(), marker.getTitle());

            return true;
        });

        eventActivityStarter();
    }

    //If needed - for use with "map not ready" error
    @Override
    public void onMapLoaded() {

    }

    //Populate Map with Selected People ------------------------------------------------------------

    private void populateMap() {
        if (map != null) {
            map.clear();
            initializeColorArray();

            for (Person person : settingsCache.getIncludedPeople()) {
                Set<Event> eventSet = dataCache.getEventsByPersonID().get(person.getPersonID());

                if (eventSet != null) {
                    generateSetMarkers(eventSet);
                }
            }
        }
    }

    private void generateSetMarkers(Set<Event> eventSet) {
        for (Event event : eventSet) {
            LatLng eventLocation = new LatLng(event.getLatitude(), event.getLongitude());

            Marker marker = map.addMarker(new MarkerOptions().position(eventLocation).title(event.getPersonID()));

            if (!colorMap.containsKey(event.getEventType().toLowerCase())) {
                if (colorArray.size() <= 0) {
                    initializeColorArray();
                }

                colorMap.put(event.getEventType().toLowerCase(), colorArray.get(0));
                colorArray.remove(0);
            }

            marker.setIcon(BitmapDescriptorFactory.defaultMarker(colorMap.get(event.getEventType().toLowerCase())));

            marker.setTag(event.getEventID());

            markerMap.put(event.getEventID(), marker);
        }
    }

    private void updateMapInfo(Object eventId, String personId) {
        if (eventId instanceof String) {
            Set<Event> personEvents = dataCache.getEventsByPersonID().get(personId);
            Event currentEvent = findCurrentEvent((String) eventId, personEvents);

            //Event Description
            String description = String.format(getResources().getString(R.string.event_description),
                    currentEvent.getEventType().toUpperCase(),
                    currentEvent.getCity(), currentEvent.getCountry(),
                    currentEvent.getYear());
            TextView mainTextView = view.findViewById(R.id.mapInfoTextMain);
            mainTextView.setText(description);


            //Event's Participant Description
            Person participant = dataCache.getPeopleById().get(personId);
            String participantDescription = String.format(getResources().getString(R.string.event_participant),
                    participant.getFirstName(),
                    participant.getLastName());
            TextView participantTextView = view.findViewById(R.id.mapInfoTextExtra);
            participantTextView.setText(participantDescription);

            //Set Appropriate description icon
            ImageView descriptionIcon = view.findViewById(R.id.mapInfoIcon);
            if (participant.getGender().equals("m")) {
                descriptionIcon.setImageDrawable(maleIcon);
            }
            else {
                descriptionIcon.setImageDrawable(femaleIcon);
            }
        }

    }

    private void eventActivityStarter() {
        if (selectedEventLatLng != null) {
            populatePolylines(currentEventId, currentPersonId);
            map.animateCamera(CameraUpdateFactory.newLatLng(selectedEventLatLng));
            updateMapInfo(currentEventId, currentPersonId);
        }
    }

    //Draw Polylines --------------------------------------------------------------------------------

    private void populatePolylines(Object markerTag, String personId) {
        clearPolylines();

        if (markerTag instanceof String) {
            Set<Event> personEvents = dataCache.getEventsByPersonID().get(personId);
            assert personEvents != null;

            //draw life line
            drawLifeLines(personEvents);

            Event currentEvent = findCurrentEvent((String) markerTag, personEvents);
            Person selectedPerson = dataCache.getPeopleById().get(personId);
            assert selectedPerson != null;

            if (settingsCache.isShowSpouse()) {
                if (selectedPerson.getSpouseID() != null) {
                    Event spouseEvent = dataCache.getEventsByPersonID().get(selectedPerson.getSpouseID()).first();
                    //draw spouse line
                    drawSpouseLine(currentEvent, spouseEvent);
                }
            }

            //draw ancestor lines
            drawAncestorLines(currentEvent, selectedPerson, 5);
        }
    }

    private void clearPolylines() {
        if (!polylines.isEmpty()) {
            for (Polyline line : polylines) {
                line.remove();
            }
            polylines.clear();
        }
    }

    private Event findCurrentEvent(String eventId, Set<Event> eventSet) {
        Event currentEvent = null;
        for (Event event : eventSet) {
            if (event.getEventID().equals(eventId)) {
                currentEvent = event;
                break;
            }
        }

        return currentEvent;
    }

    private void drawLifeLines(Set<Event> eventSet) {
        if (settingsCache.isShowLifeStory()) {
            List<LatLng> locations = new ArrayList<>();

            for (Event event : eventSet) {
                locations.add(new LatLng(event.getLatitude(), event.getLongitude()));
            }

            if (locations.size() > 1) {
                for (int i = 0; i < locations.size() - 1; i++) {
                    polylines.add(map.addPolyline(new PolylineOptions()
                            .add(locations.get(i), locations.get(i + 1))
                            .width(5)
                            .color(Color.BLUE)));
                }
            }
        }
    }

    private void drawSpouseLine(Event selectedEvent, Event spouseEvent) {
        Person spouse = dataCache.getPeopleById().get(spouseEvent.getPersonID());

        //check if line enabled in settings
        if ( (markerMap.get(spouseEvent.getEventID()) != null) &&
             (settingsCache.getIncludedPeople().contains(spouse)) ) {

            polylines.add(map.addPolyline(new PolylineOptions()
                    .add(new LatLng(selectedEvent.getLatitude(), selectedEvent.getLongitude()),
                            new LatLng(spouseEvent.getLatitude(), spouseEvent.getLongitude()))
                    .width(5)
                    .color(Color.MAGENTA)));
        }
    }

    private void drawAncestorLines(Event selectedEvent, Person selectedPerson, int width) {
        if (settingsCache.isShowFamilyTree()) {
            //Father-side draw
            recurseDrawParentLine(selectedEvent, selectedPerson.getFatherID(), width);

            //Mother-side draw
            recurseDrawParentLine(selectedEvent, selectedPerson.getMotherID(), width);
        }
    }

    private void recurseDrawParentLine(Event selectedEvent, String parentId, int width) {
        if (parentId != null) {
            Event event = dataCache.getEventsByPersonID().get(parentId).first();

            //check if line enabled in settings
            Person selectedEventPerson = dataCache.getPeopleById().get(selectedEvent.getPersonID());
            Person parent = dataCache.getPeopleById().get(parentId);
            if ( (markerMap.get(event.getEventID()) != null) &&
                  (settingsCache.getIncludedPeople().contains(selectedEventPerson)) &&
                  (settingsCache.getIncludedPeople().contains(parent)) ) {
                //draw
                makePolylineOfWidth(width, event, selectedEvent);
            }

            //recurse
            drawAncestorLines(event, dataCache.getPeopleById().get(parentId), (width / 2) );
        }
    }

    private void makePolylineOfWidth(int width, Event event1, Event event2) {
        polylines.add(map.addPolyline(new PolylineOptions()
                .add(new LatLng(event1.getLatitude(), event1.getLongitude()),
                        new LatLng(event2.getLatitude(), event2.getLongitude()))
                .width(width)
                .color(Color.BLACK)));
    }

    //Menu -----------------------------------------------------------------------------------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem settingsMenuItem = menu.findItem(R.id.settingsMenuItem);
        settingsMenuItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_search)
                .colorRes(R.color.white).actionBarSize());

        MenuItem searchMenuItem = menu.findItem(R.id.searchMenuItem);
        searchMenuItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_gear)
                .colorRes(R.color.white).actionBarSize());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch(menu.getItemId()) {
            case R.id.settingsMenuItem:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.searchMenuItem:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    //Initialize -----------------------------------------------------------------------------------

    private void initialize() {
        initializeArguments();
        initializeDrawables();
        initializeMap();
    }

    private void initializeArguments() {
        if (getArguments() != null) {
            currentEventId = getArguments().getString(EVENT_ID_KEY);
            currentPersonId = getArguments().getString(PERSON_ID_KEY);

            Set<Event> eventSet = dataCache.getEventsByPersonID().get(currentPersonId);
            assert eventSet != null;
            for (Event event : eventSet) {
                if (event.getEventID().equals(currentEventId)) {
                    this.selectedEventLatLng = new LatLng(event.getLatitude(), event.getLongitude());
                }
            }
            assert this.selectedEventLatLng != null;
        }
    }

    private void initializeMap() {
        //google map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //info display
        ImageView mapInfoIconView = view.findViewById(R.id.mapInfoIcon);
        mapInfoIconView.setImageDrawable(defaultEventIcon);

        //infoDisplay click listener
        RelativeLayout mapInfo = view.findViewById(R.id.mapInfoDisplay);
        mapInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PersonActivity.class);
            intent.putExtra(PersonActivity.PERSON_ID_KEY, currentPersonId);
            startActivity(intent);
        });

    }

    private void resetMapInfo() {
        ImageView mapInfoIconView = view.findViewById(R.id.mapInfoIcon);
        mapInfoIconView.setImageDrawable(defaultEventIcon);

        TextView mapInfoMain = view.findViewById(R.id.mapInfoTextMain);
        mapInfoMain.setText(R.string.default_map_description);

        TextView mapInfoExtra = view.findViewById(R.id.mapInfoTextExtra);
        mapInfoExtra.setText(R.string.app_name);
    }

    private void initializeDrawables() {
        defaultEventIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker)
                .colorRes(R.color.purple_500).sizeDp(40);

        maleIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male)
                .colorRes(R.color.male_blue).sizeDp(40);

        femaleIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female)
                .colorRes(R.color.female_pink).sizeDp(40);

//        defaultPersonIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker)
//                .colorRes(R.color.purple_500).sizeDp(40);
    }

    //Helper Functions -----------------------------------------------------------------------------

    private static float randFloat(float min, float max) {
        Random rand = new Random();
        return rand.nextFloat() * (max - min) + min;
    }

    private void initializeColorArray() {
        colorArray.clear();

        colorArray.add(BitmapDescriptorFactory.HUE_RED);
        colorArray.add(BitmapDescriptorFactory.HUE_ORANGE);
        colorArray.add(BitmapDescriptorFactory.HUE_YELLOW);
        colorArray.add(BitmapDescriptorFactory.HUE_GREEN);
        colorArray.add(BitmapDescriptorFactory.HUE_CYAN);
        colorArray.add(BitmapDescriptorFactory.HUE_AZURE);
        colorArray.add(BitmapDescriptorFactory.HUE_VIOLET);
        colorArray.add(BitmapDescriptorFactory.HUE_MAGENTA);
        colorArray.add(BitmapDescriptorFactory.HUE_ROSE);
        colorArray.add(BitmapDescriptorFactory.HUE_BLUE);
    }

    //Logout Listener ------------------------------------------------------------------------------

    private LogoutListener listener;

public interface LogoutListener {
    void notifyLogout();
}

    public void registerLogoutListener(LogoutListener logoutListener) {
        this.listener = logoutListener;
    }

    private void clearApp() {
        dataCache.logout();
        settingsCache.logout();
        SearchCache searchCache = SearchCache.getInstance();
        searchCache.logout();

        //FIXME - is this reset necessary?
        //reset members
        map = null;
        view = null;

        defaultEventIcon = null;
        maleIcon = null;
        femaleIcon = null;

        markerMap.clear();
        polylines.clear();
        colorMap.clear();
        colorArray.clear();

        currentPersonId = null;
        currentEventId = null;
        selectedEventLatLng = null;
    }
}
