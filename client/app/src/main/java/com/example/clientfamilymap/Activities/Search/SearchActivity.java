package com.example.clientfamilymap.Activities.Search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientfamilymap.Activities.Event.EventActivity;
import com.example.clientfamilymap.Activities.Main.MainActivity;
import com.example.clientfamilymap.Activities.Person.PersonActivity;
import com.example.clientfamilymap.R;
import com.example.clientfamilymap.Utility.cache.DataCache;
import com.example.clientfamilymap.Utility.cache.SearchCache;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;

import Model.Event;
import Model.Person;

public class SearchActivity extends AppCompatActivity {

    //Members --------------------------------------------------------------------------------------

    private static final int EVENT_ITEM_VIEW_TYPE = 0;
    private static final int PERSON_ITEM_VIEW_TYPE = 1;
    private static final String FEMALE = "f";

    private final List<Person> personSearchList = new ArrayList<>();
    private final List<Event> eventSearchList = new ArrayList<>();

    private final DataCache dataCache = DataCache.getInstance();

    //Lifecycle ------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        ImageView searchIcon = findViewById(R.id.search_mag_icon);
        searchIcon.setImageDrawable(new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_search)
                .colorRes(R.color.gray).sizeDp(40));
        /*
        search form code to retrieve event and person list
         */
        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        SearchCache searchCache = SearchCache.getInstance();
        searchCache.populate();

        SearchAdapter adapter = new SearchAdapter(eventSearchList, personSearchList);
        recyclerView.setAdapter(adapter);

        EditText searchQuery = findViewById(R.id.search_field);
        searchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                eventSearchList.clear();
                personSearchList.clear();

                List<Person> tempPeople = searchCache.getRefinedPersonList(s.toString());
                List<Event> tempEvents = searchCache.getRefinedEventList(s.toString());

                eventSearchList.addAll(tempEvents);
                personSearchList.addAll(tempPeople);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }
        return true;
    }

    //Adapter --------------------------------------------------------------------------------------

    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
        private final List<Event> events;
        private final List<Person> people;

        SearchAdapter(List<Event> events, List<Person> people) {
            this.events = events;
            this.people = people;
        }

        @Override
        public int getItemViewType(int position) {
            return position < events.size() ? EVENT_ITEM_VIEW_TYPE : PERSON_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            view = getLayoutInflater().inflate(R.layout.search_item, parent, false);

            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if (position < events.size()) {
                holder.bind(events.get(position));
            }
            else {
                holder.bind(people.get(position - events.size()));
            }
        }

        @Override
        public int getItemCount() {
            return events.size() + people.size();
        }
    }

    //Search View Holder ---------------------------------------------------------------------------

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView firstInfo;
        private final TextView secondInfo;
        private final ImageView infoIcon;

        private final int viewType;
        private Event event;
        private Person person;

        SearchViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;
            itemView.setOnClickListener(this);

            this.firstInfo = itemView.findViewById(R.id.info_first);
            this.secondInfo = itemView.findViewById(R.id.info_second);
            this.infoIcon = itemView.findViewById(R.id.item_icon);
        }

        private void bind(Person person) {
            this.person = person;
            String fullName = String.format(getResources().getString(R.string.event_participant),
                                            person.getFirstName(), person.getLastName());
            firstInfo.setText(fullName);

            if (person.getGender().equals(FEMALE)) {
                infoIcon.setImageDrawable(new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_female)
                        .colorRes(R.color.female_pink).sizeDp(40));
            }
            else {
                infoIcon.setImageDrawable(new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_male)
                        .colorRes(R.color.male_blue).sizeDp(40));
            }
        }

        private void bind(Event event) {
            this.event = event;
            this.person = dataCache.getPeopleById().get(event.getPersonID());
            if (person == null) {
                System.out.println("null person in Search event bind");
            }
            else {

                String firstInfoText = String.format(getResources().getString(R.string.event_description),
                        event.getEventType().toUpperCase(),
                        event.getCity(), event.getCountry(), event.getYear());
                firstInfo.setText(firstInfoText);

                String secondInfoText = String.format(getResources().getString(R.string.event_participant),
                        person.getFirstName(), person.getLastName());
                secondInfo.setText(secondInfoText);

                infoIcon.setImageDrawable(new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_map_marker)
                        .colorRes(R.color.purple_500).sizeDp(40));
            }
        }

        @Override
        public void onClick(View view) {
            if (viewType == EVENT_ITEM_VIEW_TYPE) {
                //TODO - verify EVENT
                Intent intent = new Intent(getApplication(), EventActivity.class);
                intent.putExtra(EventActivity.EVENT_ID_KEY, event.getEventID());
                intent.putExtra(EventActivity.PERSON_ID_KEY, person.getPersonID());
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(getApplication(), PersonActivity.class);
                intent.putExtra(PersonActivity.PERSON_ID_KEY, person.getPersonID());
                startActivity(intent);
            }
        }
    }
}
