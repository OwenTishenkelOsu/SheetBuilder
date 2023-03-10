package com.example.sheetbuilder.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sheetbuilder.R;

import timber.log.Timber;

public class CreateSheetFragment extends Fragment implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    private EditText sheetname;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle savedInstanceState){
        View v;

        Timber.tag(TAG).d("onCreateView()");
        Activity activity = requireActivity();

        v = inf.inflate(R.layout.create_sheet_fragment, c, false);

        sheetname = v.findViewById(R.id.sheet_name);

        String[] temps = {"item1", "item2", "item3", "item4"};
        ArrayAdapter<String> t;
        t = new ArrayAdapter<String>(v.getContext(), R.layout.simple_list_item_1, temps);
        ListView list = v.findViewById(R.id.list_view);
        list.setAdapter(t);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getContext(), parent.getItemIdAtPosition(position) + "is selected", Toast.LENGTH_LONG).show();
                view.setSelected(true);
            }
        });

        final Button createSheetButton = v.findViewById(R.id.create_sheet_button);
        if(createSheetButton!= null){
            createSheetButton.setOnClickListener(this);
        }
        return v;
    }

    @Override
    public void onClick(View view){
        final Activity activity = requireActivity();
        final int vId = view.getId();

        Timber.tag(TAG).d("Received button click!");

        if(vId==R.id.create_sheet_button){
            //new account
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
}
