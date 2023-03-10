package com.example.sheetbuilder.ui.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;

import com.example.sheetbuilder.R;
import com.example.sheetbuilder.ui.ui.activity.HomepageActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import timber.log.Timber;

public class LogInFragment extends Fragment implements View.OnClickListener {
    private EditText mUsername;
    private EditText mPassword;
    private AndroidViewModel mVm;

    private GoogleSignInOptions gSignInOptions;
    private GoogleSignInClient gSignInClient;

    private ImageView mGoogleBtn;

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        // Build a GoogleSignInClient with the options specified by gso.
        if(getContext() != null)
        {
            Timber.tag(TAG).d("Successfully Created Google SignInClient");
            gSignInClient = GoogleSignIn.getClient(getContext(), gSignInOptions);
        }
        else
        {
            Timber.tag(TAG).d("getContext is NULL, we cannot create Google SignInClient");
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle savedInstanceState){
        View v;

        Timber.tag(TAG).d("onCreateView()");
        Activity activity = requireActivity();

        v = inf.inflate(R.layout.login_fragment, c, false);

        mUsername=v.findViewById(R.id.username);
        mPassword=v.findViewById(R.id.password);

        mGoogleBtn=v.findViewById(R.id.gmail_button);
        mGoogleBtn.setOnClickListener(this);

        final Button newActButton = v.findViewById(R.id.new_account_button);
        if(newActButton!=null){
            newActButton.setOnClickListener(this);
        }
        final Button clearButton = v.findViewById(R.id.clear_button);
        if(clearButton!=null){
            clearButton.setOnClickListener(this);
        }
        final Button exitButton = v.findViewById(R.id.exit_button);
        if(exitButton!= null){
            exitButton.setOnClickListener(this);
        }
        return v;
    }

    @Override
    public void onClick(View view){
        final Activity activity = requireActivity();
        final int vId = view.getId();

        Timber.tag(TAG).d("Received button click!");

        if(vId==R.id.new_account_button){
            //new account
        }else if(vId == R.id.clear_button){
            //clear
        }
        else if(vId == R.id.gmail_button)
        {
            Timber.tag(TAG).d("You clicked you google button");
            googleSignIn();
        }
        else if(vId==R.id.exit_button){
            startActivity(new Intent(activity, HomepageActivity.class));
            activity.finish();
        }
    }

    private void googleSignIn()
    {
        Intent signInIntent = gSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

           handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask)
    {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            Timber.tag(TAG).d("Your google account id is: " +account.getId());
        } catch (ApiException e) {
            Timber.tag(TAG).d("Sign in Result to Google: failed code" + e.getStatusCode());
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Timber.tag(TAG).d("onDestroyView()");
        mUsername = null;
        mPassword = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.tag(TAG).d("onDestroy()");
        final Activity activity = requireActivity();
        //mUserAccountViewModel.getAllUserAccounts().removeObservers((LifecycleOwner) activity);
    }

    @Override
    public void onStart(){
        super.onStart();
        Timber.tag(TAG).d("OnStart()");
    }

    @Override
    public void onResume(){
        super.onResume();
        Timber.tag(TAG).d("OnResume()");
    }

    @Override
    public void onPause(){
        super.onPause();
        Timber.tag(TAG).d("OnPause()");
    }

    @Override
    public void onStop(){
        super.onStop();
        Timber.tag(TAG).d("OnStop()");
    }
}
