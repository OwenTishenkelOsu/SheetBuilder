package com.example.sheetbuilder.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
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

        return v;
    }

    @Override
    public void onClick(View view){
        final Activity activity = requireActivity();
        final int vId = view.getId();

        Timber.tag(TAG).d("Received button click!");

        if(vId==R.id.add_element_button){
            addEditText();
        }else if(vId==R.id.save_sheet_button){
            saveElements();
        }
    }

    void addEditText(){
        EditText et = new EditText(getContext());
        et.setText("New Element");
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        et.setLayoutParams(params);
        editTexts.add(et);
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
