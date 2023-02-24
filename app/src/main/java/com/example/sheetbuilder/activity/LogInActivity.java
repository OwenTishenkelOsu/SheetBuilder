package com.example.sheetbuilder.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.sheetbuilder.R;
import com.example.sheetbuilder.fragment.LogInFragment;

import timber.log.Timber;

public class LogInActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.plant(new Timber.DebugTree());
        Timber.tag(TAG).d("onCreate()");

        setContentView(R.layout.fragment_transition);

        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentById(R.id.container);

        if (frag == null) {
            frag = new LogInFragment();
            fm.beginTransaction().add(R.id.container, frag).commit();
        }
    }
}
