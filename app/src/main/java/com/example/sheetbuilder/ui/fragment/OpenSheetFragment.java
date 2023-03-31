package com.example.sheetbuilder.ui.fragment;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sheetbuilder.R;
import com.example.sheetbuilder.ui.activity.CreateSheetActivity;
import com.example.sheetbuilder.ui.activity.LogInActivity;
import com.example.sheetbuilder.ui.activity.OpenSheetActivity;
import com.example.sheetbuilder.ui.activity.SheetActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import com.example.sheetbuilder.model.Sheet;
import com.example.sheetbuilder.viewmodel.SheetViewModel;
import timber.log.Timber;



public class OpenSheetFragment extends Fragment implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    FirebaseFirestore db;

    List<Sheet> mSheetList;
    SheetViewModel mSheetViewModel;

    private SheetAdapter mSheetAdapter;
    private RecyclerView mSheetRecyclerView;
    private EditText sheetname;
    private Sheet mSheet;

    private String sheetUserName;
    private TextView pageName;

    //Google sign in to use across fragment classes LoginFragment and OpenSheetFragment
    private GoogleSignInOptions gSignInOptions;
    private GoogleSignInClient gSignInClient;
    private String userID;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = requireActivity();


        gSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        // Build a GoogleSignInClient with the options specified by gso.
        Timber.tag(TAG).d("Successfully Created Google SignInClient");
        gSignInClient = GoogleSignIn.getClient(getContext(), gSignInOptions);

        /* user email along with other data from google parcelable object account (below)*/
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if(account != null)
        {
            //used in onCreateView within pageName
            sheetUserName = account.getDisplayName();

            //ie String email = account.getEmail();
        }
        else
        {
            Toast.makeText(activity, "account is null", Toast.LENGTH_SHORT).show();
        }

        userID = this.getArguments().getString("id");
        mSheetViewModel = new SheetViewModel(activity.getApplication());
        mSheetViewModel.mRepository.loadSheets(userID, ()-> showSheets());
    }

    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle savedInstanceState){
        View v;

        db = FirebaseFirestore.getInstance();
        //names = new ArrayList<String>();

        Sheet sheet;


        Activity activity = requireActivity();

        v = inf.inflate(R.layout.open_sheet_fragment, c, false);

        pageName = v.findViewById(R.id.page_title);
        pageName.setText(sheetUserName);

        /*ArrayAdapter<String> t;
        t = new ArrayAdapter<String>(v.getContext(), R.layout.simple_list_item_1, temps);
        ListView list = v.findViewById(R.id.list_view);
        list.setAdapter(t);*/

        sheetname = v.findViewById(R.id.sheet_name);
        final Button selectSheetButton = v.findViewById(R.id.select_sheet_button);
        if(selectSheetButton!= null){
            selectSheetButton.setOnClickListener(this);
        }
        final Button deleteSheetButton = v.findViewById(R.id.delete_sheet_button);
        if(deleteSheetButton!= null){
            deleteSheetButton.setOnClickListener(this);
        }
        final Button createSheetButton = v.findViewById(R.id.create_sheet_button);
        if(createSheetButton!= null){
            createSheetButton.setOnClickListener(this);
        }
        final Button renameSheetButton = v.findViewById(R.id.rename_sheet_button);
        if(renameSheetButton!= null){
            renameSheetButton.setOnClickListener(this);
        }
        final Button signOutButton = v.findViewById(R.id.sign_out_button);
        if(signOutButton!= null){
            signOutButton.setOnClickListener(this);
        }


        /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(getContext(), parent.getItemIdAtPosition(position) + "is selected", Toast.LENGTH_LONG).show();
                view.setSelected(true);
                Timber.tag(TAG).d("clicking " + parent.getItemIdAtPosition(position));
            }
        });*/

        Timber.tag(TAG).d("COMPLETED CREATEVIEW");
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState){
        super.onViewCreated(v, savedInstanceState);
        Activity activity = requireActivity();
        mSheetRecyclerView = v.findViewById(R.id.sheet_recycler_view);
        mSheetRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }


    @Override
    public void onClick(View view){
        final Activity activity = requireActivity();
        final int vId = view.getId();

        Timber.tag(TAG).d("Received button click!");

        if(vId==R.id.select_sheet_button && mSheet != null){
            Intent intent = new Intent(activity, SheetActivity.class);
            Bundle b = new Bundle(); //add sheetId and sheetName to bundle for SheetActivity
            b.putInt("id", Integer.parseInt(mSheet.getId()));
            b.putString("name", mSheet.getName());
            intent.putExtras(b);
            startActivity(intent);
            activity.finish();
        }else if(vId==R.id.delete_sheet_button){
            mSheetViewModel.mRepository.deleteSheet(mSheet, ()->mSheetViewModel.mRepository.loadSheets(userID, ()-> showSheets()));
        }else if(vId == R.id.create_sheet_button){
            mSheetViewModel.mRepository.createSheet(sheetname.getText().toString(), ()->mSheetViewModel.mRepository.loadSheets(userID, ()-> showSheets()));
        }else if(vId==R.id.rename_sheet_button){
            mSheetViewModel.mRepository.renameSheet(mSheet, sheetname.getText().toString(), ()->mSheetViewModel.mRepository.loadSheets(userID, ()-> showSheets()));
        }
        else if(vId==R.id.sign_out_button){
           signOut((activity));
        }
    }

    /*
        Signout Function (signs out using google signInClient object method)
        then perform an intent to go back to the Login activity
     */
    void signOut(Activity activity)
    {
        gSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(activity, LogInActivity.class);
                startActivity(intent);
                activity.finish();
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        Timber.tag(TAG).d("OnStart()");
    }

    @Override
    public void onResume(){
        super.onResume();
        showSheets();
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

    public void showSheets(){
        Timber.tag(TAG).d("in showSheets()");
        mSheetList = mSheetViewModel.getAllSheets();
        Timber.tag(TAG).d("SheetList contents: " + mSheetList);
        if(mSheetList!= null){
            mSheetAdapter = new SheetAdapter(mSheetList);
            mSheetRecyclerView.setAdapter(mSheetAdapter);
            mSheetRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    private static class SheetHolder extends RecyclerView.ViewHolder {
        private final TextView mSheetTextView;
        View v;
        SheetHolder(LayoutInflater inf, ViewGroup parent){
            super(inf.inflate(R.layout.sheet_list_item, parent, false));

            mSheetTextView = itemView.findViewById(R.id.sheet_info);
        }
        void bind(Sheet sheet){
            String name = sheet.getName();
            mSheetTextView.setText(name);
        }

    }

    private class SheetAdapter extends RecyclerView.Adapter<SheetHolder>{
        private final List<Sheet> mSheetList;
        private int selectedPos = RecyclerView.NO_POSITION;

        SheetAdapter(List<Sheet> sheetList) {mSheetList = sheetList;}

        @NonNull
        @Override
        public SheetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            LayoutInflater inf = requireActivity().getLayoutInflater();
            return new SheetHolder(inf, parent);
        }
        @Override
        public void onBindViewHolder(@NonNull SheetHolder holder, int pos){
            Sheet sheet = mSheetList.get(pos);
            holder.bind(sheet);
            holder.itemView.setSelected(selectedPos == pos);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Timber.tag(TAG).d(sheet.getName());
                    Toast.makeText(getContext(), sheet.getName(), Toast.LENGTH_SHORT).show();
                    mSheet = sheet;
                }
            });
        }

        @Override
        public int getItemCount(){return mSheetList.size();}
    }
}

