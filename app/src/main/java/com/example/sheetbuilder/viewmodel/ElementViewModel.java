package com.example.sheetbuilder.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import com.example.sheetbuilder.model.Element;
import com.example.sheetbuilder.model.Sheet;
import com.example.sheetbuilder.model.SheetLiveData;
import com.example.sheetbuilder.repository.ElementRepository;
import com.example.sheetbuilder.repository.SheetRepository;

public class ElementViewModel extends AndroidViewModel {
    public final ElementRepository mRepository;

    public ElementViewModel(Application app){
        super(app);
        mRepository = new ElementRepository(app);
    }
    public List<Element> getAllElements(){
        List<Element> mAllElementsList = mRepository.getAllElements();
        return mAllElementsList;
    }
}
