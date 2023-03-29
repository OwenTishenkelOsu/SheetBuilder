package com.example.sheetbuilder.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import com.example.sheetbuilder.model.Sheet;
import com.example.sheetbuilder.model.SheetLiveData;
import com.example.sheetbuilder.repository.SheetRepository;

public class SheetViewModel extends AndroidViewModel {
    public final SheetRepository mRepository;
    private final SheetLiveData mAllSheetData;

    public SheetViewModel(Application app){
        super(app);
        mRepository = new SheetRepository(app);
        mAllSheetData = new SheetLiveData(app);
    }
    public List<Sheet> getAllSheets(){
        List<Sheet> mAllSheetsList = mRepository.getAllSheets();
        mAllSheetData.setValue(mAllSheetsList);
        assert(mAllSheetData.getValue()!=null);
        return mAllSheetsList;
    }
}
