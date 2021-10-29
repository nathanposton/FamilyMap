package com.example.clientfamilymap.Utility.cache;

import java.util.HashSet;
import java.util.Set;

import Model.Person;

public class SettingsCache {

    //Members --------------------------------------------------------------------------------------

    private static SettingsCache instance;

    private boolean showLifeStory = true;
    private boolean showFamilyTree = true;
    private boolean showSpouse = true;
    private boolean showFathersSide = true;
    private boolean showMothersSide = true;
    private boolean showMaleEvents = true;
    private boolean showFemaleEvents = true;
    private boolean logout = false;

    private final DataCache dataCache = DataCache.getInstance();

    private static final String FEMALE = "f";

    private Set<Person> filteredPeople = new HashSet<>();

    //Singleton Architecture -----------------------------------------------------------------------

    public static SettingsCache getInstance() {
        if (instance == null) {
            instance = new SettingsCache();
        }
        return instance;
    }

    private SettingsCache() {

    }


    //Retrieve Which People Get Mapped -------------------------------------------------------------

    public Set<Person> getIncludedPeople() {
        Set<Person> includedPeople = new HashSet<>(dataCache.getPeopleById().values());
        Set<Person> peopleToRemove = getPeopleToRemove(includedPeople);
        includedPeople.removeAll(peopleToRemove);

        this.filteredPeople = includedPeople;

        return includedPeople;
    }

    //Functions

    public Set<Person> getPeopleToRemove(Set<Person> potentialPeople) {
        Set<Person> peopleToRemove = new HashSet<>();
        for (Person person : potentialPeople) {
            if (badGender(person) || badParentSide(person)) {
                peopleToRemove.add(person);
            }
        }
        return peopleToRemove;
    }

    private boolean badGender(Person person) {
        boolean personIsFemale = person.getGender().equals(FEMALE);

        return ( (!showFemaleEvents && personIsFemale) ||
                (!showMaleEvents && !personIsFemale) );
    }

    private boolean badParentSide(Person person) {
        return badFatherSide(person) || badMotherSide(person);
    }

    private boolean badFatherSide(Person person) {
        boolean personIsFemale = person.getGender().equals(FEMALE);

        return ( (!showFathersSide &&
                ( (personIsFemale && dataCache.getFatherSideFemales().contains(person)) ||
                        (!personIsFemale && dataCache.getFatherSideMales().contains(person)) ) ) );
    }

    private boolean badMotherSide(Person person) {
        boolean personIsFemale = person.getGender().equals(FEMALE);

        return ( (!showMothersSide &&
                ( (personIsFemale && dataCache.getMotherSideFemales().contains(person)) ||
                        (!personIsFemale && dataCache.getMotherSideMales().contains(person)) ) ) );
    }

    //Wipe -----------------------------------------------------------------------------------------

    public void logout() {
        showLifeStory = true;
        showFamilyTree = true;
        showSpouse = true;
        showFathersSide = true;
        showMothersSide = true;
        showMaleEvents = true;
        showFemaleEvents = true;
        logout = false;

        filteredPeople.clear();
    }

    //Getters and Setters --------------------------------------------------------------------------

    public Set<Person> getFilteredPeople() {
        return filteredPeople;
    }

    public boolean isShowLifeStory() {
        return showLifeStory;
    }

    public void setShowLifeStory(boolean showLifeStory) {
        this.showLifeStory = showLifeStory;
    }

    public boolean isShowFamilyTree() {
        return showFamilyTree;
    }

    public void setShowFamilyTree(boolean showFamilyTree) {
        this.showFamilyTree = showFamilyTree;
    }

    public boolean isShowSpouse() {
        return showSpouse;
    }

    public void setShowSpouse(boolean showSpouse) {
        this.showSpouse = showSpouse;
    }

    public boolean isShowFathersSide() {
        return showFathersSide;
    }

    public void setShowFathersSide(boolean showFathersSide) {
        this.showFathersSide = showFathersSide;
    }

    public boolean isShowMothersSide() {
        return showMothersSide;
    }

    public void setShowMothersSide(boolean showMothersSide) {
        this.showMothersSide = showMothersSide;
    }

    public boolean isShowMaleEvents() {
        return showMaleEvents;
    }

    public void setShowMaleEvents(boolean showMaleEvents) {
        this.showMaleEvents = showMaleEvents;
    }

    public boolean isShowFemaleEvents() {
        return showFemaleEvents;
    }

    public void setShowFemaleEvents(boolean showFemaleEvents) {
        this.showFemaleEvents = showFemaleEvents;
    }

    public void setLogout(boolean logout) {
        this.logout = logout;
    }

    public boolean isLogout() {
        return logout;
    }
}
