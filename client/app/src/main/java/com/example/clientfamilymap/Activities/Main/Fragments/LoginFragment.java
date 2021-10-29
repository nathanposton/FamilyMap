package com.example.clientfamilymap.Activities.Main.Fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clientfamilymap.Activities.Main.util.GetDataTask;
import com.example.clientfamilymap.Activities.Main.util.MainActivityViewModel;
import com.example.clientfamilymap.Activities.Main.util.SignInTask;
import com.example.clientfamilymap.R;
import com.example.clientfamilymap.Utility.ServerProxy;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginFragment extends Fragment {

    //Members --------------------------------------------------------------------------------------

    private static final String SUCCESS_KEY = "success";
    private static final String USERNAME_KEY = "username";
    private static final String AUTHTOKEN_KEY = "authtoken";
    private static final String PERSON_ID_TOKEN = "personId";
    private static final String MESSAGE_TOKEN = "message";
    private static final String FIRST_NAME_KEY = "firstName";
    private static final String LAST_NAME_KEY = "lastName";

    EditText serverHost;
    EditText serverPort;
    EditText username;
    EditText password;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText gender;
    Button signInButton;
    Button registerButton;
    TextView serverHostTextView;
    TextView serverPortTextView;
    TextView usernameTextView;
    TextView passwordTextView;
    TextView firstNameTextView;
    TextView lastNameTextView;
    TextView emailTextView;
    TextView genderTextView;

    //Activity Life Cycle --------------------------------------------------------------------------

    View view;

    private MainActivityViewModel getViewModel() {
        return new ViewModelProvider(this).get(MainActivityViewModel.class);
    }

    private Listener listener;

    public interface Listener {
        void notifyDone();
    }

    public void registerListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_login, container, false);


        initialize();

        updateButtonStatus();
        setFormDefaultsServer();
        highlightDefault();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveFormToViewModel();
        //TODO - initial form preferences - saved
    }

    //Menu -----------------------------------------------------------------------------------------

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem settingsMenuItem = menu.findItem(R.id.settingsMenuItem);
        if (settingsMenuItem != null) {
            settingsMenuItem.setVisible(false);
        }

        MenuItem searchMenuItem = menu.findItem(R.id.searchMenuItem);
        if (searchMenuItem != null) {
            searchMenuItem.setVisible(false);
        }
    }

    //Initializer Functions -----------------------------------------------------------------------

    private void initializeHandlers() {
        if (signInHandler == null) {
            signInHandler = new SignInHandler(this);
        }
        else {
            signInHandler.setTarget(this);
        }

        if (getDataHandler == null) {
            getDataHandler = new GetDataHandler(this);
        }
        else {
            getDataHandler.setTarget(this);
        }
    }

    private void initialize() {
        initializeHandlers();
        initializeViewVariables();
        initializeClickListeners();
        initializeTextListeners(); //Definition at bottom
    }

    private void initializeViewVariables() {
        serverHost = view.findViewById(R.id.serverHostField);
        serverPort = view.findViewById(R.id.serverPortField);
        username = view.findViewById(R.id.usernameField);
        password = view.findViewById(R.id.passwordField);
        firstName = view.findViewById(R.id.firstNameField);
        lastName = view.findViewById(R.id.lastNameField);
        email = view.findViewById(R.id.emailField);
        gender = view.findViewById(R.id.genderField);

        serverHostTextView = view.findViewById(R.id.serverHostPrompt);
        serverPortTextView = view.findViewById(R.id.serverPortPrompt);
        usernameTextView = view.findViewById(R.id.usernamePrompt);
        passwordTextView = view.findViewById(R.id.passwordPrompt);
        firstNameTextView = view.findViewById(R.id.firstNamePrompt);
        lastNameTextView = view.findViewById(R.id.lastNamePrompt);
        emailTextView = view.findViewById(R.id.emailPrompt);
        genderTextView = view.findViewById(R.id.genderPrompt);

        signInButton = view.findViewById(R.id.signInButton);
        registerButton = view.findViewById(R.id.registerButton);
    }

    private void initializeClickListeners() {
        signInButton.setOnClickListener(v -> {
            ServerProxy.setDomain(getViewModel().getServerHost(), getViewModel().getServerPort());

            handleSignIn();
        });
        registerButton.setOnClickListener(v -> {
            ServerProxy.setDomain(getViewModel().getServerHost(), getViewModel().getServerPort());
            handleRegister();
        });
    }

    private void setFormDefaultsServer() {
        if (getViewModel().getServerHost() == null) {
            serverHost.setText(this.getString(R.string.serverHostDefault));
        }
        if (getViewModel().getServerPort() == null) {
            serverPort.setText(this.getString(R.string.serverPortDefault));
        }
    }

    private void highlightDefault() {
        serverHostTextView.setText(this.getString(R.string.serverHost));
        serverHostTextView.setTypeface(null, Typeface.NORMAL);

        serverPortTextView.setText(this.getString(R.string.serverPort));
        serverPortTextView.setTypeface(null, Typeface.NORMAL);

        usernameTextView.setText(this.getString(R.string.username));
        usernameTextView.setTypeface(null, Typeface.NORMAL);

        passwordTextView.setText(this.getString(R.string.password));
        passwordTextView.setTypeface(null, Typeface.NORMAL);

        firstNameTextView.setText(this.getString(R.string.firstName));
        firstNameTextView.setTypeface(null, Typeface.NORMAL);

        lastNameTextView.setText(this.getString(R.string.lastName));
        lastNameTextView.setTypeface(null, Typeface.NORMAL);

        emailTextView.setText(this.getString(R.string.email));
        emailTextView.setTypeface(null, Typeface.NORMAL);

        genderTextView.setText(this.getString(R.string.gender));
        genderTextView.setTypeface(null, Typeface.NORMAL);
    }

    //Sign In Button -------------------------------------------------------------------------------

    private void handleSignIn() {
        if (getViewModel().isSignInFilled()) {
            getSignInResult();
        } else {
            highlightSignIn();
            Toast.makeText(getActivity(), R.string.sign_in_unfilled, Toast.LENGTH_SHORT).show();
        }
    }

    private void getSignInResult() {
        SignInTask signInTask = new SignInTask(signInHandler, getViewModel().getUsername(),
                getViewModel().getPassword());
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(signInTask);
    }

    private void processSignInResult() {
        if (signInHandler.isSuccess()) {
            GetDataTask getDataTask = new GetDataTask(getDataHandler, signInHandler.getUsername(),
                    signInHandler.getAuthtoken(),
                    signInHandler.getPersonId());

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(getDataTask);
        } else {
            Toast.makeText(getActivity(), R.string.sign_in_fail, Toast.LENGTH_SHORT).show();
            System.out.println(signInHandler.getErrorMessage());
        }
    }

    private void processGetDataResult() {
        Toast.makeText(getActivity(),
                getResources().getString(R.string.sign_in_success,
                        getDataHandler.getFirstName(),
                        getDataHandler.getLastName()),
                Toast.LENGTH_SHORT).show();

        //Send to map fragment
        if (listener != null) {
            listener.notifyDone();
        }
    }

    //Register Button ------------------------------------------------------------------------------

    private void handleRegister() {
        if (getViewModel().isRegisterFilled()) {
            getRegisterResult();
        } else {
            highlightRegister();
            Toast.makeText(getActivity(), R.string.sign_in_unfilled, Toast.LENGTH_SHORT).show();
        }
    }

    private void getRegisterResult() {
        SignInTask signInTask = new SignInTask(signInHandler, getViewModel().getUsername(),
                getViewModel().getPassword(), getViewModel().getEmail(),
                getViewModel().getFirstName(), getViewModel().getLastName(),
                getViewModel().getGender());
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(signInTask);
    }

    //Sign In Handler ------------------------------------------------------------------------------

    static SignInHandler signInHandler;

    private static class SignInHandler extends Handler {
        private WeakReference<LoginFragment> loginFragmentTarget;
        private String username;
        private String authtoken;
        private String personId;
        private String errorMessage;
        private boolean success;

        SignInHandler(LoginFragment target) {
            loginFragmentTarget = new WeakReference<LoginFragment>(target);
        }

        public void setTarget(LoginFragment target) {
            loginFragmentTarget.clear();
            loginFragmentTarget = new WeakReference<LoginFragment>(target);
        }

        @Override
        public void handleMessage(@NonNull Message message) {
            Bundle bundle = message.getData();

            success = bundle.getBoolean(SUCCESS_KEY);

            if (success) {
                username = bundle.getString(USERNAME_KEY);
                authtoken = bundle.getString(AUTHTOKEN_KEY);
                personId = bundle.getString(PERSON_ID_TOKEN);
            }
            else {
                errorMessage = bundle.getString(MESSAGE_TOKEN);
            }

            LoginFragment loginFragment = loginFragmentTarget.get();
            loginFragment.processSignInResult();
        }

        public String getUsername() {
            return username;
        }

        public String getAuthtoken() {
            return authtoken;
        }

        public String getPersonId() {
            return personId;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public boolean isSuccess() {
            return success;
        }
    }

    //Get Data Handler -----------------------------------------------------------------------------

    static GetDataHandler getDataHandler;

    private static class GetDataHandler extends Handler {
        private WeakReference<LoginFragment> loginFragmentTarget;
        private String firstName;
        private String lastName;

        GetDataHandler(LoginFragment target) {
            loginFragmentTarget = new WeakReference<LoginFragment>(target);
        }

        public void setTarget(LoginFragment target) {
            loginFragmentTarget.clear();
            loginFragmentTarget = new WeakReference<LoginFragment>(target);
        }

        @Override
        public void handleMessage(@NonNull Message message) {
            Bundle bundle = message.getData();

            firstName = bundle.getString(FIRST_NAME_KEY);
            lastName = bundle.getString(LAST_NAME_KEY);

            LoginFragment loginFragment = loginFragmentTarget.get();
            loginFragment.processGetDataResult();
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }
    }

    //Helper Functions -----------------------------------------------------------------------------

    private void updateButtonStatus() {
//        getViewModel().updateSignIn();
//        getViewModel().updateRegister();

        if (getViewModel().isSignInFilled()) {
            view.findViewById(R.id.signInButton).setBackgroundColor(getResources().getColor(R.color.purple_500));
        }
        else {
            view.findViewById(R.id.signInButton).setBackgroundColor(getResources().getColor(R.color.gray));
        }

        if (getViewModel().isRegisterFilled()) {
            view.findViewById(R.id.registerButton).setBackgroundColor(getResources().getColor(R.color.purple_500));
        }
        else {
            view.findViewById(R.id.registerButton).setBackgroundColor(getResources().getColor(R.color.gray));

        }
    }

    private void saveFormToViewModel() {
        getViewModel().setServerHost(serverHost.getText().toString());
        getViewModel().setServerPort(serverPort.getText().toString());
        getViewModel().setUsername(username.getText().toString());
        getViewModel().setPassword(password.getText().toString());
        getViewModel().setFirstName(firstName.getText().toString());
        getViewModel().setLastName(lastName.getText().toString());
        getViewModel().setEmail(email.getText().toString());
        getViewModel().setGender(gender.getText().toString());
    }

    private void highlightSignIn() {
        serverHostTextView.setText(this.getString(R.string.promptServerHost));
        serverHostTextView.setTypeface(null, Typeface.ITALIC);

        serverPortTextView.setText(this.getString(R.string.promptServerPort));
        serverPortTextView.setTypeface(null, Typeface.ITALIC);

        usernameTextView.setText(this.getString(R.string.promptUsername));
        usernameTextView.setTypeface(null, Typeface.ITALIC);

        passwordTextView.setText(this.getString(R.string.promptPassword));
        passwordTextView.setTypeface(null, Typeface.ITALIC);

        firstNameTextView.setText(this.getString(R.string.firstName));
        firstNameTextView.setTypeface(null, Typeface.NORMAL);

        lastNameTextView.setText(this.getString(R.string.lastName));
        lastNameTextView.setTypeface(null, Typeface.NORMAL);

        emailTextView.setText(this.getString(R.string.email));
        emailTextView.setTypeface(null, Typeface.NORMAL);

        genderTextView.setText(this.getString(R.string.gender));
        genderTextView.setTypeface(null, Typeface.NORMAL);
    }

    private void highlightRegister() {
        serverHostTextView.setText(this.getString(R.string.promptServerHost));
        serverHostTextView.setTypeface(null, Typeface.ITALIC);

        serverPortTextView.setText(this.getString(R.string.promptServerPort));
        serverPortTextView.setTypeface(null, Typeface.ITALIC);

        usernameTextView.setText(this.getString(R.string.promptUsername));
        usernameTextView.setTypeface(null, Typeface.ITALIC);

        passwordTextView.setText(this.getString(R.string.promptPassword));
        passwordTextView.setTypeface(null, Typeface.ITALIC);

        firstNameTextView.setText(this.getString(R.string.promptFirstName));
        firstNameTextView.setTypeface(null, Typeface.ITALIC);

        lastNameTextView.setText(this.getString(R.string.promptLastName));
        lastNameTextView.setTypeface(null, Typeface.ITALIC);

        emailTextView.setText(this.getString(R.string.promptEmail));
        emailTextView.setTypeface(null, Typeface.ITALIC);

        genderTextView.setText(this.getString(R.string.promptGender));
        genderTextView.setTypeface(null, Typeface.ITALIC);
    }

    //Text Listeners -------------------------------------------------------------------------------

    private void initializeTextListeners() {

        serverHost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getViewModel().setServerHost(serverHost.getText().toString());

                getViewModel().updateSignIn();
                getViewModel().updateRegister();
                updateButtonStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        serverPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getViewModel().setServerPort(serverPort.getText().toString());

                getViewModel().updateSignIn();
                getViewModel().updateRegister();
                updateButtonStatus();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getViewModel().setUsername(username.getText().toString());

                getViewModel().updateSignIn();
                getViewModel().updateRegister();
                updateButtonStatus();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getViewModel().setPassword(password.getText().toString());

                getViewModel().updateSignIn();
                getViewModel().updateRegister();
                updateButtonStatus();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getViewModel().setFirstName(firstName.getText().toString());

                getViewModel().updateRegister();
                updateButtonStatus();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getViewModel().setLastName(lastName.getText().toString());

                getViewModel().updateRegister();
                updateButtonStatus();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getViewModel().setEmail(email.getText().toString());

                getViewModel().updateRegister();
                updateButtonStatus();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        gender.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getViewModel().setGender(gender.getText().toString());

                getViewModel().updateRegister();
                updateButtonStatus();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //much less text but more demand
        /*
        serverHost.addTextChangedListener(textWatcher);
        serverPort.addTextChangedListener(textWatcher);
        username.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        firstName.addTextChangedListener(textWatcher);
        lastName.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);
        gender.addTextChangedListener(textWatcher);

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            updateButtonStatus();
            saveFormToViewModel();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

          */


    }
}
