package com.example.sheetbuilder.repository;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class ElementRepository {
    FirebaseFirestore db;
    private final List<Element> mElementList = new CopyOnWriteArrayList<>();
    private final String TAG = getClass().getSimpleName();
    public Map<String, Object> result = new HashMap<>();
    public int id;

    public ElementRepository(Application app){
        db = FirebaseFirestore.getInstance();
        id = 10;
    }
    public List<Element> getAllElements(){
        return mElementList;
    }

    public void addElement(String text, String sheetID, VolleyCallBack callBack){

        mElementList.add(new Element(text, Integer.toString(id), sheetID));
        Map<String, Object> element = new HashMap<>();
        element.put("sheetID", sheetID);
        element.put("text", text);
        db.collection("element").document(Integer.toString(id)).set(element).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void saveElements(String sheetId, VolleyCallBack callBack){

        for(Element e : mElementList) {
        db.collection("element").document(e.getId()).update("text", e.getText()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        //callBack.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
        }
        callBack.onSuccess();
    }

    public void deleteElement(Element e, VolleyCallBack callBack){

        db.collection("element").document(e.getId())
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



    public void loadElements(String sheetId, VolleyCallBack callBack){
        mElementList.clear();

        db.collection("element").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        String s = "";
                        String text = "";
                        String i = document.getId();
                        for(Map.Entry e:document.getData().entrySet()){
                            if(e.getKey().toString().equals("sheetID")){
                                s = e.getValue().toString();
                            }else if(e.getKey().toString().equals("text")){
                                text = e.getValue().toString();
                            }

                            //Log.d(TAG, "In sheet load" + mSheetList);
                        }
                        if(s.equals(sheetId)) {
                            mElementList.add(new Element(text, i, s));
                        }

                    }
                    id = mElementList.size() +10;
                    callBack.onSuccess();
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }
}
