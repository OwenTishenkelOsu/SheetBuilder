package com.example.sheetbuilder;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Locale;

import timber.log.Timber;

public class SpeechToText {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private static String output = "";
    private SpeechRecognizer speechRecognizer;
    private Intent intent;
    private static boolean listening = false;
    private Activity activity;
    public  SpeechToText(Activity activity) {
        this.activity = activity;
        if(ContextCompat.checkSelfPermission(this.activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this.activity);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                Timber.d("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                Timber.d("Finished Listening.");
            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                output = data.get(0);
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this.activity,new String[]{Manifest.permission.RECORD_AUDIO},1);
        }
    }

    public boolean listen() {
        if(!listening){

            listening = true;

              intent  = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                    Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
            speechRecognizer.startListening(intent);
        } else{

            listening = false;
            speechRecognizer.stopListening();

            //output should go to wherever it's meant to go at this point
        }
        return listening;

    }
    public void destroy() {
        speechRecognizer.destroy();
        //Should be destroyed whenever not in active use, as it supposedly consumes a lot of battery life
    }
    public String retrieveText(){
        //Returns empty string if nothing was recorded
        return this.output;
    }
}
