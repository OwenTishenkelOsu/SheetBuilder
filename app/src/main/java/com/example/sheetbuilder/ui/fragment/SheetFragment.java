package com.example.sheetbuilder.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.example.sheetbuilder.R;

import com.example.sheetbuilder.SpeechToText;

import com.example.sheetbuilder.model.Element;
import com.example.sheetbuilder.ui.activity.SheetActivity;
import com.example.sheetbuilder.viewmodel.ElementViewModel;
import com.example.sheetbuilder.viewmodel.SheetViewModel;
import com.example.sheetbuilder.ui.activity.LogInActivity;
import com.example.sheetbuilder.ui.activity.OpenSheetActivity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
    SpeechRecognizer speechRecognizer;
    private int evalue;
    String textToSpeechOutput = "placeholder";
    //private SpeechToText speechToText;
    private boolean waitingForResults = false;
    private boolean listening = false;
    private Element mElement;
    private String userID;
    private Button voiceButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageTitle = this.getArguments().getString("name");
        sheetID = this.getArguments().getInt("id");
        userID = this.getArguments().getString("userid");
        //speechToText = new SpeechToText(requireActivity());

        Activity activity = requireActivity();
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        speechRecognizer.setRecognitionListener(new speechListener());
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
        voiceButton = v.findViewById(R.id.voice_button);
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
            if(waitingForResults){
                speechRecognizer.stopListening();
                editTexts.get(evalue).setText(textToSpeechOutput);
            } else {
                Intent speechIntent  = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                speechRecognizer.startListening(speechIntent);
            }



        }
    }

    void addEditText(){
        EditText et = new EditText(getContext());
        et.setText("New Element");
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        et.setLayoutParams(params);

        //touch listener to track current edit text


        //editTexts.add(et);
        //l.addView(et);
        int i = 0;
        for(Element e : mElementViewModel.getAllElements()){
            e.setText(editTexts.get(i).getText().toString());
            i++;
        }
        mElementViewModel.mRepository.addElement(et.getText().toString(), Integer.toString(sheetID), () -> showElements());
    }

    void saveElements(){
        int i = 0;
        for(Element e : mElementViewModel.getAllElements()){
            e.setText(editTexts.get(i).getText().toString());
            i++;
        }
        //TODO Check for duplicates before saving
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

    //we need to add this to check for duplicates to prevent users from added the same elements
    boolean checkForDuplicates(List<Element> sheets, String element) {

        boolean containsDuplicates = false;
        for (Element sheet : sheets) {
            if (sheet.getText().toString().matches(element)) {
                containsDuplicates = true;
                Log.d(TAG, "Cannot add duplicate sheet elements");
            }
        }

        return containsDuplicates;
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
    public class speechListener implements RecognitionListener {
        public void onRmsChanged(float rmsdB) { }
        public void onBufferReceived(byte[] buffer){ }
        public void onPartialResults(Bundle partialResults) { }
        public void onEvent(int eventType, Bundle params) { }
        public void onBeginningOfSpeech() { }

        public void onReadyForSpeech(Bundle params) {
            waitingForResults = true;
        }

        public void onEndOfSpeech() {
            voiceButton.setText("Wait for Results");
        }

        public void onResults(Bundle results) {
            ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            textToSpeechOutput = data.get(0);
            voiceButton.setText("Speech to Text");
            waitingForResults = false;
        }

        public void onError(int error) {
            Timber.tag(TAG).d("ERROR Speech to TEXT: "+error);
        }
    }

}
