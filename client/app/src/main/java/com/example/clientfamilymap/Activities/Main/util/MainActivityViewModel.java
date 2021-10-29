package com.example.clientfamilymap.Activities.Main.util;

import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    private String serverHost;
    private String serverPort;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;

    private boolean signInFilled = false;
    private boolean registerFilled = false;

    //Sign In Form Filled Evaluation ---------------------------------------------------------------

    public void updateSignIn() {
        if (signInIsFilled()) {
            signInFilled = true;
        }
        else {
            signInFilled = false;
        }
    }

    private boolean signInIsFilled() {
        boolean isFilled = false;
        if (!(signInHasNull() || signInHasEmpty())) {
            isFilled = true;
        }
        return isFilled;
    }

    private boolean signInHasNull() {
        return serverHost == null || serverPort == null || username == null || password == null;
    }

    private boolean signInHasEmpty() {
       return serverHost.isEmpty() || serverPort.isEmpty() || username.isEmpty() || password.isEmpty();
    }

    //Register Form Filled Evaluation --------------------------------------------------------------

    public void updateRegister() {
        if (registerIsFilled()) {
            registerFilled = true;
        }
        else {
            registerFilled = false;
        }
    }

    private boolean registerIsFilled() {
        boolean isFilled = false;
        if ( signInFilled && !(registerHasNull() || registerHasEmpty()) ) {
            isFilled = true;
        }
        return isFilled;
    }

    private boolean registerHasNull() {
        return firstName == null || lastName == null || email == null || gender == null;
    }

    private boolean registerHasEmpty() {
        return firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || gender.isEmpty();
    }

    //Getters & Setters ------------------------------------

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isSignInFilled() {
        return signInFilled;
    }

    public void setSignInFilled(boolean signInFilled) {
        this.signInFilled = signInFilled;
    }

    public boolean isRegisterFilled() {
        return registerFilled;
    }

    public void setRegisterFilled(boolean registerFilled) {
        this.registerFilled = registerFilled;
    }
}
