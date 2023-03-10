package model;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import timber.log.Timber;

public class SheetLiveData extends MutableLiveData<List<Sheet>> {
    private final String TAG = getClass().getSimpleName();
    public SheetLiveData(Context c){
        Timber.tag(TAG).d("In SheetLiveData constructor");
    }
}
