package model;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SheetRepository {
    private final Context mContext;
    //private final ExecutorRunner mRunner;
    private final List<Sheet> mSheetList = new CopyOnWriteArrayList<>();
    private final String TAG = getClass().getSimpleName();

    public SheetRepository(Application app){
        mContext = app;
        loadSheets();
    }
    public List<Sheet> getAllSheets(){
        return mSheetList;
    }

    private void loadSheets(){
        ContentResolver resolver = mContext.getContentResolver();
    }
}
