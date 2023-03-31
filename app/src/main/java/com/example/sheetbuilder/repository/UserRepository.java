package com.example.sheetbuilder.repository;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.sheetbuilder.VolleyCallBack;
import com.example.sheetbuilder.model.Element;
import com.example.sheetbuilder.model.Sheet;
import com.example.sheetbuilder.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserRepository {
    FirebaseFirestore db;
    private final List<User> mUserList = new CopyOnWriteArrayList<>();
    private final String TAG = getClass().getSimpleName();
    public Map<String, Object> result = new HashMap<>();
    public int id;

    public UserRepository(Application app){
        db = FirebaseFirestore.getInstance();
        id = 4;
    }
    public List<User> getAllUsers(){
        return mUserList;
    }


    public void createUser(String name, VolleyCallBack callBack){
        boolean b = false;
        for(User u : mUserList){
            if(u.getEmail().equals(name)){
                b=true;
            }
        }
        if(!b){
            Map<String, Object> user = new HashMap<>();
            user.put("email", name);
            db.collection("users").document(Integer.toString(id)).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            callBack.onSuccess();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
            id++;
        }
        callBack.onSuccess();
    }

    public void loadUsers(VolleyCallBack callBack){
        mUserList.clear();

        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        String s = "";
                        String text = "";
                        String i = document.getId();
                        for(Map.Entry e:document.getData().entrySet()){

                                s = e.getValue().toString();


                            Log.d(TAG, "In    USER    load" + mUserList);
                        }
                        mUserList.add(new User(s, i));


                    }
                    id = mUserList.size() +2;
                    callBack.onSuccess();
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }
}
