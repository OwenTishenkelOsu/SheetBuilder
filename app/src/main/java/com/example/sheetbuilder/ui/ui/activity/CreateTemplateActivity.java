package com.example.sheetbuilder.ui.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.sheetbuilder.R;
import com.example.sheetbuilder.ui.ui.fragment.CreateTemplateFragment;

import timber.log.Timber;

public class CreateTemplateActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.plant(new Timber.DebugTree());
        Timber.tag(TAG).d("onCreate()");

        setContentView(R.layout.fragment_transition);

        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentById(R.id.container);

        if (frag == null) {
            frag = new CreateTemplateFragment();
            fm.beginTransaction().add(R.id.container, frag).commit();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        Timber.tag(TAG).d("OnStart()");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Timber.tag(TAG).d("OnResume()");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Timber.tag(TAG).d("OnPause()");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Timber.tag(TAG).d("OnStop()");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Timber.tag(TAG).d("OnDestroy()");
    }
}