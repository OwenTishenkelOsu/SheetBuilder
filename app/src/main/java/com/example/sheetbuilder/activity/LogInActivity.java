package com.example.sheetbuilder.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.sheetbuilder.R;
import com.example.sheetbuilder.fragment.LogInFragment;

public class LogInActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentById(R.id.container);

        if (frag == null) {
            frag = new LogInFragment();
            fm.beginTransaction().add(R.id.container, frag).commit();
        }
    }
}
