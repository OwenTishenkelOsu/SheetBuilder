package com.example.sheetbuilder.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;

import com.example.sheetbuilder.R;
import com.example.sheetbuilder.activity.HomepageActivity;

import timber.log.Timber;

public class LogInFragment extends Fragment implements View.OnClickListener {
    private EditText mUsername;
    private EditText mPassword;
    private AndroidViewModel mVm;
    private final String TAG = getClass().getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle savedInstanceState){
        View v;
        //Timber.plant(new Timber.DebugTree());
        Timber.tag(TAG).d("onCreateView()");
        Activity activity = requireActivity();

        v = inf.inflate(R.layout.login_fragment, c, false);

        mUsername=v.findViewById(R.id.username);
        mPassword=v.findViewById(R.id.password);

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
        }else if(vId==R.id.exit_button){
            startActivity(new Intent(activity, HomepageActivity.class));
            activity.finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Timber.tag(TAG).d("onDestroyView()");
        mUsername = null;
        mPassword = null;
    }
}
