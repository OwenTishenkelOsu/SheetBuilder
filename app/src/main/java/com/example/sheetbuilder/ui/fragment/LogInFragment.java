package com.example.sheetbuilder.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;

import com.example.sheetbuilder.R;
import com.example.sheetbuilder.ui.activity.HomepageActivity;
import com.example.sheetbuilder.ui.activity.LogInActivity;
import com.example.sheetbuilder.ui.activity.OpenSheetActivity;
import com.example.sheetbuilder.ui.activity.SheetActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import timber.log.Timber;

public class LogInFragment extends Fragment implements View.OnClickListener {

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
        GoogleSignInOptions gSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        // Build a GoogleSignInClient with the options specified by gso.
        gSignInClient = GoogleSignIn.getClient(getContext(), gSignInOptions);
        Timber.tag(TAG).d("Successfully Created Google SignInClient");

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if(account != null)
        {
            finishSignIn(getActivity());
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle savedInstanceState){
        View v;

        Timber.tag(TAG).d("onCreateView()");
        Activity activity = requireActivity();

        v = inf.inflate(R.layout.login_fragment, c, false);

        mGoogleBtn=v.findViewById(R.id.gmail_button);
        mGoogleBtn.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view){
        final Activity activity = requireActivity();
        final int vId = view.getId();

        Timber.tag(TAG).d("Received button click!");

        if(vId == R.id.gmail_button)
        {
            Timber.tag(TAG).d("You clicked you google button");
            googleSignIn();
        }
        else if(vId==R.id.exit_button){
            Toast.makeText(activity, "Need to Add Exit Functionality ", Toast.LENGTH_SHORT).show();
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

            try {
                task.getResult(ApiException.class);
                Timber.tag(TAG).d("google CompleteTask is Successful");
                finishSignIn(getActivity());
            } catch (ApiException e) {
                Timber.tag(TAG).d("Sign in Result to Google: failed code" + e.getStatusCode());
            }
        }
    }

    private void finishSignIn(Activity activity)
    {
        Intent intent = new Intent(activity, OpenSheetActivity.class);
        startActivity(intent);
        activity.finish();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Timber.tag(TAG).d("onDestroyView()");
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
