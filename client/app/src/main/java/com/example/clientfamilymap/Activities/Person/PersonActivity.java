package com.example.clientfamilymap.Activities.Person;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clientfamilymap.Activities.Event.EventActivity;
import com.example.clientfamilymap.Activities.Main.MainActivity;
import com.example.clientfamilymap.R;
import com.example.clientfamilymap.Utility.cache.DataCache;
import com.example.clientfamilymap.Utility.cache.SearchCache;
import com.example.clientfamilymap.Utility.cache.SettingsCache;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;

import Model.Event;
import Model.Person;

public class PersonActivity extends AppCompatActivity {

    //Members --------------------------------------------------------------------------------------

    public static final String PERSON_ID_KEY = "ReceivedPersonID";
    private static final String FEMALE = "f";

    private final DataCache dataCache = DataCache.getInstance();
    private Person mainPerson;
    private boolean isSettingsIncluded = false;

    //Lifecycle ------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        initialize();
    }

    //Initialize -----------------------------------------------------------------------------------

    private void initialize() {
        retrieveMainPerson();
        initializeActivityView();
        initializeExpandableView();
    }

    private void retrieveMainPerson() {
        String mainPersonId = getIntent().getStringExtra(PERSON_ID_KEY);
        mainPerson = dataCache.getPeopleById().get(mainPersonId);

        SettingsCache settingsCache = SettingsCache.getInstance();
        if (settingsCache.getIncludedPeople().contains(mainPerson)) {
            isSettingsIncluded = true;
        }


        assert mainPerson != null;
    }

    private void initializeActivityView() {
        //set first name
        TextView firstNameView = findViewById(R.id.firstNameField);
        firstNameView.setText(mainPerson.getFirstName());

        //set last name
        TextView lastNameView = findViewById(R.id.lastNameField);
        lastNameView.setText(mainPerson.getLastName());

        //set gender
        TextView genderView = findViewById(R.id.genderField);
        if (mainPerson.getGender().equals(FEMALE)) {
            genderView.setText(R.string.female);
        }
        else {
            genderView.setText(R.string.male);
        }
    }

    private void initializeExpandableView() {
        ExpandableListView expandableListView = findViewById(R.id.expandable_list_view);

        SearchCache searchCache = SearchCache.getInstance();
        searchCache.populate();
        List<Event> personEvents = searchCache.getRefinedEventList(mainPerson);
        List<Person> personPeople = searchCache.getRefinedPersonList(mainPerson);

        expandableListView.setAdapter(new ExpandableListAdapter(personEvents, personPeople));
    }

    //ExpandableListAdapter ------------------------------------------------------------------------

    private class ExpandableListAdapter extends BaseExpandableListAdapter {
        private static final int EVENT_GROUP_POSITION = 0;
        private static final int PEOPLE_GROUP_POSITION = 1;

        private final List<Event> personEvents;
        private final List<Person> personPeople;

        ExpandableListAdapter(List<Event> personEvents, List<Person> personPeople) {
            this.personEvents = personEvents;
            this.personPeople = personPeople;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return personEvents.size();
                case PEOPLE_GROUP_POSITION:
                    return personPeople.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position");
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return getResources().getString(R.string.life_events);
                case PEOPLE_GROUP_POSITION:
                    return getResources().getString(R.string.family);
                default:
                    throw new IllegalArgumentException("Unrecognized group position" + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return personEvents.get(childPosition);
                case PEOPLE_GROUP_POSITION:
                    return personPeople.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position" + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.person_header, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.list_title);

            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    titleView.setText(R.string.life_events);
                    break;
                case PEOPLE_GROUP_POSITION:
                    titleView.setText(R.string.family);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position" + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChile,
                                 View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case EVENT_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.search_item, parent, false);
                    initializePersonEventView(itemView, childPosition);
                    break;
                case PEOPLE_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.search_item, parent, false);
                    initializePersonPeopleView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position" + groupPosition);
            }

            return itemView;
        }

        private void initializePersonEventView(View eventItemView, final int childPosition) {
            if (isSettingsIncluded) {
                Event event = personEvents.get(childPosition);
                Person person = dataCache.getPeopleById().get(event.getPersonID());

                TextView eventInfoView = eventItemView.findViewById(R.id.info_first);
                TextView eventNameView = eventItemView.findViewById(R.id.info_second);
                ImageView eventIconView = eventItemView.findViewById(R.id.item_icon);


                //set Event Description
                String firstInfoText = String.format(getResources().getString(R.string.event_description),
                        event.getEventType().toUpperCase(),
                        event.getCity(), event.getCountry(), event.getYear());
                eventInfoView.setText(firstInfoText);

                //set event owner
                String secondInfoText = String.format(getResources().getString(R.string.event_participant),
                        person.getFirstName(), person.getLastName());
                eventNameView.setText(secondInfoText);

                //set event icon
                eventIconView.setImageDrawable(new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_map_marker)
                        .colorRes(R.color.purple_500).sizeDp(40));

                //set onClickListener
                eventItemView.setOnClickListener(v -> {
                    //TODO - verify
                    Intent intent = new Intent(getApplication(), EventActivity.class);
                    intent.putExtra(EventActivity.EVENT_ID_KEY, event.getEventID());
                    intent.putExtra(EventActivity.PERSON_ID_KEY, event.getPersonID());
                    startActivity(intent);
                });
            }
        }

        private void initializePersonPeopleView(View personItemView, final int childPosition) {
            Person person = personPeople.get(childPosition);

            if (person != null) {
                TextView personNameView = personItemView.findViewById(R.id.info_first);
                TextView personRelationshipView = personItemView.findViewById(R.id.info_second);
                ImageView personIconView = personItemView.findViewById(R.id.item_icon);


                //set name
                String fullName = String.format(getResources().getString(R.string.event_participant),
                        person.getFirstName(), person.getLastName());
                personNameView.setText(fullName);

                //set relation
                switch (childPosition) {
                    case 0:
                        personRelationshipView.setText(getResources().getString(R.string.father));
                        break;
                    case 1:
                        personRelationshipView.setText(getResources().getString(R.string.mother));
                        break;
                    case 2:
                        personRelationshipView.setText(getResources().getString(R.string.spouse));
                        break;
                    default:
                        personRelationshipView.setText(getResources().getString(R.string.child));
                        break;
                }

                //set gender icon
                if (person.getGender().equals(FEMALE)) {
                    personIconView.setImageDrawable(new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_female)
                            .colorRes(R.color.female_pink).sizeDp(40));
                } else {
                    personIconView.setImageDrawable(new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_male)
                            .colorRes(R.color.male_blue).sizeDp(40));
                }

                //set onClickListener
                personItemView.setOnClickListener(v -> {
                    //TODO - verify
                    Intent intent = new Intent(getApplication(), PersonActivity.class);
                    intent.putExtra(PersonActivity.PERSON_ID_KEY, person.getPersonID());
                    startActivity(intent);
                });
            }
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }

    //Menu -----------------------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }
        return true;
    }
}
