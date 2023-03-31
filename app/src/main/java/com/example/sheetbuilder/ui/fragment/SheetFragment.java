package com.example.sheetbuilder.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sheetbuilder.R;
import com.example.sheetbuilder.SpeechToText;
import com.example.sheetbuilder.ui.activity.OpenSheetActivity;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class SheetFragment extends Fragment implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    private TextView title;
    private EditText templatename;
    private ScrollView sv;
    private List<EditText> editTexts;
    private LinearLayout l;
    private String pageTitle;
    private int sheetID;
    private int evalue;
    private SpeechToText speechToText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageTitle = this.getArguments().getString("name");
        sheetID = this.getArguments().getInt("id");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle savedInstanceState){
        View v;

        Timber.tag(TAG).d("onCreateView()");
        Activity activity = requireActivity();

        v = inf.inflate(R.layout.sheet_fragment, c, false);

        title = v.findViewById(R.id.title);
        title.setText(pageTitle);
        sv = v.findViewById(R.id.scroll_view);
        l = v.findViewById(R.id.list);
        editTexts = new ArrayList<EditText>();

        templatename = v.findViewById(R.id.template_name);

        final Button addElementButton = v.findViewById(R.id.add_element_button);
        if(addElementButton!= null){
            addElementButton.setOnClickListener(this);
        }
        final Button saveSheetButton = v.findViewById(R.id.save_sheet_button);
        if(saveSheetButton!= null){
            saveSheetButton.setOnClickListener(this);
        }
        final Button backButton = v.findViewById(R.id.back_button);
        if(backButton!= null){
            backButton.setOnClickListener(this);
        }
        final Button voiceButton = v.findViewById(R.id.voice_button);
        if(voiceButton!= null){
            voiceButton.setOnClickListener(this);
        }
        return v;
    }

    @Override
    public void onClick(View view) {
        final Activity activity = requireActivity();
        final int vId = view.getId();

        Timber.tag(TAG).d("Received button click!");

        if (vId == R.id.add_element_button) {
            addEditText();
        } else if (vId == R.id.save_sheet_button) {
            saveElements();
        } else if (vId == R.id.back_button) {
            Intent intent = new Intent(activity, OpenSheetActivity.class);
            startActivity(intent);
            activity.finish();
        } else if(vId == R.id.voice_button){
            speechToText = new SpeechToText(requireActivity());
            if(speechToText.listen()){
                editTexts.get(evalue).setText(speechToText.retrieveText());
            }

        }
    }

    void addEditText(){
        EditText et = new EditText(getContext());
        et.setText("New Element");
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        et.setLayoutParams(params);
        editTexts.add(et);
        //touch listener to track current edit text
        et.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View arg0, MotionEvent arg1)
            {
                evalue=editTexts.indexOf(et);
                return false;
            }
        });
        l.addView(et);
    }

    void saveElements(){

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

    @Override
    public void onDestroy(){
        super.onDestroy();
        Timber.tag(TAG).d("OnDestroy()");
    }
}
