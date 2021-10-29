package com.example.clientfamilymap.Activities.Main.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.clientfamilymap.Utility.ServerProxy;

import Request.LoginRequest;
import Request.RegisterRequest;
import Result.Responses.LoginResult;
import Result.Responses.RegisterResult;


public class SignInTask implements Runnable {
    private static final String SUCCESS_KEY = "success";
    private static final String USERNAME_KEY = "username";
    private static final String AUTHTOKEN_KEY = "authtoken";
    private static final String PERSON_ID_TOKEN = "personId";
    private static final String MESSAGE_TOKEN = "message";

    private final Handler messageHandler;
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;

    public SignInTask(Handler messageHandler, String username, String password, String email,
                      String firstName, String lastName, String gender) {
        this.messageHandler = messageHandler;
        this.registerRequest = new RegisterRequest(username, password, email, firstName, lastName, gender);
    }

    public SignInTask(Handler messageHandler, String username, String password) {
        this.messageHandler = messageHandler;
        this.loginRequest = new LoginRequest(username, password);
    }

    @Override
    public void run() {
        if (registerRequest != null) {
            RegisterResult registerResult = ServerProxy.register(registerRequest);

            if (registerResult != null && registerResult.isSuccess()) {
                sendMessage(registerResult.getUsername(), registerResult.getAuthtoken(),
                            registerResult.getPersonID());
            }
            else {
                if (registerResult == null) {
                    sendMessage("null RegisterResult");
                }
                else {
                    sendMessage(registerResult.getMessage());
                }
            }
        }
        else {
            LoginResult loginResult = ServerProxy.login(loginRequest);

            if (loginResult != null && loginResult.isSuccess()) {
                sendMessage(loginResult.getUsername(), loginResult.getAuthtoken(), loginResult.getPersonID());
            } else {
                if (loginResult == null) {
                    sendMessage("null LoginResult");
                } else {
                    sendMessage(loginResult.getMessage());
                }
            }
        }

        loginRequest = null;
        registerRequest = null;
    }

    private void sendMessage(String errorMessage) {
        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();
        messageBundle.putBoolean(SUCCESS_KEY, false);
        messageBundle.putString(MESSAGE_TOKEN, errorMessage);
        message.setData(messageBundle);

        messageHandler.sendMessage(message);
    }

    private void sendMessage(String username, String authtoken, String personId) {
        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();
        messageBundle.putBoolean(SUCCESS_KEY, true);
        messageBundle.putString(USERNAME_KEY, username);
        messageBundle.putString(AUTHTOKEN_KEY, authtoken);
        messageBundle.putString(PERSON_ID_TOKEN, personId);
        message.setData(messageBundle);

        messageHandler.sendMessage(message);
    }
}