package model.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import model.Sheet;
import model.SheetLiveData;
import model.SheetRepository;

public class SheetViewModel extends AndroidViewModel {
    private final SheetRepository mRepository;
    private final SheetLiveData mAllSheetData;

    public SheetViewModel(Application app){
        super(app);
        mRepository = new SheetRepository(app);
        mAllSheetData = new SheetLiveData(app);
    }
    public SheetLiveData getAllSheets(){
        List<Sheet> mAllSheetsList = mRepository.getAllSheets();
        mAllSheetData.setValue(mAllSheetsList);
        assert(mAllSheetData.getValue()!=null);
        return mAllSheetData;
    }
}
