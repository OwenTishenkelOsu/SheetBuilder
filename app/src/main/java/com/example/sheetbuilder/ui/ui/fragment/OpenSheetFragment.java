package com.example.sheetbuilder.ui.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.example.sheetbuilder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Sheet;
import model.viewmodel.SheetViewModel;
import timber.log.Timber;



public class OpenSheetFragment extends Fragment implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    FirebaseFirestore db;
    List<Sheet> mSheetList;
    SheetViewModel mSheetViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = requireActivity();
        mSheetViewModel = new SheetViewModel(activity.getApplication());
        mSheetViewModel.getAllSheets();
    }

    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle savedInstanceState){
        View v;

        db = FirebaseFirestore.getInstance();
        //names = new ArrayList<String>();

        Sheet sheet;


        Activity activity = requireActivity();

        v = inf.inflate(R.layout.open_sheet_fragment, c, false);

        String[] temps = {"item1", "item2", "item3", "item4"};



        ArrayAdapter<String> t;
        t = new ArrayAdapter<String>(v.getContext(), R.layout.simple_list_item_1, temps);
        ListView list = v.findViewById(R.id.list_view);
        list.setAdapter(t);

        final Button selectSheetButton = v.findViewById(R.id.select_sheet_button);
        if(selectSheetButton!= null){
            selectSheetButton.setOnClickListener(this);
        }
        final Button deleteSheetButton = v.findViewById(R.id.delete_sheet_button);
        if(deleteSheetButton!= null){
            deleteSheetButton.setOnClickListener(this);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(getContext(), parent.getItemIdAtPosition(position) + "is selected", Toast.LENGTH_LONG).show();
                view.setSelected(true);
                Timber.tag(TAG).d("clicking " + parent.getItemIdAtPosition(position));
            }
        });

        Timber.tag(TAG).d("COMPLETED CREATEVIEW");
        return v;
    }


    @Override
    public void onClick(View view){
        final Activity activity = requireActivity();
        final int vId = view.getId();

        Timber.tag(TAG).d("Received button click!");

        if(vId==R.id.select_sheet_button){
            //new account
        }else if(vId==R.id.delete_sheet_button){
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
