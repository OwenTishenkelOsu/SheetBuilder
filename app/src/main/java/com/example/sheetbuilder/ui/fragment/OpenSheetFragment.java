package com.example.sheetbuilder.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sheetbuilder.R;
import com.example.sheetbuilder.ui.activity.LogInActivity;
import com.example.sheetbuilder.ui.activity.SheetActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Array;
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

    private ImageView selectSheetButton, deleteSheetButton, createSheetButton, renameSheetButton, signOutButton;

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

        userID = this.getArguments().getString("userid");
        mSheetViewModel = new SheetViewModel(activity.getApplication());
        mSheetViewModel.mRepository.loadSheets(userID, ()-> showSheets()); //loads sheets using userID then displays them
    }

    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle savedInstanceState){
        View v;

        db = FirebaseFirestore.getInstance();
        //names = new ArrayList<String>();

        Sheet sheet;


        Activity activity = requireActivity();

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation(); //check rotation for display

        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            v = inf.inflate(R.layout.open_sheet_fragment_land, c, false);
        } else {
            v = inf.inflate(R.layout.open_sheet_fragment, c, false);
        }

        //v = inf.inflate(R.layout.open_sheet_fragment, c, false);

        selectSheetButton=v.findViewById(R.id.select_sheet_button);
        selectSheetButton.setOnClickListener(this);

        deleteSheetButton=v.findViewById(R.id.delete_sheet_button);
        deleteSheetButton.setOnClickListener(this);

        createSheetButton=v.findViewById(R.id.create_sheet_button);
        createSheetButton.setOnClickListener(this);

        renameSheetButton=v.findViewById(R.id.rename_sheet_button);
        renameSheetButton.setOnClickListener(this);

        signOutButton=v.findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(this);

        pageName = v.findViewById(R.id.page_title);
        pageName.setText(sheetUserName); //sets pageName to be the name of user

        /*ArrayAdapter<String> t;
        t = new ArrayAdapter<String>(v.getContext(), R.layout.simple_list_item_1, temps);
        ListView list = v.findViewById(R.id.list_view);
        list.setAdapter(t);*/

        sheetname = v.findViewById(R.id.sheet_name);

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
            b.putString("userid", userID);
            intent.putExtras(b);
            startActivity(intent);
            activity.finish();
        }else if(vId==R.id.delete_sheet_button){
            mSheetViewModel.mRepository.deleteSheet(mSheet, ()->mSheetViewModel.mRepository.loadSheets(userID, ()-> showSheets()));
        }else if(vId == R.id.create_sheet_button){
            //This checks to see if the sheet name is empty or just a bunch of whitespace
            if(sheetname.getText().toString().trim().length()==0) {
                Toast.makeText(activity, "You cannot enter an empty sheet name", Toast.LENGTH_SHORT).show();
            }
            else {
                mSheetViewModel.mRepository.createSheet(sheetname.getText().toString(), ()->mSheetViewModel.mRepository.loadSheets(userID, ()-> showSheets()));
            }
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
        //creates new adapter for sheets
        if(mSheetList!= null){
            mSheetAdapter = new SheetAdapter(mSheetList);
            mSheetRecyclerView.setAdapter(mSheetAdapter);
            mSheetRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    private static class SheetHolder extends RecyclerView.ViewHolder {
        private final TextView mSheetTextView;
        private final ImageView mSheetImageView;

        View v;
        SheetHolder(LayoutInflater inf, ViewGroup parent){
            super(inf.inflate(R.layout.sheet_list_item, parent, false));

            mSheetTextView = itemView.findViewById(R.id.sheet_info);
            mSheetImageView = itemView.findViewById(R.id.fp_selector);
        }
        void bind(Sheet sheet){
            String name = sheet.getName();
            mSheetTextView.setText(name);
        }

    }

    private class SheetAdapter extends RecyclerView.Adapter<SheetHolder>{
        private final List<Sheet> mSheetList;
        private int selectedPos = RecyclerView.NO_POSITION;

        private int lastClickedPosition = -1;

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

            Timber.tag(TAG).d("Called onBindViewHolder");
            if(lastClickedPosition == pos) {
                //Displays Pointer next to Selected Item
                holder.mSheetImageView.setVisibility(View.VISIBLE);
            }
            else{
                //this makes the pointer disappear
                holder.mSheetImageView.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Timber.tag(TAG).d(sheet.getName());
                    mSheet = sheet;

                    if(lastClickedPosition != -1)
                    {
                        //this calls onBindViewHolder with the last Clicked Item Position
                        notifyItemChanged(lastClickedPosition);
                    }
                    //this resets the last clicked position to the currently selected item
                    //And finally makes the pointer visible that is currently selected
                    lastClickedPosition = holder.getAdapterPosition();
                    notifyItemChanged(lastClickedPosition);
                }
            });
        }

        @Override
        public int getItemCount(){return mSheetList.size();}
    }
}

