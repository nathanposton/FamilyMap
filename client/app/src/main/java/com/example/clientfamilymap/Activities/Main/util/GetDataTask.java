package com.example.clientfamilymap.Activities.Main.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.clientfamilymap.Utility.cache.DataCache;

import Model.AuthToken;


public class GetDataTask implements Runnable {
    private static final String FIRST_NAME_KEY = "firstName";
    private static final String LAST_NAME_KEY = "lastName";

    private final Handler messageHandler;
    private AuthToken authtoken;
    private String personId;

    public GetDataTask(Handler messageHandler, String username, String authtoken, String personId) {
        this.messageHandler = messageHandler;
        this.authtoken = new AuthToken(authtoken, username);
        this.personId = personId;
    }

    @Override
    public void run() {
        DataCache dataCache = DataCache.getInstance();
        dataCache.setAuthToken(authtoken);

        dataCache.populate(personId);

        String firstName = dataCache.getUserPerson().getFirstName();
        String lastName = dataCache.getUserPerson().getLastName();

        sendMessage(firstName, lastName);
    }

    private void sendMessage(String firstName, String lastName) {
        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();
        messageBundle.putString(FIRST_NAME_KEY, firstName);
        messageBundle.putString(LAST_NAME_KEY, lastName);
        message.setData(messageBundle);

        messageHandler.sendMessage(message);
    }
}