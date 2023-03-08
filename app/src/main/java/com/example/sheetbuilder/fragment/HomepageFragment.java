package com.example.sheetbuilder.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sheetbuilder.R;

import timber.log.Timber;

public class HomepageFragment extends Fragment implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle savedInstanceState){
        View v;

        Timber.tag(TAG).d("onCreateView()");
        Activity activity = requireActivity();

        v = inf.inflate(R.layout.homepage_fragment, c, false);

        final Button createSheetButton = v.findViewById(R.id.create_sheet_button);
        if(createSheetButton!=null){
            createSheetButton.setOnClickListener(this);
        }
        final Button openSheetButton = v.findViewById(R.id.open_sheet_button);
        if(openSheetButton!=null){
            openSheetButton.setOnClickListener(this);
        }
        final Button openTemplateButton = v.findViewById(R.id.open_template_button);
        if(openTemplateButton!= null){
            openTemplateButton.setOnClickListener(this);
        }
        final Button createTemplateButton = v.findViewById(R.id.create_template_button);
        if(createTemplateButton!= null){
            createTemplateButton.setOnClickListener(this);
        }
        return v;
    }

    @Override
    public void onClick(View view){
        final Activity activity = requireActivity();
        final int vId = view.getId();

        Timber.tag(TAG).d("Received button click!");

        if(vId==R.id.create_sheet_button){
            //create sheet
        }else if(vId == R.id.open_sheet_button){
            //open sheet
        }else if(vId==R.id.open_template_button){
            //open template
        }else if(vId== R.id.create_template_button){
            //create template
        }
    }
}
