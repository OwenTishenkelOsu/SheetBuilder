package model;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

    public SheetRepository(Application app){
        mContext = app;
        db = FirebaseFirestore.getInstance();
        loadSheets();
    }
    public List<Sheet> getAllSheets(){
        return mSheetList;
    }

    private void loadSheets(){

        ContentResolver resolver = mContext.getContentResolver();

        db.collection("sheet").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        String s;
                        for(Map.Entry e:document.getData().entrySet()){
                            s = e.getValue().toString();
                            Log.d(TAG, s);
                            mSheetList.add(new Sheet(s));
                            Log.d(TAG, "In sheet load" + mSheetList);
                        }
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
        Log.d(TAG, "Completed sheet load" + mSheetList);
    }
}
