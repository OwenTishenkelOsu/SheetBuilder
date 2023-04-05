package com.example.sheetbuilder.ui.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.example.sheetbuilder.model.Element;
import com.example.sheetbuilder.viewmodel.ElementViewModel;
import com.example.sheetbuilder.ui.activity.OpenSheetActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import timber.log.Timber;

public class SheetFragment extends Fragment implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    private TextView title;
    private EditText templatename;
    private List<Element> mElementList;
    private ScrollView sv;
    private List<EditText> editTexts;
    private LinearLayout l;
    private String pageTitle;
    private ElementViewModel mElementViewModel;
    private int sheetID;

    private int evalue;
    private Element mElement;
    private String userID;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageTitle = this.getArguments().getString("name");
        sheetID = this.getArguments().getInt("id");
        userID = this.getArguments().getString("userid");

        Activity activity = requireActivity();
        mElementViewModel = new ElementViewModel(activity.getApplication());
        mElementViewModel.mRepository.loadElements(Integer.toString(sheetID), ()-> showElements());
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

        final Button addElementButton = v.findViewById(R.id.add_element_button);
        if(addElementButton!= null){
            addElementButton.setOnClickListener(this);
        }
        final Button deleteElementButton = v.findViewById(R.id.delete_element_button);
        if(deleteElementButton!= null){
            deleteElementButton.setOnClickListener(this);
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
        }else if(vId==R.id.delete_element_button){
            if(mElement != null) {
                mElementViewModel.mRepository.deleteElement(mElement, () -> mElementViewModel.mRepository.loadElements(Integer.toString(sheetID), ()->showElements()));
            }
        }else if (vId == R.id.save_sheet_button) {
            saveElements();
        } else if (vId == R.id.back_button) {
            Intent intent = new Intent(activity, OpenSheetActivity.class);
            Bundle b = new Bundle(); //add sheetId and sheetName to bundle for SheetActivity
            b.putString("userid", userID);
            intent.putExtras(b);
            startActivity(intent);
            activity.finish();
        } else if(vId == R.id.voice_button){
            Intent intent
                    = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                    Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
            }
            catch (Exception e) {
                Timber.tag(TAG).d("ERROR, Speech to Text: "+e.toString());
            }

        }
    }

    void addEditText(){
        EditText et = new EditText(getContext());
        et.setText("New Element");
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        et.setLayoutParams(params);

        mElementViewModel.mRepository.addElement(et.getText().toString(), Integer.toString(sheetID), () -> showElements());
    }

    void saveElements(){
        int i = 0;
        for(Element e : mElementViewModel.getAllElements()){
            e.setText(editTexts.get(i).getText().toString());
            i++;
        }
        mElementViewModel.mRepository.saveElements(Integer.toString(sheetID), ()->showElements());
    }

    void showElements(){
        mElementList = mElementViewModel.getAllElements();
        l.removeAllViews();
        Timber.tag(TAG).d("SheetList contents: " + mElementList);
        if(mElementList!= null){
            for(Element e : mElementList){
                EditText et = new EditText(getContext());
                et.setText(e.getText());
                ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                et.setLayoutParams(params);
                et.setTag(e.getId());
                editTexts.add(et);
                l.addView(et);
                et.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        Timber.tag(TAG).d("CLICKING EDITTEXT");
                        for(Element e : mElementList){
                            if(e.getId()==et.getTag()){
                                mElement = e;
                            }
                        }
                        evalue=editTexts.indexOf(et);
                    }
                });
            }
        }
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
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                editTexts.get(evalue).setText(Objects.requireNonNull(result).get(0));
            }
        }
    }
}
