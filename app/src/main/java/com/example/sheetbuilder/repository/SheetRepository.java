package com.example.sheetbuilder.repository;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.sheetbuilder.VolleyCallBack;
import com.example.sheetbuilder.model.Element;
import com.example.sheetbuilder.model.Sheet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class SheetRepository {
    private final Context mContext;
    //private final ExecutorRunner mRunner;
    FirebaseFirestore db;

    private final List<Sheet> mSheetList = new CopyOnWriteArrayList<>();
    private final String TAG = getClass().getSimpleName();
    public Map<String, Object> result = new HashMap<>();
    public int id;
    public String userID;

    public SheetRepository(Application app){
        mContext = app;
        db = FirebaseFirestore.getInstance();
    }
    public List<Sheet> getAllSheets(){
        return mSheetList;
    }

    public void deleteSheet(Sheet sheet, final VolleyCallBack callBack){
        db.collection("sheet").document(sheet.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        callBack.onSuccess();
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }
    public void createSheet(String name, final VolleyCallBack callBack){
        Map<String, Object> sheet = new HashMap<>();
        sheet.put("sheetName", name);
        sheet.put("userID", userID);
        db.collection("sheet").document(Integer.toString(id)).set(sheet).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void renameSheet(Sheet sheet, String name, final VolleyCallBack callBack){
        db.collection("sheet").document(sheet.getId()).update("sheetName", name).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        callBack.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    public void loadSheets(String uid, final VolleyCallBack callBack){
        mSheetList.clear();
        this.userID = uid;

        ContentResolver resolver = mContext.getContentResolver();

        db.collection("sheet").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        String s = "";
                        String uId = "";
                        String i = document.getId();


                        for(Map.Entry e:document.getData().entrySet()){
                            if(e.getKey().toString().equals("sheetName")){
                                s = e.getValue().toString();
                            }else if(e.getKey().toString().equals("userID")){
                                uId = e.getValue().toString();
                            }

                            //Log.d(TAG, "In sheet load" + mSheetList);
                        }
                        Log.d(TAG, uId + " " + userID);
                        if(uId.equals(userID)) {
                            mSheetList.add(new Sheet(s, i, uId));
                        }


                    }
                    id = mSheetList.size() +2;
                    callBack.onSuccess();
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
        Log.d(TAG, "Completed sheet load" + mSheetList);
    }
}
